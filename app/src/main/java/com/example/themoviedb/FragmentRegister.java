package com.example.themoviedb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;

public class FragmentRegister extends Fragment {
    private static final String TAG = "MovieRegister";
    private static final int PICK_IMAGE = 1;
    private static final int MAX_SIZE = 200;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;

    private CheckBox checkBoxShowPassword;

    private Button buttonRegister;
    private Button buttonAddProfilePicture;

    private ImageView imageViewProfilePicture;

    private DatabaseHelper databaseHelper;

    public FragmentRegister() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ((MainActivity) getActivity()).setBottomNavigationMenu(MainActivity.MENU_START);

        editTextUsername = view.findViewById(R.id.editText_Register_Username);
        editTextPassword = view.findViewById(R.id.editText_Register_Password);
        editTextPasswordAgain = view.findViewById(R.id.editText_Register_PasswordAgain);
        checkBoxShowPassword = view.findViewById(R.id.checkBox_Register_ShowPassword);
        buttonRegister = view.findViewById(R.id.button_Register_SignUp);
        buttonAddProfilePicture = view.findViewById(R.id.button_Register_AddProfilePicture);
        imageViewProfilePicture = view.findViewById(R.id.imageView_Register_ProfilePicture);

        databaseHelper = new DatabaseHelper(getContext());

        if (savedInstanceState != null){
            editTextUsername.setText(savedInstanceState.getString("username"));
            checkBoxShowPassword.setChecked(savedInstanceState.getBoolean("checked"));
        }

        checkBoxShowPassword.setOnCheckedChangeListener(onCheckedChangeListener);
        buttonRegister.setOnClickListener(onClickListenerForRegisterButton);
        buttonAddProfilePicture.setOnClickListener(onClickListenerForPictureButton);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", editTextUsername.getText().toString());
        outState.putBoolean("checked", checkBoxShowPassword.isChecked());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            imageViewProfilePicture.setImageURI(selectedImage);
        }
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                editTextPassword.setTransformationMethod(null);
                editTextPasswordAgain.setTransformationMethod(null);
            }
            else{
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                editTextPasswordAgain.setTransformationMethod(new PasswordTransformationMethod());
            }
        }
    };

    private View.OnClickListener onClickListenerForRegisterButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (editTextPassword.getText().toString().equals(editTextPasswordAgain.getText().toString())){
                Profile profile = new Profile(
                        editTextUsername.getText().toString(),
                        editTextPassword.getText().toString(),
                        scaleProfilePicture());
                databaseHelper.register(profile);
                MainActivity.currentUser = editTextUsername.getText().toString();
                Toast.makeText(getContext(), "Register successful", Toast.LENGTH_LONG).show();

                ((MainActivity) getActivity()).replaceFragment(MainActivity.FRAGMENT_HOME);
            }
            else{
                Toast.makeText(getContext(), "Check password!", Toast.LENGTH_LONG).show();
            }
        }
    };

    private View.OnClickListener onClickListenerForPictureButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    };

    private Bitmap scaleProfilePicture(){
        Bitmap image = ((BitmapDrawable) imageViewProfilePicture.getDrawable()).getBitmap();
        int width = image.getWidth();
        int height = image.getHeight();
        float ratio = (float) width/height;

        if (ratio > 1){
            width = MAX_SIZE;
            height = (int) (width / ratio);
        }
        else{
            height = MAX_SIZE;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }
}

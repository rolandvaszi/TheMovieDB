package com.example.themoviedb;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class FragmentProfile extends Fragment {
    private static final String TAG = "MovieProfile";
    private static final int PICK_IMAGE = 1;
    private static final int MAX_SIZE = 400;

    private TextView textViewUsername;
    private TextView textViewShowPasswordLabel;
    private ImageView imageViewProfilePicture;
    private Button buttonChangePassword;
    private Button buttonChangeProfilePicture;
    private Button buttonSave;
    private EditText editTextPassword;
    private EditText editTextPasswordAgain;
    private CheckBox checkBoxShowPassword;

    private DatabaseHelper databaseHelper;
    private Profile currentProfile;

    private int visibleModifiers = View.INVISIBLE;

    public FragmentProfile() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUsername = view.findViewById(R.id.textView_Profile_Username);
        textViewShowPasswordLabel = view.findViewById(R.id.textView_Profile_ShowPasswordLabel);
        imageViewProfilePicture = view.findViewById(R.id.imageView_Profile_ProfilePicture);
        buttonChangePassword = view.findViewById(R.id.button_Profile_ChangePassword);
        buttonChangeProfilePicture = view.findViewById(R.id.button_Profile_ChangeProfilePicture);
        buttonSave = view.findViewById(R.id.button_Profile_Save);
        editTextPassword = view.findViewById(R.id.editText_Profile_Password);
        editTextPasswordAgain = view.findViewById(R.id.editText_Profile_PasswordAgain);
        checkBoxShowPassword = view.findViewById(R.id.checkBox_Profile_ShowPassword);

        buttonChangePassword.setOnClickListener(onClickListenerForPasswordButton);
        buttonChangeProfilePicture.setOnClickListener(onClickListenerForPictureButton);
        buttonSave.setOnClickListener(onClickListenerForSaveButton);

        databaseHelper = new DatabaseHelper(getContext());
        currentProfile = databaseHelper.getProfile();

        if (savedInstanceState != null){
            checkBoxShowPassword.setChecked(savedInstanceState.getBoolean("checked"));
            visibleModifiers = savedInstanceState.getInt("visible_modifiers");
            setVisibilityForModifiers();
        }

        textViewUsername.setText(currentProfile.getUserName());
        imageViewProfilePicture.setImageBitmap(currentProfile.getProfilePictureBitmap());

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("checked", checkBoxShowPassword.isChecked());
        outState.putInt("visible_modifiers", visibleModifiers);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri selectedImage = data.getData();
            imageViewProfilePicture.setImageURI(selectedImage);
        }
    }

    private View.OnClickListener onClickListenerForPasswordButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            visibleModifiers = View.VISIBLE;
            setVisibilityForModifiers();
        }
    };

    private View.OnClickListener onClickListenerForPictureButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            buttonSave.setVisibility(View.VISIBLE);
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
        }
    };

    private  View.OnClickListener onClickListenerForSaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (visibleModifiers == View.VISIBLE && passwordIsValid()){
                currentProfile.setPassword(editTextPassword.getText().toString());
            }

            try {
                currentProfile.setProfilePicture(scaleProfilePicture());
            }
            catch (NullPointerException e){
                Log.d(TAG, "No profile picture");
            }

            if (databaseHelper.updateProfile(currentProfile) > 0){
                Toast.makeText(getContext(), "Profile updated.", Toast.LENGTH_LONG).show();
                ((MainActivity) getActivity()).replaceFragment(MainActivity.FRAGMENT_HOME);
            }
            else{
                Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_LONG).show();
            }
        }
    };

    private boolean passwordIsValid(){
        return (!editTextPassword.getText().toString().equals(currentProfile.getPassword()) &&
                 editTextPassword.getText().toString().equals(editTextPasswordAgain.getText().toString()));
    }

    private void setVisibilityForModifiers(){
        editTextPassword.setVisibility(visibleModifiers);
        editTextPasswordAgain.setVisibility(visibleModifiers);
        checkBoxShowPassword.setVisibility(visibleModifiers);
        textViewShowPasswordLabel.setVisibility(visibleModifiers);
        buttonSave.setVisibility(visibleModifiers);
    }

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

        return Bitmap.createScaledBitmap(image, width, height, false);
    }
}

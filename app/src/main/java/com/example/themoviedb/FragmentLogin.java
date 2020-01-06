package com.example.themoviedb;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FragmentLogin extends Fragment {
    private static final String TAG = "MovieLogin";

    private EditText editTextUsername;
    private EditText editTextPassword;
    private CheckBox checkBoxShowPassword;
    private Button buttonLogin;

    private DatabaseHelper databaseHelper;

    public FragmentLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ((MainActivity) getActivity()).setBottomNavigationMenu(MainActivity.MENU_START);

        editTextUsername = view.findViewById(R.id.editText_LogIn_Username);
        editTextPassword = view.findViewById(R.id.editText_LogIn_Password);
        checkBoxShowPassword = view.findViewById(R.id.checkBox_LogIn_ShowPassword);
        buttonLogin = view.findViewById(R.id.button_LogIn_Login);

        databaseHelper = new DatabaseHelper(getContext());

        if (savedInstanceState != null){
            editTextUsername.setText(savedInstanceState.getString("username"));
            checkBoxShowPassword.setChecked(savedInstanceState.getBoolean("checked"));
        }

        checkBoxShowPassword.setOnCheckedChangeListener(onCheckedChangeListener);
        buttonLogin.setOnClickListener(onClickListener);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("username", editTextUsername.getText().toString());
        outState.putBoolean("checked", checkBoxShowPassword.isChecked());
    }

    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b){
                editTextPassword.setTransformationMethod(null);
            }
            else{
                editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        if (databaseHelper.login(editTextUsername.getText().toString(), editTextPassword.getText().toString())){
            MainActivity.currentUser = editTextUsername.getText().toString();
            Toast.makeText(getContext(), "Log in successful!", Toast.LENGTH_LONG).show();

            ((MainActivity) getActivity()).replaceFragment(MainActivity.FRAGMENT_HOME);
        }
        else{
            Toast.makeText(getContext(), "Incorrect username or password!", Toast.LENGTH_LONG).show();
        }
        }
    };
}

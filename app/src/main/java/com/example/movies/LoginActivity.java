package com.example.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button sign_in_first, sign_up_first, sign_in_second, sign_up_second;
    LinearLayout buttons_layout, sign_in_layout, sign_up_layout;
    TextView sign_in_login, sign_in_password, sign_up_personName, sign_up_email, sign_up_login, sign_up_password, sign_up_repeat_password;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initialComponents();
    }

    @Override
    public void onBackPressed() {
        sign_in_layout.setVisibility(View.GONE);
        sign_up_layout.setVisibility(View.GONE);
        buttons_layout.setVisibility(View.VISIBLE);
    }

    private void initialComponents() {
        sign_in_first = findViewById(R.id.sign_in_first);
        sign_up_first = findViewById(R.id.sign_up_first);
        sign_in_second = findViewById(R.id.sign_in_second);
        sign_up_second = findViewById(R.id.sign_up_second);

        buttons_layout = findViewById(R.id.buttons_layout);
        sign_in_layout = findViewById(R.id.sign_in_layout);
        sign_up_layout = findViewById(R.id.sign_up_layout);

        sign_in_login = findViewById(R.id.sign_in_login);
        sign_in_password = findViewById(R.id.sign_in_password);

        sign_up_personName = findViewById(R.id.sign_up_personName);
        sign_up_email = findViewById(R.id.sign_up_email);
        sign_up_login = findViewById(R.id.sign_up_login);
        sign_up_password = findViewById(R.id.sign_up_password);
        sign_up_repeat_password = findViewById(R.id.sign_up_repeat_password);

        mSettings = getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    private boolean isEmailValid(CharSequence target) {
        if (target == null) return false;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isSignUpValid() {
        if (sign_up_personName.getText().toString().isEmpty()) return false;
        if (sign_up_login.getText().toString().isEmpty()) return false;
        if (sign_up_password.getText().toString().isEmpty() ||
                sign_up_repeat_password.getText().toString().isEmpty() ||
                !sign_up_password.getText().toString().equals(sign_up_repeat_password.getText().toString())
        ) return false;
        return isEmailValid(sign_up_email.getText().toString());
    }

    private boolean isSignInValid() {
        if (sign_in_login.getText().toString().isEmpty()) return false;
        if (sign_in_password.getText().toString().isEmpty()) return false;
        return true;
    }

    private boolean isSignInSuccessful() {
        if (!mSettings.contains("Login") || !mSettings.contains("Password")) return false;
        if (!sign_in_login.getText().toString().equals(mSettings.getString("Login","")) ||
                !sign_in_password.getText().toString().equals(mSettings.getString("Password",""))
        ) return false;
        return true;
    }

    private void saveRegisterData() {
        SharedPreferences.Editor editor = mSettings.edit();
        editor.clear();
        editor.putString("PersonName", sign_up_personName.getText().toString());
        editor.putString("Email",sign_up_email.getText().toString());
        editor.putString("Login",sign_up_login.getText().toString());
        editor.putString("Password",sign_up_password.getText().toString());
        editor.apply();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_first:
                buttons_layout.setVisibility(View.GONE);
                sign_in_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.sign_up_first:
                buttons_layout.setVisibility(View.GONE);
                sign_up_layout.setVisibility(View.VISIBLE);
                break;
            case R.id.sign_in_second:
                if (!isSignInValid()) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid values", Toast.LENGTH_SHORT).show();
                } else {
                    if (!isSignInSuccessful()) {
                        Toast.makeText(getApplicationContext(),
                                "Invalid login or password", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }
                }
                break;
            case R.id.sign_up_second:
                if(!isSignUpValid()) {
                    Toast.makeText(getApplicationContext(),
                            "Invalid values", Toast.LENGTH_SHORT).show();
                } else {
                    saveRegisterData();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(sign_up_second.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    sign_up_layout.setVisibility(View.GONE);
                    buttons_layout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }
}
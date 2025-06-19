package com.liu.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button buttonLogin;

    // 预设的用户名和密码
    private static final String CORRECT_USERNAME = "zzz";
    private static final String CORRECT_PASSWORD = "123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast.makeText(this, R.string.username_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, R.string.password_cannot_be_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        if (username.equals(CORRECT_USERNAME) && password.equals(CORRECT_PASSWORD)) {
            Toast.makeText(this, R.string.login_success, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("USERNAME_EXTRA", username); // 将用户名传递给MainActivity
            startActivity(intent);
            finish(); // 关闭登录页
        } else {
            Toast.makeText(this, R.string.login_failed, Toast.LENGTH_SHORT).show();
        }
    }
}
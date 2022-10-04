package huce.fit.appreadstories.controller.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.TaiKhoan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountCreateActivity extends AppCompatActivity {

    private EditText etName, etEmail, etUsername, etPasswword1, etPasswword2;
    private Button btExit, btOk;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_create);

        btExit = findViewById(R.id.btExit);
        btOk = findViewById(R.id.btOk);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPasswword1 = findViewById(R.id.etPassword1);
        etPasswword2 = findViewById(R.id.etPassword2);

        processEvents();
    }

    private void processEvents() {
        btExit.setOnClickListener(v -> {
            finish();
        });
        btOk.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password1 = etPasswword1.getText().toString().trim();
            String password2 = etPasswword2.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String name = etName.getText().toString().trim();
            if (username.equals("") || password1.equals("") || password2.equals("") || email.equals("") || name.equals("")) {
                Toast.makeText(AccountCreateActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                if (password1.equals(password2)) {
                    createAccount(username, password1, email, name);
                } else {
                    Toast.makeText(AccountCreateActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createAccount(String username, String password, String email, String name) {
        Api.apiInterface().register(username, password, email, name).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int status = response.body().getAccountsuccess();
                    if (status == 0) {
                        Toast.makeText(AccountCreateActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                    } else if (status == 1) {
                        Toast.makeText(AccountCreateActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                        AccountLoginActivity accountLoginActivity = new AccountLoginActivity();
                        accountLoginActivity.close();

                        Intent intent = new Intent(AccountCreateActivity.this, AccountLoginActivity.class);
                        intent.putExtra("username", username);
                        startActivity(intent);

                        finish();
                    } else {
                        Toast.makeText(AccountCreateActivity.this, "Lỗi đăng ký!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(AccountCreateActivity.this, "Lỗi đường dẫn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

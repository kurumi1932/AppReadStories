package huce.fit.appreadstories.controller.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class AccountLoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin, btCreate;
    private String userNameCreate, username, password;

    @Override
    protected void onResume() {
        super.onResume();
        getSharedPreferences();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btCreate = findViewById(R.id.btCreate);

        userNameCreate = getIntent().getStringExtra("username");
        if (userNameCreate != null) {
            etUsername.setText(userNameCreate);
        }
        processEvents();
    }

    private void processEvents() {
        try {
            btLogin.setOnClickListener(v -> { //lambda
                username = etUsername.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                loginAccount(username, password);
            });
            btCreate.setOnClickListener(v -> {
                Intent intent = new Intent(AccountLoginActivity.this, AccountCreateActivity.class);
                startActivity(intent);
            });
        } catch (Exception ex) {
            Log.e("Events", ex.getMessage());
        }
    }

    private void loginAccount(String username, String password) {
        if (username.length() == 0) {
            Toast.makeText(AccountLoginActivity.this, "Bạn chưa nhập tên tài khoản!", Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(AccountLoginActivity.this, "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
        } else {
            Api.apiInterface().login(username, password).enqueue(new Callback<TaiKhoan>() {
                @Override
                public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getAccountsuccess() == 1) {
                            int idAccount = response.body().getMataikhoan();
                            String name = response.body().getTenhienthi();
                            //lưu vào bộ nhớ tạm của máy
                            setSharedPreferences(idAccount, name);

                            Intent intent = new Intent(AccountLoginActivity.this, MainActivity.class);
                            startActivity(intent);

                            finish();
                            Toast.makeText(AccountLoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AccountLoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<TaiKhoan> call, Throwable t) {
                    Toast.makeText(AccountLoginActivity.this, "Lỗi đường dẫn!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setSharedPreferences(int idAccount, String name) {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        SharedPreferences.Editor myedit = sharedPreferences.edit();

        myedit.putInt("idAccount", idAccount);
        myedit.putString("name", name);
        myedit.commit();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        int idAccount = sharedPreferences.getInt("idAccount", 0);
        if (idAccount != 0) {
            Intent intent = new Intent(AccountLoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void close() {
        finish();
    }
}

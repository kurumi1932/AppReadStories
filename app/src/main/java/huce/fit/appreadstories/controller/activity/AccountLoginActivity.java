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
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.TaiKhoan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountLoginActivity extends AppCompatActivity {
    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin, btCreate;
    private String username, password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);
        getSharedPreferences();

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btCreate = findViewById(R.id.btCreate);

        processEvents();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        if (!username.equals("")) {
            etUsername.setText(username);

            SharedPreferences.Editor myedit = sharedPreferences.edit();
            myedit.putString("username", "");
            myedit.commit();
        }
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(this);
        return checkNetwork.isNetwork();
    }

    private void processEvents() {
        btLogin.setOnClickListener(v -> { //lambda
            username = etUsername.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            if (isNetwork()) {
                loginAccount(username, password);
            } else {
                Toast.makeText(AccountLoginActivity.this, "Vui lòng kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
        btCreate.setOnClickListener(v -> {
            if (isNetwork()) {
                Intent intent = new Intent(AccountLoginActivity.this, AccountCreateActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AccountLoginActivity.this, "Vui lòng kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
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
                    Log.e("Err_AccountLogin", "loginAccount", t);
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
}

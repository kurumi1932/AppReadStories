package huce.fit.appreadstories.controller.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.TaiKhoan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountUpdateActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etName, etEmail, etOldPassword, etNewPassword;
    private Button btSave;
    private int idAccount;
    private String name, email, oldpass, newpass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etOldPassword = findViewById(R.id.etPassword1);
        etNewPassword = findViewById(R.id.etPassword2);

        ivBack = findViewById(R.id.ivBack);
        btSave = findViewById(R.id.btSave);

        getSharedPreferences();
        getAccount();
        processEvents();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idAccountUpdate", String.valueOf(idAccount));
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });
        btSave.setOnClickListener(v -> {
            checkPassword();
        });
    }

    private void getAccount() {
        Api.apiInterface().getAccount(idAccount).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                etName.setText(response.body().getTenhienthi());
                etEmail.setText(response.body().getEmail());
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(AccountUpdateActivity.this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPassword() {
        name = etName.getText().toString();
        email = etEmail.getText().toString();
        oldpass = etOldPassword.getText().toString();
        newpass = etNewPassword.getText().toString();
        Api.apiInterface().getAccount(idAccount).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (oldpass.equals("") && newpass.equals("")) {
                        if (name.equals("") || email.equals("")) {
                            Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập đầy đủ tên hiển thị và email!", Toast.LENGTH_SHORT).show();
                        } else {
                            updateAccount(idAccount, response.body().getMatkhau(), email, name);
                        }
                    }

                    if (oldpass.equals("") && !newpass.equals("")) {
                        Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
                    }
                    if (!oldpass.equals("") && newpass.equals("")) {
                        Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
                    }

                    if (!oldpass.equals("") && !newpass.equals("")) {
                        if (oldpass.equals(response.body().getMatkhau())) {
                            if (oldpass.equals(newpass)) {
                                Toast.makeText(AccountUpdateActivity.this, "Mật khẩu mới trùng với mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
                            } else {
                                if (response.body().getMatkhau().equals(oldpass)) {
                                    if (name.equals("") || email.equals("")) {
                                        Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập đầy đủ tên hiển thị và email!", Toast.LENGTH_SHORT).show();
                                    }
                                    if (!name.equals("") && !email.equals("")) {
                                        updateAccount(idAccount, newpass, email, name);
                                    }
                                } else {
                                    Toast.makeText(AccountUpdateActivity.this, "Mật khẩu hiện tại không chính xác!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(AccountUpdateActivity.this, "Mật khẩu hiện tại không chính xác!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(AccountUpdateActivity.this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAccount(int idAccount, String password, String email, String name) {
        Api.apiInterface().updateAccount(idAccount, password, email, name).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int status = response.body().getAccountsuccess();
                    if (status == 3) {
                        Toast.makeText(AccountUpdateActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    } else if (status == 1) {
                        Toast.makeText(AccountUpdateActivity.this, "Cập nhật tài khoản thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AccountUpdateActivity.this, "Lỗi thông tin cập nhật!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập Email khác!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package huce.fit.appreadstories.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    @SuppressLint("SimpleDateFormat")
    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Date currentDate = new Date();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);

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
            myedit.apply();
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
                Intent intent = new Intent(AccountLoginActivity.this, AccountRegisterActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(AccountLoginActivity.this, "Vui lòng kết nối mạng!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int age(String startDate) throws ParseException {
        String endDate = simpleDateFormat.format(currentDate);
        Date date1 = simpleDateFormat.parse(startDate);
        Date date2 = simpleDateFormat.parse(endDate);
        long getDiff = 0;
        if (date1 != null && date2 != null) {
            getDiff = date2.getTime() - date1.getTime();
            Log.e("date1", date1.toString());
            Log.e("date2", date2.toString());
        }
        Log.e("getDiff", String.valueOf(getDiff));

        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);//24h*60p*60s*1000
        int age = (int) getDaysDiff / 365;
        Log.e("getDaysDiff", String.valueOf(getDaysDiff));
        Log.e("age", String.valueOf(age));
        return age;
    }

    private void loginAccount(String username, String password) {
        if (username.length() == 0) {
            Toast.makeText(AccountLoginActivity.this, "Bạn chưa nhập tên tài khoản!", Toast.LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(AccountLoginActivity.this, "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
        } else {
            Api.apiInterface().login(username, password).enqueue(new Callback<TaiKhoan>() {
                @Override
                public void onResponse(@NonNull Call<TaiKhoan> call,@NonNull Response<TaiKhoan> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getAccountsuccess() == 1) {

                            int idAccount = response.body().getMataikhoan();
                            String password = response.body().getMatkhau();
                            String name = response.body().getTenhienthi();
                            String email = response.body().getEmail();
                            String startDate = response.body().getNgaysinh();
                            //lưu vào bộ nhớ tạm của máy
                            try {
                                setSharedPreferences(idAccount, password, name, email, startDate, age(startDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

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
                public void onFailure(@NonNull Call<TaiKhoan> call,@NonNull Throwable t) {
                    Log.e("Err_AccountLogin", "loginAccount", t);
                }
            });
        }
    }

    private void setSharedPreferences(int idAccount, String password, String name, String email, String birthDay, int age) {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        SharedPreferences.Editor myedit = sharedPreferences.edit();

        myedit.putInt("idAccount", idAccount);
        myedit.putString("password", password);
        myedit.putString("name", name);
        myedit.putString("email", email);
        myedit.putString("birthDay", birthDay);
        myedit.putInt("age", age);
        myedit.apply();
    }
}

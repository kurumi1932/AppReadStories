package huce.fit.appreadstories.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.TaiKhoan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountRegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etUsername, etPasswword1, etPasswword2, etBirthday;
    private ImageView ivDate;
    private Button btExit, btRegister;

    private final Calendar myCalendar = Calendar.getInstance();
    private final DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        @SuppressLint("SimpleDateFormat") String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(myCalendar.getTime());
        etBirthday.setText(dateStr);
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPasswword1 = findViewById(R.id.etPassword1);
        etPasswword2 = findViewById(R.id.etPassword2);
        etBirthday = findViewById(R.id.etBirthday);
        ivDate = findViewById(R.id.ivDate);

        btExit = findViewById(R.id.btExit);
        btRegister = findViewById(R.id.btRegister);

        processEvents();
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(this);
        return checkNetwork.isNetwork();
    }

    private void processEvents() {
        btExit.setOnClickListener(v -> finish());

        ivDate.setOnClickListener(v -> new DatePickerDialog(AccountRegisterActivity.this, d,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        btRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password1 = etPasswword1.getText().toString().trim();
            String password2 = etPasswword2.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();

            if (username.equals("") || password1.equals("") || password2.equals("") || email.equals("") || name.equals("") || birthday.equals("")) {
                Toast.makeText(AccountRegisterActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            } else {
                if (checkDate(birthday)) {
                    if (password1.equals(password2)) {
                        if (isNetwork()) {
                            createAccount(username, password1, email, name, birthday);
                        } else {
                            Toast.makeText(AccountRegisterActivity.this, "Vui lòng kết nối mạng!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AccountRegisterActivity.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AccountRegisterActivity.this, "Định dạng ngày không đúng!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean checkDate(String dateStr) {
        String[] dateArr = dateStr.split("-");//tách từng giá trị vào mảng khi gặp -
        if (Integer.parseInt(dateArr[0]) < 9999) {//năm ở vị trí 0 phải nhỏ hơn 9999
            return isValid(dateStr);
        } else {
            return false;
        }
    }

    public boolean isValid(String dateStr) {
        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);//convert date nếu sai thì chạy catch
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private void createAccount(String username, String password, String email, String name, String birthday) {
        Api.apiInterface().register(username, password, email, name, birthday).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(@NonNull Call<TaiKhoan> call, @NonNull Response<TaiKhoan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int status = response.body().getAccountsuccess();
                    if (status == 0) {
                        Toast.makeText(AccountRegisterActivity.this, "Tài khoản đã tồn tại!", Toast.LENGTH_SHORT).show();
                    } else if (status == 1) {
                        Toast.makeText(AccountRegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                        setSharedPreferences(username);
                        finish();
                    } else {
                        Toast.makeText(AccountRegisterActivity.this, "Lỗi đăng ký!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<TaiKhoan> call,@NonNull Throwable t) {
                Log.e("Err_AccountCreate", "createAccount", t);
            }
        });
    }

    private void setSharedPreferences(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        SharedPreferences.Editor myedit = sharedPreferences.edit();

        myedit.putString("username", username);
        myedit.apply();
    }
}

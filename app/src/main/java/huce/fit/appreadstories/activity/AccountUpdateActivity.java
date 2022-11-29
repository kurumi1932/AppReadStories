package huce.fit.appreadstories.activity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.model.TaiKhoan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountUpdateActivity extends AppCompatActivity {

    private ImageView ivBack;
    private EditText etName, etEmail, etOldPassword, etNewPassword, etBirthday;
    private ImageView ivDate;
    private Button btSave;
    private int idAccount;
    private String name, passwordOld, email, birthday, oldpass, newpass;
    private DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date currentDate = new Date();

    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener d = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String dateStr = new SimpleDateFormat("yyyy-MM-dd").format(myCalendar.getTime());
        etBirthday.setText(dateStr);
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etOldPassword = findViewById(R.id.etPassword1);
        etNewPassword = findViewById(R.id.etPassword2);
        etBirthday = findViewById(R.id.etBirthday);

        ivBack = findViewById(R.id.ivBack);
        ivDate = findViewById(R.id.ivDate);
        btSave = findViewById(R.id.btSave);

        getSharedPreferences();
        getAccount();
        processEvents();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        passwordOld = sharedPreferences.getString("password", "");
        name = sharedPreferences.getString("name", "");
        email = sharedPreferences.getString("email", "");
        birthday = sharedPreferences.getString("birthDay", "1000-01-01");
        Log.e("idAccountUpdate", String.valueOf(idAccount));
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(this);
        return checkNetwork.isNetwork();
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });

        ivDate.setOnClickListener(v -> {
            new DatePickerDialog(AccountUpdateActivity.this, d,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        btSave.setOnClickListener(v -> {
            if (isNetwork()) {
                checkPassword();
            } else {
                Toast.makeText(this, "Không có kết nối mạng!\n\tVui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAccount() {
        etName.setText(name);
        etEmail.setText(email);
        etBirthday.setText(birthday);
    }

    public boolean checkDate(String dateStr) {
        String[] dateArr = dateStr.split("-");
        if (Integer.parseInt(dateArr[0]) < 9999) {
            if (isValid(dateStr)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    private void checkPassword() {
        name = etName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        oldpass = etOldPassword.getText().toString().trim();
        newpass = etNewPassword.getText().toString().trim();
        birthday = etBirthday.getText().toString().trim();

        if (checkDate(birthday)) {
            if (name.equals("") || email.equals("")) {
                Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập đầy đủ tên hiển thị và email!", Toast.LENGTH_SHORT).show();
            } else {
                if (oldpass.equals("") && newpass.equals("")) {
                    updateAccount(passwordOld);
                }

                if (oldpass.equals("") && !newpass.equals("")) {
                    Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
                }
                if (!oldpass.equals("") && newpass.equals("")) {
                    Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập mật khẩu mới!", Toast.LENGTH_SHORT).show();
                }

                if (!oldpass.equals("") && !newpass.equals("")) {
                    if (oldpass.equals(passwordOld)) {
                        if (oldpass.equals(newpass)) {
                            Toast.makeText(AccountUpdateActivity.this, "Mật khẩu mới phải trùng với mật khẩu hiện tại!", Toast.LENGTH_SHORT).show();
                        } else {
                            updateAccount(newpass);
                        }
                    } else {
                        Toast.makeText(AccountUpdateActivity.this, "Mật khẩu hiện tại không chính xác!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            Toast.makeText(AccountUpdateActivity.this, "Định dạng ngày không đúng!", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAccount(String password) {
        Api.apiInterface().updateAccount(idAccount, password, email, name).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int status = response.body().getAccountsuccess();
                    if (status == 3) {
                        Toast.makeText(AccountUpdateActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    } else if (status == 1) {
                        Toast.makeText(AccountUpdateActivity.this, "Cập nhật tài khoản thành công", Toast.LENGTH_SHORT).show();

                        try {
                            setSharedPreferences(idAccount, password, name, email, birthday, age(birthday));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

    private int age(String startDate) throws ParseException {
        String endDate = simpleDateFormat.format(currentDate);
        Date date1 = simpleDateFormat.parse(startDate);
        Date date2 = simpleDateFormat.parse(endDate);
        long getDiff = date2.getTime() - date1.getTime();
        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);//24h*60p*60s*1000
        int age = (int) getDaysDiff / 365;
        Log.e("date1", date1.toString());
        Log.e("date2", date2.toString());
        Log.e("getDiff", String.valueOf(getDiff));
        Log.e("getDaysDiff", String.valueOf(getDaysDiff));
        Log.e("age", String.valueOf(age));
        return age;
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
        myedit.commit();
    }
}

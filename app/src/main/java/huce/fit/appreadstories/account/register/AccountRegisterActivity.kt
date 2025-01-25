package huce.fit.appreadstories.account.register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import huce.fit.appreadstories.R;


public class AccountRegisterActivity extends AppCompatActivity implements AccountRegisterView {

    private AccountRegisterPresenter mAccountRegisterPresenter;
    private EditText etName, etEmail, etUsername, etPassword1, etPassword2, etBirthday;
    private ImageView ivDate;
    private Button btExit, btRegister;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_register);

        init();
        processEvents();
    }

    private void init() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword1 = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);
        etBirthday = findViewById(R.id.etBirthday);
        ivDate = findViewById(R.id.ivDate);
        btExit = findViewById(R.id.btExit);
        btRegister = findViewById(R.id.btRegister);

        mAccountRegisterPresenter = new AccountRegisterImpl(this);
    }

    private void processEvents() {
        btExit.setOnClickListener(v -> finish());
        ivDate.setOnClickListener(v -> mAccountRegisterPresenter.openDatePicker());
        btRegister.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password1 = etPassword1.getText().toString().trim();
            String password2 = etPassword2.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String name = etName.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();

            if (password1.equals(password2)) {
                if (username.equals("") || password1.equals("") || email.equals("") || name.equals("") || birthday.equals("")) {
                    Toast.makeText(AccountRegisterActivity.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    mAccountRegisterPresenter.register(username, password1, email, name, birthday);
                }
            } else {
                Toast.makeText(AccountRegisterActivity.this, "Mật khẩu nhập lại không khớp!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void changeDatePicker(String birthday) {
        etBirthday.setText(birthday);
    }

    @Override
    public void register(int status) {
        switch (status) {
            case 0:
                Toast.makeText(AccountRegisterActivity.this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(AccountRegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 2:
                Toast.makeText(AccountRegisterActivity.this, "Vui lòng kết nối mạng", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(AccountRegisterActivity.this, "Định dạng ngày sinh không đúng", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}

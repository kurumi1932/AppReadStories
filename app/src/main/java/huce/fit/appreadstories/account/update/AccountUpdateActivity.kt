package huce.fit.appreadstories.account.update;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import huce.fit.appreadstories.R;

public class AccountUpdateActivity extends AppCompatActivity implements AccountUpdateView {

    private AccountUpdatePresenter mAccountUpdatePresenter;
    private ImageView ivBack;
    private EditText etName, etEmail, etOldPassword, etNewPassword, etBirthday;
    private ImageView ivDate;
    private Button btSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_update);

        init();
        processEvents();
    }

    private void init() {
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etOldPassword = findViewById(R.id.etPassword1);
        etNewPassword = findViewById(R.id.etPassword2);
        etBirthday = findViewById(R.id.etBirthday);
        ivBack = findViewById(R.id.ivBack);
        ivDate = findViewById(R.id.ivDate);
        btSave = findViewById(R.id.btSave);

        mAccountUpdatePresenter = new AccountUpdateImpl(this);
    }

    @Override
    public void getInfoAccount(String name, String email, String birthday) {
        etName.setText(name);
        etEmail.setText(email);
        etBirthday.setText(birthday);
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> finish());
        ivDate.setOnClickListener(v -> mAccountUpdatePresenter.openDatePicker());
        btSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String oldPass = etOldPassword.getText().toString().trim();
            String newPass = etNewPassword.getText().toString().trim();
            String birthday = etBirthday.getText().toString().trim();

            if (name.equals("") || email.equals("") || birthday.equals("")) {
                Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập đầy đủ tên hiển thị!", Toast.LENGTH_SHORT).show();
            } else {
                int idAccount = mAccountUpdatePresenter.getSharedPreferences().getIdAccount();
                if (oldPass.equals("")) {
                    if (newPass.equals("")) {
                        mAccountUpdatePresenter.updateAccount(idAccount, email, name, birthday);
                    } else {
                        Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập mật khẩu hiện tại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (newPass.equals("")) {
                        Toast.makeText(AccountUpdateActivity.this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                    } else {
                        mAccountUpdatePresenter.changePassword(idAccount, oldPass, newPass);
                    }
                }
            }
        });
    }

    @Override
    public void changeDatePicker(String dateStr) {
        etBirthday.setText(dateStr);
    }

    @Override
    public void update(int status) {
        switch (status) {
            case 0:
                Toast.makeText(AccountUpdateActivity.this, "Cập nhật tài khoản thất bại", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(AccountUpdateActivity.this, "Cập nhật tài khoản thành công", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case 2:
                Toast.makeText(AccountUpdateActivity.this, "Vui lòng kết nối mạng", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(AccountUpdateActivity.this, "Định dạng ngày sinh không đúng", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(AccountUpdateActivity.this, "Mật khẩu mới trùng với mật khẩu cũ", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(AccountUpdateActivity.this, "Cập nhật mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(AccountUpdateActivity.this, "Mật khẩu hiện tại không chính xác", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

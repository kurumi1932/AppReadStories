package huce.fit.appreadstories.account.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.account.register.AccountRegisterActivity;
import huce.fit.appreadstories.main.MainActivity;

public class AccountLoginActivity extends AppCompatActivity implements AccountLoginView {

    private EditText etUsername;
    private EditText etPassword;
    private Button btLogin, btCreate;
    private AccountPresenter mAccountPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_login);

        init();
        processEvents();
    }

    private void init() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
        btCreate = findViewById(R.id.btCreate);

        mAccountPresenter = new AccountLoginImpl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        etUsername.setText(mAccountPresenter.getSharedPreferences().getUsername());
    }

    private void processEvents() {
        btLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (username.length() == 0) {
                Toast.makeText(AccountLoginActivity.this, "Bạn chưa nhập tên tài khoản!", Toast.LENGTH_SHORT).show();
            } else if (password.length() == 0) {
                Toast.makeText(AccountLoginActivity.this, "Bạn chưa nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            } else {
                mAccountPresenter.login(username, password);
            }
        });
        btCreate.setOnClickListener(v -> {
            Intent intent = new Intent(AccountLoginActivity.this, AccountRegisterActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void login(int status) {
        switch (status) {
            case 0:
                Toast.makeText(AccountLoginActivity.this, "Tài khoản hoặc mật khẩu không chính xác!", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(AccountLoginActivity.this, "Đăng nhập thành công!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AccountLoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case 2:
                Toast.makeText(AccountLoginActivity.this, "Vui lòng kết nối mạng!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

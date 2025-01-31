package huce.fit.appreadstories.account.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R
import huce.fit.appreadstories.account.register.AccountRegisterActivity
import huce.fit.appreadstories.main.MainActivity
import huce.fit.appreadstories.util.AppUtil

class AccountLoginActivity : AppCompatActivity(), AccountLoginView {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var btCreate: Button
    private lateinit var accountPresenter: AccountLoginImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_login)
        init()
        processEvents()
    }

    private fun init() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btLogin = findViewById(R.id.btLogin)
        btCreate = findViewById(R.id.btCreate)
        accountPresenter = AccountLoginImpl(this)
    }

    override fun onResume() {
        super.onResume()
        etUsername.setText(accountPresenter.getAccount().getUsername())
    }

    private fun processEvents() {
        btLogin.setOnClickListener {
            val username = etUsername.getText().toString().trim { it <= ' ' }
            val password: String = etPassword.getText().toString().trim { it <= ' ' }
            if (username.isEmpty()) {
                AppUtil.setToast(this, "Bạn chưa nhập tên tài khoản!")
            } else if (password.isEmpty()) {
                AppUtil.setToast(this, "Bạn chưa nhập mật khẩu!")
            } else {
                accountPresenter.login(username, password)
            }
        }
        btCreate.setOnClickListener {
            val intent = Intent(this, AccountRegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun login(status: Int) {
        when (status) {
            0 -> AppUtil.setToast(this, "Tài khoản hoặc mật khẩu không chính xác!")

            1 -> {
                AppUtil.setToast(this, "Đăng nhập thành công!!")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            2 -> AppUtil.setToast(this, "Vui lòng kết nối mạng!")
        }
    }
}

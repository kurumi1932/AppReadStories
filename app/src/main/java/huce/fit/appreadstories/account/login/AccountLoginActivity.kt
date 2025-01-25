package huce.fit.appreadstories.account.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R
import huce.fit.appreadstories.account.register.AccountRegisterActivity
import huce.fit.appreadstories.main.MainActivity

class AccountLoginActivity : AppCompatActivity(), AccountLoginView {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var btCreate: Button
    private var mAccountPresenter: AccountPresenter = AccountLoginImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_login)
        init()
        processEvents()
    }

    fun init() {
        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        btLogin = findViewById(R.id.btLogin)
        btCreate = findViewById(R.id.btCreate)
    }

    override fun onResume() {
        super.onResume()
        etUsername.setText(mAccountPresenter.getAccount().getUsername())
    }

    fun processEvents() {
        btLogin.setOnClickListener {
            val username = etUsername.getText().toString().trim { it <= ' ' }
            val password: String = etPassword.getText().toString().trim { it <= ' ' }
            if (username.isEmpty()) {
                Toast.makeText(
                    this@AccountLoginActivity,
                    "Bạn chưa nhập tên tài khoản!",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password.isEmpty()) {
                Toast.makeText(
                    this@AccountLoginActivity,
                    "Bạn chưa nhập mật khẩu!",
                    Toast.LENGTH_SHORT
                )
                    .show()
            } else {
                mAccountPresenter.login(username, password)
            }
        }
        btCreate.setOnClickListener {
            val intent = Intent(
                this@AccountLoginActivity,
                AccountRegisterActivity::class.java
            )
            startActivity(intent)
        }
    }

    override fun login(status: Int) {
        when (status) {
            0 -> Toast.makeText(
                this@AccountLoginActivity,
                "Tài khoản hoặc mật khẩu không chính xác!",
                Toast.LENGTH_SHORT
            ).show()

            1 -> {
                Toast.makeText(
                    this@AccountLoginActivity,
                    "Đăng nhập thành công!!",
                    Toast.LENGTH_SHORT
                ).show()
                val intent = Intent(this@AccountLoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            2 -> Toast.makeText(
                this@AccountLoginActivity,
                "Vui lòng kết nối mạng!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

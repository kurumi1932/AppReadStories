package huce.fit.appreadstories.account.register

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R
import huce.fit.appreadstories.util.AppUtil

class AccountRegisterActivity : AppCompatActivity(), AccountRegisterView {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etUsername: EditText
    private lateinit var etPassword1: EditText
    private lateinit var etPassword2: EditText
    private lateinit var etBirthday: EditText
    private lateinit var ivDate: ImageView
    private lateinit var btExit: Button
    private lateinit var btRegister: Button
    private var accountRegisterPresenter = AccountRegisterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_register)
        init()
        processEvents()
    }

    private fun init() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etUsername = findViewById(R.id.etUsername)
        etPassword1 = findViewById(R.id.etPassword1)
        etPassword2 = findViewById(R.id.etPassword2)
        etBirthday = findViewById(R.id.etBirthday)
        ivDate = findViewById(R.id.ivDate)
        btExit = findViewById(R.id.btExit)
        btRegister = findViewById(R.id.btRegister)
    }

    private fun processEvents() {
        btExit.setOnClickListener { finish() }
        ivDate.setOnClickListener { accountRegisterPresenter.openDatePicker() }
        btRegister.setOnClickListener {
            val username: String = etUsername.getText().toString().trim { it <= ' ' }
            val password1: String = etPassword1.getText().toString().trim { it <= ' ' }
            val password2: String = etPassword2.getText().toString().trim { it <= ' ' }
            val email: String = etEmail.getText().toString().trim { it <= ' ' }
            val name = etName.getText().toString().trim { it <= ' ' }
            val birthday: String = etBirthday.getText().toString().trim { it <= ' ' }
            if (password1 == password2) {
                if (username.isEmpty() || password1.isEmpty() || email.isEmpty() || name.isEmpty() || birthday.isEmpty()) {
                    AppUtil.setToast(this, "Vui lòng điền đầy đủ thông tin!")
                } else {
                    accountRegisterPresenter.register(username, password1, email, name, birthday)
                }
            } else {
                AppUtil.setToast(this, "Mật khẩu nhập lại không khớp!")
            }
        }
    }

    override fun changeDatePicker(birthday: String) {
        etBirthday.setText(birthday)
    }

    override fun register(status: Int) {
        when (status) {
            0 -> AppUtil.setToast(this, "Tài khoản đã tồn tại")

            1 -> {
                AppUtil.setToast(this, "Đăng ký thành công")
                finish()
            }

            2 -> AppUtil.setToast(this, "Vui lòng kết nối mạng")

            3 -> AppUtil.setToast(this, "Định dạng ngày sinh không đúng")
        }
    }
}

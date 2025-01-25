package huce.fit.appreadstories.account.register

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R

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
    private var mAccountRegisterPresenter: AccountRegisterPresenter = AccountRegisterImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_register)
        init()
        processEvents()
    }

    fun init() {
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

    fun processEvents() {
        btExit.setOnClickListener { finish() }
        ivDate.setOnClickListener { mAccountRegisterPresenter.openDatePicker() }
        btRegister.setOnClickListener {
            val username: String = etUsername.getText().toString().trim { it <= ' ' }
            val password1: String = etPassword1.getText().toString().trim { it <= ' ' }
            val password2: String = etPassword2.getText().toString().trim { it <= ' ' }
            val email: String = etEmail.getText().toString().trim { it <= ' ' }
            val name = etName.getText().toString().trim { it <= ' ' }
            val birthday: String = etBirthday.getText().toString().trim { it <= ' ' }
            if (password1 == password2) {
                if (username.isEmpty() || password1.isEmpty() || email.isEmpty() || name.isEmpty() || birthday.isEmpty()) {
                    Toast.makeText(
                        this@AccountRegisterActivity,
                        "Vui lòng điền đầy đủ thông tin!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    mAccountRegisterPresenter.register(username, password1, email, name, birthday)
                }
            } else {
                Toast.makeText(
                    this@AccountRegisterActivity,
                    "Mật khẩu nhập lại không khớp!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun changeDatePicker(birthday: String) {
        etBirthday.setText(birthday)
    }

    override fun register(status: Int) {
        when (status) {
            0 -> Toast.makeText(
                this@AccountRegisterActivity,
                "Tài khoản đã tồn tại",
                Toast.LENGTH_SHORT
            ).show()

            1 -> {
                Toast.makeText(
                    this@AccountRegisterActivity,
                    "Đăng ký thành công",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            2 -> Toast.makeText(
                this@AccountRegisterActivity,
                "Vui lòng kết nối mạng",
                Toast.LENGTH_SHORT
            ).show()

            3 -> Toast.makeText(
                this@AccountRegisterActivity,
                "Định dạng ngày sinh không đúng",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

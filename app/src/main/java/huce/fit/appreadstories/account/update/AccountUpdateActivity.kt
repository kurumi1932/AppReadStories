package huce.fit.appreadstories.account.update

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R

class AccountUpdateActivity : AppCompatActivity(), AccountUpdateView {

    private lateinit var ivBack: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etBirthday: EditText
    private lateinit var ivDate: ImageView
    private lateinit var btSave: Button
    private var mAccountUpdatePresenter: AccountUpdatePresenter= AccountUpdateImpl(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_update)
        init()
        processEvents()
    }

    fun init() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etOldPassword = findViewById(R.id.etPassword1)
        etNewPassword = findViewById(R.id.etPassword2)
        etBirthday = findViewById(R.id.etBirthday)
        ivBack = findViewById(R.id.ivBack)
        ivDate = findViewById(R.id.ivDate)
        btSave = findViewById(R.id.btSave)
    }

    override fun getInfoAccount(name: String, email: String, birthday: String) {
        etName.setText(name)
        etEmail.setText(email)
        etBirthday.setText(birthday)
    }

    fun processEvents() {
        ivBack.setOnClickListener { finish() }
        ivDate.setOnClickListener { mAccountUpdatePresenter.openDatePicker() }
        btSave.setOnClickListener {
            val name = etName.getText().toString().trim { it <= ' ' }
            val email: String = etEmail.getText().toString().trim { it <= ' ' }
            val oldPassword: String = etOldPassword.getText().toString().trim { it <= ' ' }
            val newPassword: String = etNewPassword.getText().toString().trim { it <= ' ' }
            val birthday: String = etBirthday.getText().toString().trim { it <= ' ' }
            if (name.isEmpty() || email.isEmpty() || birthday.isEmpty()) {
                Toast.makeText(
                    this@AccountUpdateActivity,
                    "Vui lòng nhập đầy đủ tên hiển thị!",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                val accountId = mAccountUpdatePresenter.getAccount().getAccountId()
                if (oldPassword.isEmpty()) {
                    if (newPassword.isEmpty()) {
                        mAccountUpdatePresenter.updateAccount(accountId, email, name, birthday)
                    } else {
                        Toast.makeText(
                            this@AccountUpdateActivity,
                            "Vui lòng nhập mật khẩu hiện tại",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    if (newPassword.isEmpty()) {
                        Toast.makeText(
                            this@AccountUpdateActivity,
                            "Vui lòng nhập mật khẩu mới",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        mAccountUpdatePresenter.changePassword(
                            accountId,
                            oldPassword,
                            newPassword
                        )
                    }
                }
            }
        }
    }

    override fun changeDatePicker(dateStr: String) {
        etBirthday.setText(dateStr)
    }

    override fun update(status: Int) {
        when (status) {
            0 -> Toast.makeText(
                this@AccountUpdateActivity,
                "Cập nhật tài khoản thất bại",
                Toast.LENGTH_SHORT
            ).show()

            1 -> {
                Toast.makeText(
                    this@AccountUpdateActivity,
                    "Cập nhật tài khoản thành công",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }

            2 -> Toast.makeText(
                this@AccountUpdateActivity,
                "Vui lòng kết nối mạng",
                Toast.LENGTH_SHORT
            ).show()

            3 -> Toast.makeText(
                this@AccountUpdateActivity,
                "Định dạng ngày sinh không đúng",
                Toast.LENGTH_SHORT
            ).show()

            4 -> Toast.makeText(
                this@AccountUpdateActivity,
                "Mật khẩu mới trùng với mật khẩu cũ",
                Toast.LENGTH_SHORT
            ).show()

            5 -> Toast.makeText(
                this@AccountUpdateActivity,
                "Cập nhật mật khẩu thất bại",
                Toast.LENGTH_SHORT
            ).show()

            6 -> Toast.makeText(
                this@AccountUpdateActivity,
                "Mật khẩu hiện tại không chính xác",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

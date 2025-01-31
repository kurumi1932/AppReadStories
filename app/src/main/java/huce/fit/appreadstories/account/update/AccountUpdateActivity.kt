package huce.fit.appreadstories.account.update

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R
import huce.fit.appreadstories.util.AppUtil

class AccountUpdateActivity : AppCompatActivity(), AccountUpdateView {

    private lateinit var ivBack: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etOldPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etBirthday: EditText
    private lateinit var ivDate: ImageView
    private lateinit var btSave: Button
    private lateinit var accountUpdatePresenter: AccountUpdateImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_update)
        init()
        processEvents()
    }

    private fun init() {
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etOldPassword = findViewById(R.id.etPassword1)
        etNewPassword = findViewById(R.id.etPassword2)
        etBirthday = findViewById(R.id.etBirthday)
        ivBack = findViewById(R.id.ivBack)
        ivDate = findViewById(R.id.ivDate)
        btSave = findViewById(R.id.btSave)
        accountUpdatePresenter = AccountUpdateImpl(this)
    }

    override fun getInfoAccount(name: String, email: String, birthday: String) {
        etName.setText(name)
        etEmail.setText(email)
        etBirthday.setText(birthday)
    }

    private fun processEvents() {
        ivBack.setOnClickListener { finish() }
        ivDate.setOnClickListener { accountUpdatePresenter.openDatePicker() }
        btSave.setOnClickListener {
            val name = etName.getText().toString().trim { it <= ' ' }
            val email: String = etEmail.getText().toString().trim { it <= ' ' }
            val oldPassword: String = etOldPassword.getText().toString().trim { it <= ' ' }
            val newPassword: String = etNewPassword.getText().toString().trim { it <= ' ' }
            val birthday: String = etBirthday.getText().toString().trim { it <= ' ' }
            if (name.isEmpty() || email.isEmpty() || birthday.isEmpty()) {
                AppUtil.setToast(this, "Vui lòng nhập đầy đủ tên hiển thị!")
            } else {
                val accountId = accountUpdatePresenter.getAccount().getAccountId()
                if (oldPassword.isEmpty()) {
                    if (newPassword.isEmpty()) {
                        accountUpdatePresenter.updateAccount(accountId, email, name, birthday)
                    } else {
                        AppUtil.setToast(this, "Vui lòng nhập mật khẩu hiện tại")
                    }
                } else {
                    if (newPassword.isEmpty()) {
                        AppUtil.setToast(this, "Vui lòng nhập mật khẩu mới")
                    } else {
                        accountUpdatePresenter.changePassword(accountId, oldPassword, newPassword)
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
            0 -> setToast("Cập nhật tài khoản thất bại")

            1 -> {
                setToast("Cập nhật tài khoản thành công")
                finish()
            }

            2 -> setToast("Vui lòng kết nối mạng")

            3 -> setToast("Định dạng ngày sinh không đúng")

            4 -> setToast("Mật khẩu mới trùng với mật khẩu cũ")

            5 -> setToast("Cập nhật mật khẩu thất bại")

            6 -> setToast("Mật khẩu hiện tại không chính xác")
        }
    }

    private fun setToast(content: String) {
        AppUtil.setToast(this, content)
    }
}

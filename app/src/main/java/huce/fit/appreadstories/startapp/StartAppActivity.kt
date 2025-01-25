package huce.fit.appreadstories.startapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import huce.fit.appreadstories.R
import huce.fit.appreadstories.account.login.AccountLoginActivity
import huce.fit.appreadstories.main.MainActivity

class StartAppActivity: AppCompatActivity(), StartAppView {

    private lateinit var mStartAppPresenter: StartAppPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_app)
        init()
        startApp()
    }

    fun init() {
        mStartAppPresenter = StartAppImpl(this)
    }

    fun startApp() {
        //delay
        if (mStartAppPresenter.checkLogged()) {
             Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        } else {
            Handler().postDelayed({
                val intent = Intent(this, AccountLoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }
}

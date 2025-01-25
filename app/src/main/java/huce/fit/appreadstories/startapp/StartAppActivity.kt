package huce.fit.appreadstories.startapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.account.login.AccountLoginActivity;
import huce.fit.appreadstories.main.MainActivity;

public class StartAppActivity extends AppCompatActivity implements StartAppView {

    private StartAppPresenter mStartAppPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        init();
        startApp();
    }

    private void init(){
        mStartAppPresenter = new StartAppImpl(this);
    }

    private void startApp() {
        //delay
        Handler handler = new Handler();
        if (mStartAppPresenter.checkLogged()) {
            handler.postDelayed(() -> {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }, 3000);
        } else {
            handler.postDelayed(() -> {
                Intent intent = new Intent(this, AccountLoginActivity.class);
                startActivity(intent);
                finish();
            }, 3000);
        }
    }
}

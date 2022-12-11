package huce.fit.appreadstories.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import huce.fit.appreadstories.R;

public class StartAppActivity extends AppCompatActivity {
    @SuppressLint("SimpleDateFormat")
    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final Date currentDate = new Date();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_app);

        getSharedPreferences();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        int idAccount = sharedPreferences.getInt("idAccount", 0);
        String startDate = sharedPreferences.getString("birthDay", "1000-1-1");

        Handler handler = new Handler();

        if (idAccount != 0) {
            try {
                setSharedPreferences(age(startDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

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

    private void setSharedPreferences(int age) {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        SharedPreferences.Editor myedit = sharedPreferences.edit();

        myedit.putInt("age", age);
        myedit.apply();
    }

    private int age(String startDate) throws ParseException {
        String endDate = simpleDateFormat.format(currentDate);
        Date date1 = simpleDateFormat.parse(startDate);
        Date date2 = simpleDateFormat.parse(endDate);
        long getDiff = 0;
        if (date1 != null && date2 != null) {
            getDiff = date2.getTime() - date1.getTime();
            Log.e("date1", date1.toString());
            Log.e("date2", date2.toString());
        }
        Log.e("getDiff", String.valueOf(getDiff));

        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);//24h*60p*60s*1000
        int age = (int) getDaysDiff / 365;
        Log.e("getDaysDiff", String.valueOf(getDaysDiff));
        Log.e("age", String.valueOf(age));
        return age;
    }
}

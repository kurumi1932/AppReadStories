package huce.fit.appreadstories.account;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.shared_preferences.MySharedPreferences;
import huce.fit.appreadstories.shared_preferences.MySharedPreferencesImpl;

public class BaseAccountImpl implements BaseAccountPresenter {

    private static final String TAG = "BaseAccountImpl";
    private final MySharedPreferences mMySharedPreferences;
    private final CheckNetwork mCheckNetwork;

    @SuppressLint("SimpleDateFormat")
    private final DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    protected BaseAccountImpl(Context context) {
        mCheckNetwork = new CheckNetwork(context);
        mMySharedPreferences = new MySharedPreferencesImpl(context);
    }

    @Override
    public boolean isNetwork() {
        return mCheckNetwork.isNetwork();
    }

    @Override
    public MySharedPreferences getSharedPreferences(){
        Log.e(TAG +"1", "getSharedPreferences");
        mMySharedPreferences.getMySharedPreferences("CheckLogin", MODE_PRIVATE);
        Log.e(TAG +"1", "birthday= "+ mMySharedPreferences.getBirthday());
        return mMySharedPreferences;
    }

    @Override
    public MySharedPreferences setSharedPreferences(){
        Log.e(TAG, "getSharedPreferences");
        mMySharedPreferences.setMySharedPreferences("CheckLogin", MODE_PRIVATE);
        return mMySharedPreferences;
    }

    @Override
    public int age(String birthday) {
        String endDate = simpleDateFormat.format(new Date());
        Log.e(TAG, "birthday= "+birthday);
        Date date1, date2;
        try {
            date1 = simpleDateFormat.parse(birthday);
            Log.e(TAG, "date1= "+date1);
            date2 = simpleDateFormat.parse(endDate);
            Log.e(TAG, "date2= "+date2);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        long getDiff = 0;
        if (date1 != null && date2 != null) {
            getDiff = date2.getTime() - date1.getTime();
            Log.e(TAG, "date1= "+ date1);
            Log.e(TAG, "date2= "+ date2);
        }
        Log.e(TAG, "getDiff"+ getDiff);

        long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);//24h*60p*60s*1000
        Log.e(TAG,"getDaysDiff= "+ getDaysDiff);
        Log.e(TAG, "age= "+ (int) getDaysDiff / 365);

        return (int) getDaysDiff / 365;
    }

    protected boolean checkDate(String dateStr) {
        String[] dateArr = dateStr.split("-");//tách từng giá trị vào mảng khi gặp -
        if (dateArr[0].length() > 4)
            return false;
        int year = Integer.parseInt(dateArr[0]);
        if (year < 9999 && year > 1900) //năm ở vị trí 0 phải nhỏ hơn 9999 và lớn hơn 1900
            return isValid(dateStr);
        else
            return false;
    }

    private boolean isValid(String dateStr) {
        @SuppressLint("SimpleDateFormat") DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);//convert date nếu sai thì chạy catch
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}

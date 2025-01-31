package huce.fit.appreadstories.dialog.setting.color;

import android.content.Context;
import android.util.Log;

import huce.fit.appreadstories.chapter.information.ChapterInformationView;
import huce.fit.appreadstories.shared_preferences.SettingSharedPreferences;

public class ColorDialogImpl implements ColorDialogPresenter {

    private final ChapterInformationView mChapterInformationView;
    private final ColorDialogView mColorDialogView;
    private final SettingSharedPreferences mSettingSharedPreferences;
    private String mTextColor, mBackgroundColor;

    public ColorDialogImpl(ChapterInformationView chapterInformationView, ColorDialogView colorDialogView) {
        mChapterInformationView =chapterInformationView;
        mColorDialogView = colorDialogView;
        mSettingSharedPreferences = new SettingSharedPreferences((Context) chapterInformationView);

        getSetting();
        mTextColor = mSettingSharedPreferences.getTextColor();
        mBackgroundColor = mSettingSharedPreferences.getBackgroundColor();
        setSetting();

        mColorDialogView.setTextColor(mTextColor);
        mColorDialogView.setBackgroundColor(mBackgroundColor);
    }

    private void getSetting() {
        mSettingSharedPreferences.getSharedPreferences("Setting", Context.MODE_PRIVATE);
    }

    private void setSetting() {
        mSettingSharedPreferences.setSharedPreferences("Setting", Context.MODE_PRIVATE);
    }

    @Override
    public void setTextColorPickerDialog() {
        mColorDialogView.setColorPickerDialog(mBackgroundColor);
    }

    @Override
    public void setBackgroundColorPickerDialog() {
        mColorDialogView.setColorPickerDialog(mBackgroundColor);
    }

    @Override
    public void viewTextColor(String hexVal) {
        mTextColor = "#" + hexVal.substring(3);
        Log.e("NHT hexVal: ", hexVal);
        Log.e("NHT hexColor: ", mTextColor);
        mColorDialogView.setTextColor(mTextColor);

        mSettingSharedPreferences.setTextColor(mTextColor);
        mSettingSharedPreferences.myApply();
    }

    @Override
    public void viewBackgroundColor(String hexVal) {
        mBackgroundColor = "#" + hexVal.substring(3);
        Log.e("NHT hexVal: ", hexVal);
        Log.e("NHT hexColor: ", mBackgroundColor);
        mColorDialogView.setBackgroundColor(mBackgroundColor);

        mSettingSharedPreferences.setBackgroundColor(mBackgroundColor);
        mSettingSharedPreferences.myApply();
    }

    @Override
    public void setTextColor() {
        mChapterInformationView.setTextColor(mTextColor);
    }

    @Override
    public void setBackgroundColor() {
        mChapterInformationView.setBackgroundColor(mBackgroundColor);
    }
}

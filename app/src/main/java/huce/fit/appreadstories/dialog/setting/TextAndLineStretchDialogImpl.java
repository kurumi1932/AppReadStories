package huce.fit.appreadstories.dialog.setting;

import android.content.Context;
import android.util.Log;

import java.util.Locale;

import huce.fit.appreadstories.chapter.information.ChapterInformationView;
import huce.fit.appreadstories.shared_preferences.SettingSharedPreferences;

public class TextAndLineStretchDialogImpl implements TextAndLineStretchDialogPresenter {

    private final ChapterInformationView mChapterInformationView;
    private final TextAndLineStretchDialogView mTextAndLineStretchDialogView;
    private final SettingSharedPreferences mSettingSharedPreferences;
    private int mTextSize, mLineStretch;
    private String mBackgroundColor, mTextColor;

    public TextAndLineStretchDialogImpl(ChapterInformationView chapterInformationView, TextAndLineStretchDialogView textAndLineStretchDialogView) {
        mChapterInformationView = chapterInformationView;
        mTextAndLineStretchDialogView = textAndLineStretchDialogView;
        mSettingSharedPreferences = new SettingSharedPreferences((Context) chapterInformationView);
        getData();
    }

    private void getSetting() {
        mSettingSharedPreferences.getSharedPreferences("Setting", Context.MODE_PRIVATE);
    }

    private void setSetting() {
        mSettingSharedPreferences.setSharedPreferences("Setting", Context.MODE_PRIVATE);
    }

    private void getData() {
        getSetting();
        mTextSize = mSettingSharedPreferences.getTextSize();
        mLineStretch = mSettingSharedPreferences.getLineStretch();
        mTextAndLineStretchDialogView.getData(String.valueOf(mTextSize), String.format(Locale.getDefault(), "%d%%", mLineStretch));
    }

    @Override
    public void upTextSize() {
        if (mTextSize > 14 && mTextSize < 40) {
            ++mTextSize;
            Log.e("NHT textSize up: ", String.valueOf(mTextSize));
            setTextSize();
        }
    }

    @Override
    public void downTextSize() {
        if (mTextSize > 15 && mTextSize < 41) {
            --mTextSize;
            Log.e("NHT textSize down: ", String.valueOf(mTextSize));
            setTextSize();
        }
    }

    private void setTextSize() {
        mChapterInformationView.setTextSize(mTextSize);
        mTextAndLineStretchDialogView.setTextSize(String.valueOf(mTextSize));

        setSetting();
        mSettingSharedPreferences.setTextSize(mTextSize);
        mSettingSharedPreferences.myApply();
    }

    @Override
    public void upLineStretch() {
        if (mLineStretch > 65 && mLineStretch < 300) {
            mLineStretch += 5;
            setLineStretch(mLineStretch);
        }
    }

    @Override
    public void downLineStretch() {
        if (mLineStretch > 70 && mLineStretch < 305) {
            mLineStretch -= 5;
            setLineStretch(mLineStretch);
        }
    }

    private void setLineStretch(int lineStretch) {
        String lineStretchStr = String.format(Locale.getDefault(), "%d%%", mLineStretch);
        mTextAndLineStretchDialogView.setLineStretch(lineStretchStr);

        float lineStretchFloat = Float.parseFloat(String.valueOf(lineStretch)) / 100;
        mChapterInformationView.setLineStretch(lineStretchFloat);
        setSetting();
        mSettingSharedPreferences.setLineStretch(mLineStretch);
        mSettingSharedPreferences.myApply();
    }

    @Override
    public void setColor(int backgroundColor, int textColor) {
        if (backgroundColor != 0) {
            mBackgroundColor = "#" + Integer.toHexString(backgroundColor);
        }
        if (textColor != 0) {
            mTextColor = "#" + Integer.toHexString(textColor);
        }

        mChapterInformationView.setTextColor(mTextColor);
        mChapterInformationView.setBackgroundColor(mBackgroundColor);
        setSetting();
        mSettingSharedPreferences.setBackgroundColor(mBackgroundColor);
        mSettingSharedPreferences.setTextColor(mTextColor);
        mSettingSharedPreferences.myApply();
    }
}

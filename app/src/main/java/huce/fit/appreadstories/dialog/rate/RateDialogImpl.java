package huce.fit.appreadstories.dialog.rate;

import android.content.Context;

import huce.fit.appreadstories.model.Rate;
import huce.fit.appreadstories.shared_preferences.RateSharedPreferences;
import huce.fit.appreadstories.story.information.StoryInformationView;

public class RateDialogImpl implements RateDialogPresenter {

    private final StoryInformationView mStoryInformationView;
    private final RateDialogView mRateDialogView;
    private final Context mContext;
    private Rate mRate;
    private int mRatePoint;

    RateDialogImpl(StoryInformationView storyInformationView, RateDialogView rateDialogView) {
        mStoryInformationView =  storyInformationView;
        mRateDialogView = rateDialogView;
        mContext = (Context) storyInformationView;
    }

    @Override
    public Rate getRate() {
        RateSharedPreferences rate = new RateSharedPreferences(mContext);
        rate.getSharedPreferences("Rate", Context.MODE_PRIVATE);
        mRate = new Rate(rate.getRateId(), rate.getRatePoint(), rate.getRate(), rate.getSuccess());
        return mRate;
    }

    @Override
    public void setRatePoint(int ratePoint) {
        mRatePoint = ratePoint;
    }

    @Override
    public void rate(String rate) {
        if (mRate.getRatePoint() > 0) {//update rate
            mRateDialogView.openConfirmDiaLog(mRatePoint,rate);
        } else {//add rate
            if (mRatePoint > 0) {
                mStoryInformationView.addRate(mRatePoint, rate);
                mRateDialogView.dismiss();
            } else {
                mRateDialogView.noRatePoint();
            }
        }
    }


}

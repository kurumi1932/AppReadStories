package huce.fit.appreadstories.dialog.confirm;

import android.content.Context;

import huce.fit.appreadstories.dialog.rate.RateDialog;
import huce.fit.appreadstories.model.Rate;
import huce.fit.appreadstories.shared_preferences.RateSharedPreferences;
import huce.fit.appreadstories.story.information.StoryInformationView;

public class ConfirmEditRateDialog extends BaseConfirmDialog {

    private int mRatePoint;
    private String mRate;

    public ConfirmEditRateDialog(StoryInformationView storyInformationView, Context context) {
        super(context);

        init();
        processEvents(storyInformationView);
    }

    private void init() {
        String title = "Sửa đánh giá";
        String content = "Bạn có muốn thay đổi đánh giá không?";
        setContent(title, content);
    }

    public void setData(int ratePoint, String rate) {
        mRatePoint = ratePoint;
        mRate = rate;
    }

    private Rate getRate() {
        RateSharedPreferences rate = new RateSharedPreferences(mContext);
        rate.getSharedPreferences("Rate", Context.MODE_PRIVATE);
        return new Rate(rate.getRateId(), mRatePoint, mRate, rate.getSuccess());
    }

    private void processEvents(StoryInformationView storyInformationView) {
        tvYes.setOnClickListener(view -> {
            storyInformationView.updateRate(mRatePoint, mRate);
            dismiss();
        });
        tvNo.setOnClickListener(view -> {
            RateDialog rateDialog = new RateDialog(storyInformationView, mContext);
            rateDialog.setData(getRate());
            rateDialog.show();
            dismiss();
        });
    }
}

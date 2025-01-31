package huce.fit.appreadstories.dialog.rate;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.dialog.BaseDialog;
import huce.fit.appreadstories.dialog.confirm.ConfirmEditRateDialog;
import huce.fit.appreadstories.model.Rate;
import huce.fit.appreadstories.story.information.StoryInformationView;

public class RateDialog extends BaseDialog implements RateDialogView {

    private final StoryInformationView mStoryInformationView;
    private final RateDialogPresenter mRateDialogPresenter;
    private ImageView ivClose, ivRate1, ivRate2, ivRate3, ivRate4, ivRate5;
    private Button btRate, btDeleteRate;
    private EditText etRate;
    private TextView tvNumberTextRate;
    private static final int[] RATE = {R.id.ivRate1, R.id.ivRate2, R.id.ivRate3, R.id.ivRate4, R.id.ivRate5};

    public RateDialog(StoryInformationView storyInformationView, Context context) {
        super(context);
        mStoryInformationView = storyInformationView;
        mRateDialogPresenter = new RateDialogImpl(mStoryInformationView, this);
        setDialog(R.layout.dialog_rate);
        setWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
        init();
        setData(mRateDialogPresenter.getRate());
        processEvents();
    }

    private void init() {
        ivClose = mDialog.findViewById(R.id.ivClose);
        ivRate1 = mDialog.findViewById(R.id.ivRate1);
        ivRate2 = mDialog.findViewById(R.id.ivRate2);
        ivRate3 = mDialog.findViewById(R.id.ivRate3);
        ivRate4 = mDialog.findViewById(R.id.ivRate4);
        ivRate5 = mDialog.findViewById(R.id.ivRate5);
        etRate = mDialog.findViewById(R.id.etRate);
        tvNumberTextRate = mDialog.findViewById(R.id.tvNumberTextRate);
        btRate = mDialog.findViewById(R.id.btRate);
        btDeleteRate = mDialog.findViewById(R.id.btDeleteRate);
    }

    @Override
    public void openConfirmDiaLog(int ratePoint, String rate) {
        dismiss();
        ConfirmEditRateDialog confirmEditRateDialog = new ConfirmEditRateDialog(mStoryInformationView, mContext);
        confirmEditRateDialog.setData(ratePoint, rate);
        confirmEditRateDialog.show();
    }

    @Override
    public void setData(Rate rate) {
        if (rate.getSuccess() == 1) {
            setRatePoint(rate.getRatePoint());
            etRate.setText(rate.getRate());
            tvNumberTextRate.setText(String.format(Locale.getDefault(), "%d/400", rate.getRate().length()));
            btDeleteRate.setVisibility(View.VISIBLE);
        } else {
            btDeleteRate.setVisibility(View.GONE);
        }
    }

    private void processEvents() {
        ivClose.setOnClickListener(view -> mDialog.dismiss());
        ivRate1.setOnClickListener(view -> {
            setRatePoint(1);
        });
        ivRate2.setOnClickListener(view -> {
            setRatePoint(2);
        });
        ivRate3.setOnClickListener(view -> {
            setRatePoint(3);
        });
        ivRate4.setOnClickListener(view -> {
            setRatePoint(4);
        });
        ivRate5.setOnClickListener(view -> {
            setRatePoint(5);
        });

        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etRate.getLineCount() > 7) {
                    Toast.makeText(mContext, "Vượt quá sô dòng giới hạn đánh giá!", Toast.LENGTH_SHORT).show();
                    etRate.getText().delete(etRate.getText().length() - 1, etRate.getText().length());
                }
                tvNumberTextRate.setText(String.format(Locale.getDefault(), "%d/400", s.toString().length()));
            }
        });

        btDeleteRate.setOnClickListener(view -> {//delete rate
            mStoryInformationView.deleteRate();
            dismiss();
        });

        btRate.setOnClickListener(view -> {
            mRateDialogPresenter.rate(etRate.getText().toString().trim());
        });
    }

    @Override
    public void noRatePoint(){
        Toast.makeText(mContext, "Vui lòng cho điểm dánh giá!", Toast.LENGTH_SHORT).show();
    }

    private void setRatePoint(int ratePoint) {
        mRateDialogPresenter.setRatePoint(ratePoint);
        for (int i = 0; i < ratePoint; i++) {
            ImageView ivRate = mDialog.findViewById(RATE[i]);
            ivRate.setImageResource(R.drawable.ic_rate);
        }
        for (int i = ratePoint; i < 5; i++) {
            ImageView ivRate = mDialog.findViewById(RATE[i]);
            ivRate.setImageResource(R.drawable.ic_not_rate);
        }
    }
}

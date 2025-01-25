package huce.fit.appreadstories.dialog.setting;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.chapter.information.ChapterInformationView;
import huce.fit.appreadstories.dialog.BaseDialog;
import huce.fit.appreadstories.dialog.setting.color.ColorDialog;

public class TextAndLineStretchDialog extends BaseDialog implements TextAndLineStretchDialogView {

    private final ChapterInformationView mChapterInformationView;
    private TextAndLineStretchDialogPresenter mTextAndLineStretchDialogPresenter;
    private ImageView ivSettingColor, ivTextSizeDown, ivTextSizeUp, ivLineStretchDown, ivLineStretchUp;
    private TextView tvTextSize, tvLineStretch, tvColor1, tvColor2, tvColor3, tvColor4, tvColor5, tvColor6, tvColor7;
    private static final int[] backgroundColor = {R.color.circle1, R.color.circle2, R.color.circle3,R.color.circle4,R.color.circle5,R.color.circle6,R.color.circle7};
    private static final int[] textColor = {R.color.black, R.color.white, R.color.medium_sea_green};
    public TextAndLineStretchDialog(ChapterInformationView chapterInformationView, Context context) {
        super(context);
        mChapterInformationView = chapterInformationView;
        setDialog(R.layout.dialog_setting);
        setWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0);
        init();
        processEvent();
    }

    private void init() {
        ivSettingColor = mDialog.findViewById(R.id.ivSettingColor);
        ivTextSizeDown = mDialog.findViewById(R.id.ivTextSizeDown);
        ivTextSizeUp = mDialog.findViewById(R.id.ivTextSizeUp);
        ivLineStretchDown = mDialog.findViewById(R.id.ivLineStretchDown);
        ivLineStretchUp = mDialog.findViewById(R.id.ivLineStretchUp);
        tvTextSize = mDialog.findViewById(R.id.tvTextSize);
        tvLineStretch = mDialog.findViewById(R.id.tvLineStretch);

        tvColor1 = mDialog.findViewById(R.id.tvColor1);
        tvColor2 = mDialog.findViewById(R.id.tvColor2);
        tvColor3 = mDialog.findViewById(R.id.tvColor3);
        tvColor4 = mDialog.findViewById(R.id.tvColor4);
        tvColor5 = mDialog.findViewById(R.id.tvColor5);
        tvColor6 = mDialog.findViewById(R.id.tvColor6);
        tvColor7 = mDialog.findViewById(R.id.tvColor7);

        mTextAndLineStretchDialogPresenter = new TextAndLineStretchDialogImpl(mChapterInformationView,this);
    }

    @Override
    public void getData(String fontSize, String lineStretch){
        Log.e("dialog","NHT fontSize: "+fontSize);
        tvTextSize.setText(fontSize);
        tvLineStretch.setText(lineStretch);
    }

    private void processEvent() {
        ivSettingColor.setOnClickListener(view -> {
            dismiss();
            ColorDialog colorDialog = new ColorDialog(mChapterInformationView, mContext);
            colorDialog.show();
        });

        ivTextSizeDown.setOnClickListener(view -> {
            mTextAndLineStretchDialogPresenter.downTextSize();
        });

        ivTextSizeUp.setOnClickListener(view -> {
            mTextAndLineStretchDialogPresenter.upTextSize();
        });

        ivLineStretchDown.setOnClickListener(view -> {
            mTextAndLineStretchDialogPresenter.downLineStretch();
        });

        ivLineStretchUp.setOnClickListener(view -> {
            mTextAndLineStretchDialogPresenter.upLineStretch();
        });

        tvColor1.setOnClickListener(view -> {
            setColor(0,0);
        });

        tvColor2.setOnClickListener(view -> {
            setColor(1,0);
        });

        tvColor3.setOnClickListener(view -> {
            setColor(2,0);
        });

        tvColor4.setOnClickListener(view -> {
            setColor(3,1);
        });

        tvColor5.setOnClickListener(view -> {
            setColor(4,2);
        });

        tvColor6.setOnClickListener(view -> {
            setColor(5,1);
        });

        tvColor7.setOnClickListener(view -> {
            setColor(6,2);
        });
    }

    private void setColor(int indexBackgroundColor, int indexTextColor) {
        mTextAndLineStretchDialogPresenter.setColor(mContext.getResources().getColor(backgroundColor[indexBackgroundColor]),
                mContext.getResources().getColor(textColor[indexTextColor]));
    }

    @Override
    public void setTextSize(String textSize) {
        tvTextSize.setText(textSize);
    }

    @Override
    public void setLineStretch(String lineStretch) {
        tvLineStretch.setText(lineStretch);
    }
}

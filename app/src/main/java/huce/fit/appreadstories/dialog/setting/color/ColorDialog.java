package huce.fit.appreadstories.dialog.setting.color;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.chapter.information.ChapterInformationView;
import huce.fit.appreadstories.dialog.BaseDialog;
import huce.fit.appreadstories.dialog.setting.TextAndLineStretchDialog;

public class ColorDialog extends BaseDialog implements ColorDialogView {

    private final ChapterInformationView mChapterInformationView;
    private ColorDialogPresenter mColorDialogPresenter;
    ImageView ivBack, ivTextColor, ivBackgroundColor;
    TextView tvUseTextColor, tvUseBackgroundColor;
//    ColorPickerDialog mColorPickerDialog;

    public ColorDialog(ChapterInformationView chapterInformationView, Context context) {
        super(context);
        mChapterInformationView = chapterInformationView;
        setDialog(R.layout.dialog_setting_color);
        setWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, R.style.DialogSetting);
        init();
        processEvent();
    }

    private void init() {
        ivBack = mDialog.findViewById(R.id.ivBack);
        ivTextColor = mDialog.findViewById(R.id.ivTextColor);
        ivBackgroundColor = mDialog.findViewById(R.id.ivBackgroundColor);
        tvUseTextColor = mDialog.findViewById(R.id.tvUseTextColor);
        tvUseBackgroundColor = mDialog.findViewById(R.id.tvUseBackgroundColor);

//        mColorPickerDialog = ColorPickerDialog.createColorPickerDialog(mContext);
        mColorDialogPresenter = new ColorDialogImpl(mChapterInformationView, this);
    }

    @Override
    public void setTextColor(String color) {
        ivTextColor.setBackgroundColor(Color.parseColor(color));
    }

    @Override
    public void setBackgroundColor(String color) {
        ivBackgroundColor.setBackgroundColor(Color.parseColor(color));
    }

    private void processEvent() {
        ivBack.setOnClickListener(view -> {
            TextAndLineStretchDialog textAndLineStretchDialog = new TextAndLineStretchDialog(mChapterInformationView, mContext);
            textAndLineStretchDialog.show();
            dismiss();
        });

        ivTextColor.setOnClickListener(view -> {
//            mColorPickerDialog.setOnColorPickedListener((color, hexVal) -> mColorDialogPresenter.viewTextColor(hexVal));
            mColorDialogPresenter.setTextColorPickerDialog();
        });

        ivBackgroundColor.setOnClickListener(view -> {
//            mColorPickerDialog.setOnColorPickedListener((color, hexVal) -> mColorDialogPresenter.viewBackgroundColor(hexVal));
            mColorDialogPresenter.setBackgroundColorPickerDialog();
        });

        tvUseTextColor.setOnClickListener(view -> mColorDialogPresenter.setTextColor());
        tvUseBackgroundColor.setOnClickListener(view -> mColorDialogPresenter.setBackgroundColor());
    }

    @Override
    public void setColorPickerDialog(String color) {
//        mColorPickerDialog.setHexaDecimalTextColor(mContext.getResources().getColor(R.color.black));
//        mColorPickerDialog.setInitialColor(Color.parseColor(color));
//        mColorPickerDialog.setLastColor(color);
//        mColorPickerDialog.show();
    }
}

package huce.fit.appreadstories.dialog.setting.color

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.chapter.information.ChapterInformationView
import huce.fit.appreadstories.dialog.BaseDialog
import huce.fit.appreadstories.dialog.setting.TextAndLineStretchDialog

class ColorDialog(private val chapterInformationView: ChapterInformationView) :
    BaseDialog(chapterInformationView as Context), ColorDialogView {
   
    private lateinit var ivBack: ImageView
    private lateinit var ivTextColor: ImageView
    private lateinit var ivBackgroundColor: ImageView
    private lateinit var tvUseTextColor: TextView
    private lateinit var tvUseBackgroundColor: TextView
    private lateinit var colorDialogPresenter: ColorDialogPresenter

    //    ColorPickerDialog mColorPickerDialog;
    init{
        setDialog(R.layout.dialog_setting_color)
        setWindow(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM, R.style.DialogSetting
        )
        init()
        processEvent()
    }

    private fun init() {
        ivBack = dialog.findViewById(R.id.ivBack)
        ivTextColor = dialog.findViewById(R.id.ivTextColor)
        ivBackgroundColor = dialog.findViewById(R.id.ivBackgroundColor)
        tvUseTextColor = dialog.findViewById(R.id.tvUseTextColor)
        tvUseBackgroundColor = dialog.findViewById(R.id.tvUseBackgroundColor)

        //        mColorPickerDialog = ColorPickerDialog.createColorPickerDialog(mContext);
        colorDialogPresenter = ColorDialogImpl(chapterInformationView, this)
    }

    override fun setTextColor(color: String) {
        ivTextColor.setBackgroundColor(Color.parseColor(color))
    }

    override fun setBackgroundColor(color: String) {
        ivBackgroundColor.setBackgroundColor(Color.parseColor(color))
    }

    private fun processEvent() {
        ivBack.setOnClickListener {
            val textAndLineStretchDialog = TextAndLineStretchDialog(chapterInformationView)
            textAndLineStretchDialog.show()
            dismiss()
        }

//        ivTextColor.setOnClickListener {
//            mColorPickerDialog.setOnColorPickedListener((color, hexVal) -> colorDialogPresenter.viewTextColor(hexVal));
//            colorDialogPresenter.setTextColorPickerDialog()
//        }

//        ivBackgroundColor.setOnClickListener {
//            mColorPickerDialog.setOnColorPickedListener((color, hexVal) -> colorDialogPresenter.viewBackgroundColor(hexVal));
//            colorDialogPresenter.setBackgroundColorPickerDialog()
//        }

        tvUseTextColor.setOnClickListener { colorDialogPresenter.setTextColor() }
        tvUseBackgroundColor.setOnClickListener { colorDialogPresenter.setBackgroundColor() }
    }

    override fun setColorPickerDialog(color: String) {
//        mColorPickerDialog.setHexaDecimalTextColor(mContext.getResources().getColor(R.color.black));
//        mColorPickerDialog.setInitialColor(Color.parseColor(color));
//        mColorPickerDialog.setLastColor(color);
//        mColorPickerDialog.show();
    }
}

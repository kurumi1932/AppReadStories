package huce.fit.appreadstories.dialog.setting

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.chapter.information.ChapterInformationView
import huce.fit.appreadstories.dialog.BaseDialog
import huce.fit.appreadstories.dialog.setting.color.ColorDialog

class TextAndLineStretchDialog(private val chapterInformationView: ChapterInformationView) :
    BaseDialog(chapterInformationView as Context), TextAndLineStretchDialogView {

    companion object {
        private val BACKGROUND_COLOR = intArrayOf(
            R.color.circle1, R.color.circle2, R.color.circle3, R.color.circle4, R.color.circle5,
            R.color.circle6, R.color.circle7
        )
        private val TEXT_COLOR = intArrayOf(R.color.black, R.color.white, R.color.medium_sea_green)
    }

    private lateinit var ivSettingColor: ImageView
    private lateinit var ivTextSizeDown: ImageView
    private lateinit var ivTextSizeUp: ImageView
    private lateinit var ivLineStretchDown: ImageView
    private lateinit var ivLineStretchUp: ImageView
    private lateinit var tvTextSize: TextView
    private lateinit var tvLineStretch: TextView
    private lateinit var tvColor1: TextView
    private lateinit var tvColor2: TextView
    private lateinit var tvColor3: TextView
    private lateinit var tvColor4: TextView
    private lateinit var tvColor5: TextView
    private lateinit var tvColor6: TextView
    private lateinit var tvColor7: TextView
    private lateinit var textAndLineStretchDialogPresenter: TextAndLineStretchDialogImpl

    init{
        setDialog(R.layout.dialog_setting)
        setWindow(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0
        )
        init()
        processEvent()
    }

    private fun init() {
        ivSettingColor = dialog.findViewById(R.id.ivSettingColor)
        ivTextSizeDown = dialog.findViewById(R.id.ivTextSizeDown)
        ivTextSizeUp = dialog.findViewById(R.id.ivTextSizeUp)
        ivLineStretchDown = dialog.findViewById(R.id.ivLineStretchDown)
        ivLineStretchUp = dialog.findViewById(R.id.ivLineStretchUp)
        tvTextSize = dialog.findViewById(R.id.tvTextSize)
        tvLineStretch = dialog.findViewById(R.id.tvLineStretch)

        tvColor1 = dialog.findViewById(R.id.tvColor1)
        tvColor2 = dialog.findViewById(R.id.tvColor2)
        tvColor3 = dialog.findViewById(R.id.tvColor3)
        tvColor4 = dialog.findViewById(R.id.tvColor4)
        tvColor5 = dialog.findViewById(R.id.tvColor5)
        tvColor6 = dialog.findViewById(R.id.tvColor6)
        tvColor7 = dialog.findViewById(R.id.tvColor7)

        textAndLineStretchDialogPresenter = TextAndLineStretchDialogImpl(
            chapterInformationView, this
        )
    }

    override fun getData(fontSize: String, lineStretch: String) {
        Log.e("dialog", "NHT fontSize: $fontSize")
        tvTextSize.text = fontSize
        tvLineStretch.text = lineStretch
    }

    private fun processEvent() {
        ivSettingColor.setOnClickListener {
            dismiss()
            val colorDialog = ColorDialog(chapterInformationView)
            colorDialog.show()
        }

        ivTextSizeDown.setOnClickListener {
            textAndLineStretchDialogPresenter.downTextSize()
        }

        ivTextSizeUp.setOnClickListener {
            textAndLineStretchDialogPresenter.upTextSize()
        }

        ivLineStretchDown.setOnClickListener {
            textAndLineStretchDialogPresenter.downLineStretch()
        }

        ivLineStretchUp.setOnClickListener {
            textAndLineStretchDialogPresenter.upLineStretch()
        }

        tvColor1.setOnClickListener { setColor(0, 0) }
        tvColor2.setOnClickListener { setColor(1, 0) }
        tvColor3.setOnClickListener { setColor(2, 0) }
        tvColor4.setOnClickListener { setColor(3, 1) }
        tvColor5.setOnClickListener { setColor(4, 2) }
        tvColor6.setOnClickListener { setColor(5, 1) }
        tvColor7.setOnClickListener { setColor(6, 2) }
    }

    private fun setColor(indexBackgroundColor: Int, indexTextColor: Int) {
        textAndLineStretchDialogPresenter.setColor(
            context.getColor(BACKGROUND_COLOR[indexBackgroundColor]),
            context.getColor(TEXT_COLOR[indexTextColor])
        )
    }

    override fun setTextSize(textSize: String) {
        tvTextSize.text = textSize
    }

    override fun setLineStretch(lineStretch: String) {
        tvLineStretch.text = lineStretch
    }
}

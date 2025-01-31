package huce.fit.appreadstories.dialog.rate

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.dialog.BaseDialog
import huce.fit.appreadstories.model.Rate
import huce.fit.appreadstories.story.information.StoryInformationView
import huce.fit.appreadstories.util.AppUtil
import java.util.Locale

class RateDialog(private val storyInformationView: StoryInformationView) :
    BaseDialog(storyInformationView as Context), RateDialogView {

    companion object {
        private val RATE =
            intArrayOf(R.id.ivRate1, R.id.ivRate2, R.id.ivRate3, R.id.ivRate4, R.id.ivRate5)
    }

    private lateinit var ivClose: ImageView
    private lateinit var ivRate1: ImageView
    private lateinit var ivRate2: ImageView
    private lateinit var ivRate3: ImageView
    private lateinit var ivRate4: ImageView
    private lateinit var ivRate5: ImageView
    private lateinit var btRate: Button
    private lateinit var btDeleteRate: Button
    private lateinit var etRate: EditText
    private lateinit var tvNumberTextRate: TextView

    private val rateDialogPresenter = RateDialogImpl(storyInformationView, this)

    init {
        setDialog(R.layout.dialog_rate)
        setWindow(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER, 0
        )
        init()
        processEvents()
    }

    private fun init() {
        ivClose = dialog.findViewById(R.id.ivClose)
        ivRate1 = dialog.findViewById(R.id.ivRate1)
        ivRate2 = dialog.findViewById(R.id.ivRate2)
        ivRate3 = dialog.findViewById(R.id.ivRate3)
        ivRate4 = dialog.findViewById(R.id.ivRate4)
        ivRate5 = dialog.findViewById(R.id.ivRate5)
        etRate = dialog.findViewById(R.id.etRate)
        tvNumberTextRate = dialog.findViewById(R.id.tvNumberTextRate)
        btRate = dialog.findViewById(R.id.btRate)
        btDeleteRate = dialog.findViewById(R.id.btDeleteRate)
    }

    override fun setData(rate: Rate) {
        if (rate.rateSuccess == 1) {
            setRatePoint(rate.ratePoint)
            etRate.setText(rate.rateContent)
            tvNumberTextRate.text =
                String.format(Locale.getDefault(), "%d/400", rate.rateContent.length)
            btDeleteRate.visibility = View.VISIBLE
        } else {
            btDeleteRate.visibility = View.GONE
        }
    }

    private fun processEvents() {
        ivClose.setOnClickListener { dialog.dismiss() }
        ivRate1.setOnClickListener { setRatePoint(1) }
        ivRate2.setOnClickListener { setRatePoint(2) }
        ivRate3.setOnClickListener { setRatePoint(3) }
        ivRate4.setOnClickListener { setRatePoint(4) }
        ivRate5.setOnClickListener { setRatePoint(5) }

        etRate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if (etRate.lineCount > 7) {
                    AppUtil.setToast(context, "Vượt quá sô dòng giới hạn đánh giá!")
                    etRate.text.delete(etRate.text.length - 1, etRate.text.length)
                }
                tvNumberTextRate.text =
                    String.format(Locale.getDefault(), "%d/400", s.toString().length)
            }
        })

        btDeleteRate.setOnClickListener {//delete rate
            storyInformationView.deleteRate()
            dismiss()
        }

        btRate.setOnClickListener {
            rateDialogPresenter.rate(etRate.text.toString().trim { it <= ' ' })
        }
    }

    override fun noRatePoint() {
        AppUtil.setToast(context, "Vui lòng cho điểm dánh giá!")
    }

    private fun setRatePoint(ratePoint: Int) {
        rateDialogPresenter.setRatePoint(ratePoint)
        for (i in 0 until ratePoint) {
            val ivRate: ImageView = dialog.findViewById(RATE[i])
            ivRate.setImageResource(R.drawable.ic_rate)
        }
        for (i in ratePoint..4) {
            val ivRate: ImageView = dialog.findViewById(RATE[i])
            ivRate.setImageResource(R.drawable.ic_not_rate)
        }
    }
}
package huce.fit.appreadstories.dialog.rate

import huce.fit.appreadstories.dialog.BaseDialogView
import huce.fit.appreadstories.model.Rate

interface RateDialogView : BaseDialogView {
    fun setData(rate: Rate)
    fun noRatePoint()
}

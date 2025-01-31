package huce.fit.appreadstories.dialog.rate

interface RateDialogPresenter {
    fun setRatePoint(ratePoint: Int)
    fun rate(rateContent: String)
}

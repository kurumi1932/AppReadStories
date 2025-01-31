package huce.fit.appreadstories.account.update

interface AccountUpdateView {
    fun getInfoAccount(name: String, email: String, birthday: String)
    fun update(status: Int)
    fun changeDatePicker(dateStr: String)
}
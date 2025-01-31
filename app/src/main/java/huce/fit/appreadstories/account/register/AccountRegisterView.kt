package huce.fit.appreadstories.account.register

interface AccountRegisterView {
    fun changeDatePicker(birthday: String)
    fun register(status: Int)
}
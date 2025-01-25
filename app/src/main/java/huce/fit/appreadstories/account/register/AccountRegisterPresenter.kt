package huce.fit.appreadstories.account.register

import huce.fit.appreadstories.account.BaseAccountPresenter

interface AccountRegisterPresenter : BaseAccountPresenter {

    fun register(username: String, password: String, email: String, name: String, birthday: String)
    fun openDatePicker()
}

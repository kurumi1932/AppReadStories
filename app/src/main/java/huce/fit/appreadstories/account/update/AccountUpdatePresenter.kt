package huce.fit.appreadstories.account.update

import huce.fit.appreadstories.account.BaseAccountPresenter

interface AccountUpdatePresenter : BaseAccountPresenter {

    fun updateAccount(accountId: Int, email: String, name: String, birthday: String)
    fun changePassword(accountId: Int, oldPass: String, newPass: String)
    fun openDatePicker()
}

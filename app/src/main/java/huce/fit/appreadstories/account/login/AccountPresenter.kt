package huce.fit.appreadstories.account.login

import huce.fit.appreadstories.account.BaseAccountPresenter

interface AccountPresenter : BaseAccountPresenter {

    fun login(username: String, password: String)
}

package huce.fit.appreadstories.startapp

import huce.fit.appreadstories.account.BaseAccountPresenter

interface StartAppPresenter : BaseAccountPresenter {
    fun checkLogged(): Boolean
}

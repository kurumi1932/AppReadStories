package huce.fit.appreadstories.account.manager

import android.content.Context
import huce.fit.appreadstories.account.BaseAccountImpl

class AccountManagerImpl(accountManagerView: AccountManagerView, context: Context): BaseAccountImpl(context) , AccountManagerPresenter {

    companion object{
        const val TAG = "AccountManagerImpl"
    }
    init{
        accountManagerView.setName(getAccount().getName()!!)
    }

    override fun logout() {
        setAccount().myRemove()
    }
}

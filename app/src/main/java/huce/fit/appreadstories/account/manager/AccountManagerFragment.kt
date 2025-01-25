package huce.fit.appreadstories.account.manager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import huce.fit.appreadstories.R
import huce.fit.appreadstories.account.login.AccountLoginActivity
import huce.fit.appreadstories.account.update.AccountUpdateActivity
import huce.fit.appreadstories.guile.GuideActivity
import huce.fit.appreadstories.main.MainActivity
import huce.fit.appreadstories.main.MainView

class AccountManagerFragment : Fragment(), AccountManagerView {

    private lateinit var tvName: TextView
    private lateinit var tvEditAccount: TextView
    private lateinit var tvGuide: TextView
    private lateinit var tvLogOut: TextView
    private lateinit var mAccountManagerPresenter: AccountManagerPresenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_account_manager, container, false)
        init(view)
        processEvents()
        return view
    }

    fun init(view: View) {
        tvName = view.findViewById(R.id.tvName)
        tvEditAccount = view.findViewById(R.id.tvEditAccount)
        tvGuide = view.findViewById(R.id.tvGuide)
        tvLogOut = view.findViewById(R.id.tvLogOut)

        mAccountManagerPresenter = AccountManagerImpl(this, requireActivity())
    }

    override fun setName(name: String) {
        tvName.text = name
    }

    fun processEvents() {
        tvEditAccount.setOnClickListener {
            val intent = Intent(requireActivity(), AccountUpdateActivity::class.java)
            startActivity(intent)
        }
        tvGuide.setOnClickListener {
            val intent = Intent(requireActivity(), GuideActivity::class.java)
            startActivity(intent)
        }
        tvLogOut.setOnClickListener {
            mAccountManagerPresenter.logout()
            val intent = Intent(requireActivity(), AccountLoginActivity::class.java)
            startActivity(intent)
            val mainView: MainView = MainActivity()
            mainView.closeMain()
        }
    }
}
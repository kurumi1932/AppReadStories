package huce.fit.appreadstories.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.account.manager.AccountManagerFragment
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.story.list.download.StoryDownloadFragment
import huce.fit.appreadstories.story.list.filter.StoryFilterFragment
import huce.fit.appreadstories.story.list.follow.StoryFollowFragment
import huce.fit.appreadstories.story.list.online.StoryFragment
import huce.fit.appreadstories.util.AppUtil

class MainActivity : AppCompatActivity() , MainView {

    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var fragmentManager: FragmentManager
    private lateinit var btNavigationView: BottomNavigationView
    private lateinit var mainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
        processEvents()
    }


    private fun initView() {
        mainPresenter = MainImpl()
        fragmentManager = supportFragmentManager
        fragmentTransaction = supportFragmentManager.beginTransaction()

        btNavigationView = findViewById(R.id.btNavigationView)

        if (isConnecting(this)) {
            addFragment(StoryFragment(), R.id.btMenuStory)
        } else {
            addFragment(StoryDownloadFragment(), R.id.btMenuDownload)
        }

        onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onResume() {
        super.onResume()
        if (!isConnecting(this)) {
            changeFragment(R.id.btMenuDownload, StoryDownloadFragment())
        }
    }

    private fun addFragment(fragment: Fragment, id: Int) {
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.fragment, fragment, null)
        fragmentTransaction.commit()

        btNavigationView.menu.findItem(id).setChecked(true)
        mainPresenter.addFragment(id)
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun changeFragment(id: Int, fragment: Fragment) {
        if (!btNavigationView.menu.findItem(id).isChecked) {
            mainPresenter.changeFragment(id)
            btNavigationView.menu.findItem(id).setChecked(true)
            replaceFragment(fragment)
        }
    }

    override fun closeMain() {
        finish()
    }

    @SuppressLint("NonConstantResourceId")
    private fun processEvents() {
        btNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (val id = item.itemId) {
                R.id.btMenuStory -> changeFragment(id, StoryFragment())
                R.id.btMenuFilter -> changeFragment(id, StoryFilterFragment())
                R.id.btMenuFollow -> changeFragment(id, StoryFollowFragment())
                R.id.btMenuDownload -> changeFragment(id, StoryDownloadFragment())
                R.id.btMenuAccount -> changeFragment(id, AccountManagerFragment())
            }
            true
        }
    }

    val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val count = mainPresenter.backPressed()
            if (count < 0) {
                finish()
            } else if (count == 0) {
                AppUtil.setToast(this@MainActivity, "Nhấn lần nữa để thoát ứng dụng!")
            } else {
                btNavigationView.menu.findItem(mainPresenter.getId()).setChecked(true)
                mainPresenter.removeCount()
            }
        }
    }
}
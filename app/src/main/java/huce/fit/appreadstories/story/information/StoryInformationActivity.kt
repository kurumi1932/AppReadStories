package huce.fit.appreadstories.story.information

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import huce.fit.appreadstories.R
import huce.fit.appreadstories.adapters.ViewPagerStoryInterfaceAdapter
import huce.fit.appreadstories.checknetwork.isConnecting
import huce.fit.appreadstories.dialog.confirm.ConfirmCheckAgeDialog
import huce.fit.appreadstories.dialog.rate.RateDialog
import huce.fit.appreadstories.model.Story
import huce.fit.appreadstories.util.AppUtil
import java.util.Locale


class StoryInformationActivity : AppCompatActivity() , StoryInformationView {
    
    companion object{
        private val RATE = intArrayOf(R.id.ivRate1, R.id.ivRate2, R.id.ivRate3, R.id.ivRate4, R.id.ivRate5)
    }
    
    private lateinit var viewPager: ViewPager2
    private lateinit var linearLayout: LinearLayout
    private lateinit var ivBack: ImageView
    private lateinit var ivStory:ImageView
    private lateinit var tvStoryName: TextView
    private lateinit var tvAuthor:TextView
    private lateinit var tvStatus:TextView
    private lateinit var tvSpecies:TextView
    private lateinit var tvLike:TextView
    private lateinit var tvView:TextView
    private lateinit var tvSumChapter:TextView
    private lateinit var tvComment:TextView
    private lateinit var tvRate:TextView
    private lateinit var btReadStory: Button
    private lateinit var btFollow:Button
    private lateinit var btDownLoad:Button
    private lateinit var storyInformationPresenter: StoryInformationImpl
    private lateinit var viewPagerStoryInterfaceAdapter: ViewPagerStoryInterfaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_story_interface)

        checkAge()
        init()
        processEvents()
    }

    private fun init() {
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        viewPager = findViewById(R.id.view_pager)
        linearLayout = findViewById(R.id.linearLayoutRate)
        tvStoryName = findViewById(R.id.tvStoryName)
        tvAuthor = findViewById(R.id.tvAuthor)
        tvStatus = findViewById(R.id.tvStatus)
        tvSpecies = findViewById(R.id.tvSpecies)
        tvRate = findViewById(R.id.tvRate)
        tvLike = findViewById(R.id.tvLike)
        tvView = findViewById(R.id.tvView)
        tvSumChapter = findViewById(R.id.tvSumChapter)
        tvComment = findViewById(R.id.tvComment)
        ivBack = findViewById(R.id.ivBack)
        ivStory = findViewById(R.id.ivStory)
        btReadStory = findViewById(R.id.btReadStory)
        btFollow = findViewById(R.id.btFollow)
        btDownLoad = findViewById(R.id.btDownload)

        storyInformationPresenter = StoryInformationImpl(this)
        viewPagerStoryInterfaceAdapter = ViewPagerStoryInterfaceAdapter(this)
        viewPager.adapter = viewPagerStoryInterfaceAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.setText("Giới thiệu")
                1 -> tab.setText("Đánh giá")
            }
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        storyInformationPresenter.start()
    }

    override fun close() = finish()

    override fun setRateStartLight(index: Int) {
        val ivRate: ImageView = findViewById(RATE[index])
        ivRate.setImageResource(R.drawable.ic_rate)
    }

    override fun setRateStartDark(index: Int) {
        val ivRate: ImageView = findViewById(RATE[index])
        ivRate.setImageResource(R.drawable.ic_not_rate)
    }

    override fun setData(story: Story) {
        tvStoryName.text = story.storyName
        tvAuthor.text = story.author
        tvStatus.text = story.status
        tvSpecies.text = story.species
        Picasso.get().load(story.image).into(ivStory)
        tvRate.text = story.ratePoint.toString()
        storyInformationPresenter.showRate(story.ratePoint.toInt())
        tvLike.text = String.format(Locale.getDefault(), "%d\nYêu thích", story.totalLikes)
        tvView.text = String.format(Locale.getDefault(), "%d\nLượt xem", story.totalViews)
        tvSumChapter.text = String.format(Locale.getDefault(), "%d\nChương", story.sumChapter)
        tvComment.text = String.format(Locale.getDefault(), "%d\nBình luận", story.totalComments)
    }

    override fun setRateId(rateId: Int) {
        storyInformationPresenter.setRateId(rateId)
    }

    private fun checkAge() {
        val confirmCheckAgeDialog = ConfirmCheckAgeDialog(this)
        confirmCheckAgeDialog.show()
    }

    override fun checkInteractive(isLike: Int, likeNumber: String) {
        if (isLike == 0) {
            tvLike.setTextColor(this.getColor(R.color.black))
        } else {
            tvLike.setTextColor(this.getColor(R.color.orange))
        }
        tvLike.text = likeNumber
    }

    private fun processEvents() {
        ivBack.setOnClickListener { finish() }

        if (isConnecting(this)) {
            linearLayout.setOnClickListener {
                //kiểm tra xem tài khoản đã đánh giá chưa
                Handler().postDelayed({ storyInformationPresenter.checkRateOfAccount() }, 1000)
            }

            tvLike.setOnClickListener { storyInformationPresenter.likeStory() }

            tvComment.setOnClickListener {
                storyInformationPresenter.enterComment()
            }

            btFollow.setOnClickListener { storyInformationPresenter.followStory() }

            btDownLoad.setOnClickListener { storyInformationPresenter.enterDownload() }
        }
        tvSumChapter.setOnClickListener { storyInformationPresenter.enterChapter() }
        btReadStory.setOnClickListener { storyInformationPresenter.enterReadStory() }
    }

    override fun followStory(isFollow: Int) {
        when (isFollow) {
            1 -> {
                btFollow.text = "Đang theo dõi"
                btFollow.setTextColor(this.getColor(R.color.medium_sea_green))
                btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_follow, 0, 0)
            }

            2 -> {
                btFollow.text = "Theo dõi"
                btFollow.setTextColor(this.getColor(R.color.dim_gray))
                btFollow.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_unfollow, 0, 0)
            }
        }
    }

    override fun openDialogRate() {
        val rateDialog = RateDialog(this)
        rateDialog.show()
    }

    override fun addRate(ratePoint: Int, rate: String) {
        storyInformationPresenter.addRate(ratePoint, rate)
    }

    override fun deleteRate() {
        storyInformationPresenter.deleteRate()
    }

    override fun updateRate(ratePoint: Int, rate: String) {
        storyInformationPresenter.updateRate(ratePoint, rate)
    }

    override fun reloadViewPaper(text: String) {
        viewPager.adapter = viewPagerStoryInterfaceAdapter //reload view pager
        AppUtil.setToast(this, text)
    }

    override fun checkStory() {
        val intent = Intent(this, CheckStoryService::class.java)
        startService(intent)
    }
}
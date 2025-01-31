package huce.fit.appreadstories.chapter.information

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.comment.CommentActivity
import huce.fit.appreadstories.dialog.chapter_list.ChapterListDialog
import huce.fit.appreadstories.dialog.setting.TextAndLineStretchDialog
import huce.fit.appreadstories.model.Chapter
import huce.fit.appreadstories.util.AppUtil

class ChapterInformationActivity : AppCompatActivity(), ChapterInformationView {

    companion object {
        private const val TAG = "ChapterInformationActivity"
    }
    
    private lateinit var constraintLayout: ConstraintLayout
    private lateinit var btNavigationView: BottomNavigationView
    private lateinit var pbReLoad: ProgressBar
    private lateinit var scView: ScrollView
    private lateinit var tvTitle: TextView
    private lateinit var tvContent: TextView
    private lateinit var rlNextAndPrevious: RelativeLayout
    private lateinit var ivPrevious: ImageView
    private lateinit var ivNext: ImageView
    private lateinit var chapterInformationPresenter: ChapterInformationPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter_read)
        init()
        processEvents()
    }

    fun init() {
        constraintLayout = findViewById(R.id.constraintLayout)
        btNavigationView = findViewById(R.id.btNavigationView)
        scView = findViewById(R.id.scView)
        rlNextAndPrevious = findViewById(R.id.rlNextAndPrevious)
        ivPrevious = findViewById(R.id.ivPrevious)
        ivNext = findViewById(R.id.ivNext)
        pbReLoad = findViewById(R.id.pbReLoad)
        tvTitle = findViewById(R.id.tvTitle)
        tvContent = findViewById(R.id.tvContent)
        btNavigationView.visibility = View.GONE
        rlNextAndPrevious.visibility = View.GONE
        chapterInformationPresenter = ChapterInformationImpl(this, intent)
        reloadChapter(2)
    }

    override fun setBackgroundColor(backgroundColor: String) {
        constraintLayout.setBackgroundColor(Color.parseColor(backgroundColor))
    }

    override fun setTextColor(textColor: String) {
        Log.e(TAG, "NHT textColor: $textColor")
        tvTitle.setTextColor(Color.parseColor(textColor))
        tvContent.setTextColor(Color.parseColor(textColor))
    }

    override fun setTextSize(size: Int) {
        tvTitle.textSize = (size + 2).toFloat()
        tvContent.textSize = size.toFloat()
    }

    override fun setLineStretch(lineStretch: Float) {
        tvContent.setLineSpacing(4f, lineStretch)
    }

    @SuppressLint("NonConstantResourceId")
    fun processEvents() {
        btNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.btMenuBack -> finish()
                R.id.btMenuChapterList -> ChapterListDialog(this)
                R.id.btMenuComment -> {
                    val intent = Intent(this, CommentActivity::class.java)
                    startActivity(intent)
                }

                R.id.btMenuSetting -> {
                    val textAndLineStretchDialog = TextAndLineStretchDialog(this)
                    textAndLineStretchDialog.show()
                }
            }
            false
        }

        //ẩn hiện btnavigation
        constraintLayout.setOnClickListener {
            if (btNavigationView.visibility == View.VISIBLE) {
                btNavigationView.visibility = View.GONE
                rlNextAndPrevious.visibility = View.GONE
            } else {
                btNavigationView.visibility = View.VISIBLE
                rlNextAndPrevious.visibility = View.VISIBLE
            }
        }
        ivPrevious.setOnClickListener {
            reloadChapter(1)
        }
        ivNext.setOnClickListener {
            reloadChapter(3)
        }
    }

    override fun reloadChapter(changeChapter: Int) {
        pbReLoad.visibility = View.VISIBLE
        Handler().postDelayed({
            chapterInformationPresenter.changeChapter(changeChapter)
        }, 500)
    }

    override fun moveChapter(chapterId: Int) {
        pbReLoad.visibility = View.VISIBLE
        Handler().postDelayed({
            chapterInformationPresenter.getChapter(chapterId)
        }, 500)
    }

    override fun setData(chapter: Chapter, text: String) {
        if (chapter.chapterId != 0) {
            scView.getViewTreeObserver().addOnGlobalLayoutListener {
                // trở lại đầu trang
                scView.fullScroll(View.FOCUS_UP)
            }
            tvTitle.text =
                String.format("Chương %s: %s", chapter.chapterNumber, chapter.chapterName)
            tvContent.text = String.format(
                "Người đăng: %s\nNgày đăng: %s\n\n\n%s", chapter.poster, chapter.postDay,
                chapter.content
            )
            chapterInformationPresenter.insertChapterRead(chapter.chapterId)
            pbReLoad.visibility = View.GONE
        } else {
            pbReLoad.visibility = View.GONE
            AppUtil.setToast(this, text)
        }
    }
}

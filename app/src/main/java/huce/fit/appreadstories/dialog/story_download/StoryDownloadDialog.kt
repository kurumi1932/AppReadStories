package huce.fit.appreadstories.dialog.story_download

import android.content.Context
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.dialog.BaseDialog
import huce.fit.appreadstories.story.list.download.StoryDownloadView

class StoryDownloadDialog(storyDownloadView: StoryDownloadView) :
    BaseDialog(storyDownloadView as Context) {

    private lateinit var tvReadStory: TextView
    private lateinit var tvDelete: TextView

    init {
        setDialog(R.layout.dialog_story_download)
        setWindow(
            WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
            Gravity.BOTTOM, 0
        )
        init()
        processEvents(storyDownloadView)
    }

    private fun init() {
        val tvStoryName = dialog.findViewById<TextView>(R.id.tvStoryName)
        tvReadStory = dialog.findViewById(R.id.tvReadStory)
        tvDelete = dialog.findViewById(R.id.tvDelete)

        val mStoryDownloadDialogPresenter = StoryDownloadDialogImpl(context)
        tvStoryName.text = mStoryDownloadDialogPresenter.getStoryName()
    }

    private fun processEvents(storyDownloadView: StoryDownloadView) {
        tvReadStory.setOnClickListener {
            storyDownloadView.openStoryDownload()
            dialog.dismiss()
        }
        tvDelete.setOnClickListener {
            storyDownloadView.deleteStoryDownload()
            dialog.dismiss()
        }
    }
}

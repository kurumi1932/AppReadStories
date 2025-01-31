package huce.fit.appreadstories.dialog.confirm

import android.content.Context
import huce.fit.appreadstories.story.information.StoryInformationView

class ConfirmCheckAgeDialog(storyInformationView: StoryInformationView) :
    BaseConfirmDialog(storyInformationView as Context) {

    init {
        init()
        processEvents(storyInformationView)
    }

    private fun init() {
        // true click bên ngoài dialog có thể tắt dialog
        dialog.setCancelable(false)
        val title = "Cảnh báo nội dung"
        val content =
            "Truyện bạn muốn xem có chứa nội dung nhạy cảm, phù hợp với lứa tuổi 18 trở lên. Hãy cân nhắc trước khi tiếp tục."
        setContent(title, content)
    }

    private fun processEvents(storyInformationView: StoryInformationView) {
        tvYes.setOnClickListener { dialog.dismiss() }
        tvNo.setOnClickListener {
            dialog.dismiss()
            storyInformationView.close()
        }
    }
}

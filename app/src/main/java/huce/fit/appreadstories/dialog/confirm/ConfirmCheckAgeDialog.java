package huce.fit.appreadstories.dialog.confirm;

import android.content.Context;

import huce.fit.appreadstories.story.information.StoryInformationView;

public class ConfirmCheckAgeDialog extends BaseConfirmDialog {

    public ConfirmCheckAgeDialog(StoryInformationView storyInformationView, Context context) {
        super(context);

        init();
        processEvents(storyInformationView);
    }

    private void init(){
        // true click bên ngoài dialog có thể tắt dialog
        mDialog.setCancelable(false);
        String title = "Cảnh báo nội dung";
        String content = "Truyện bạn muốn xem có chứa nội dung nhạy cảm, phù hợp với lứa tuổi 18 trở lên. Hãy cân nhắc trước khi tiếp tục.";
        setContent(title, content);
    }

    private void processEvents(StoryInformationView storyInformationView) {
        tvYes.setOnClickListener(view -> mDialog.dismiss());
        tvNo.setOnClickListener(view -> {
            mDialog.dismiss();
            storyInformationView.close();
        });
    }
}

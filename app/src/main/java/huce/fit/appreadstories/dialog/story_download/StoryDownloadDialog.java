package huce.fit.appreadstories.dialog.story_download;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.dialog.BaseDialog;
import huce.fit.appreadstories.story.list.download.StoryDownloadView;

public class StoryDownloadDialog extends BaseDialog {

    private TextView tvReadStory;
    private TextView tvDelete;

    public StoryDownloadDialog(StoryDownloadView storyDownloadView, Context context) {
        super(context);

        setDialog(R.layout.dialog_story_download);
        setWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0);
        init();
        processEvents(storyDownloadView);
    }

    private void init() {
        TextView tvStoryName = mDialog.findViewById(R.id.tvStoryName);
        tvReadStory = mDialog.findViewById(R.id.tvReadStory);
        tvDelete = mDialog.findViewById(R.id.tvDelete);

        StoryDownloadDialogPresenter mStoryDownloadDialogPresenter = new StoryDownloadDialogImpl(mContext);
        tvStoryName.setText(mStoryDownloadDialogPresenter.getStoryName());
    }

    private void processEvents(StoryDownloadView storyDownloadView) {
        tvReadStory.setOnClickListener(view -> {
            storyDownloadView.openStoryDownload();
            mDialog.dismiss();
        });
        tvDelete.setOnClickListener(view -> {
            storyDownloadView.deleteStoryDownload();
            mDialog.dismiss();
        });
    }
}

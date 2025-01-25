package huce.fit.appreadstories.dialog.confirm;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.comment.CommentView;
import huce.fit.appreadstories.dialog.BaseDialog;

public class ConfirmEditOrDeleteCommentDialog extends BaseDialog {

    private final CommentView mCommentView;
    private TextView tvUpdate,tvDelete;
    public ConfirmEditOrDeleteCommentDialog(CommentView commentView, Context context) {
        super(context);
        mCommentView =commentView;
        setDialog( R.layout.dialog_comment_update_delete);
        setWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM, 0);
        init();
        processEvents();
    }

    private void init() {
        mDialog.setCancelable(false);

        tvUpdate = mDialog.findViewById(R.id.tvUpdate);
        tvDelete = mDialog.findViewById(R.id.tvDelete);
    }

    private void processEvents() {
        tvUpdate.setOnClickListener(view -> {
            mCommentView.setDataUpdate();
            dismiss();
        });
        tvDelete.setOnClickListener(view -> {
            ConfirmDeleteComment confirmDeleteComment = new ConfirmDeleteComment(mCommentView,mContext);
            confirmDeleteComment.show();
            dismiss();
        });
    }
}

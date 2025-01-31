package huce.fit.appreadstories.dialog.confirm;

import android.content.Context;

import huce.fit.appreadstories.comment.CommentView;

public class ConfirmDeleteComment extends BaseConfirmDialog{
    private final CommentView mCommentView;
    ConfirmDeleteComment(CommentView commentView, Context context) {
        super(context);
        mCommentView = commentView;

        init();
        processEvents();
    }

    private void init(){
        mDialog.setCancelable(false);
        String title = "Xóa bình luận";
        String content = "\"Bạn có muốn xóa bình luận không?";
        setContent(title, content);
    }

    private void processEvents() {
        tvYes.setOnClickListener(view -> {
            mCommentView.deleteComment();
            dismiss();
        });
        tvNo.setOnClickListener(view -> {
            dismiss();
        });
    }
}

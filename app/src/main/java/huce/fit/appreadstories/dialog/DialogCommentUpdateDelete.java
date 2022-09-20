package huce.fit.appreadstories.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import huce.fit.appreadstories.R;

public class DialogCommentUpdateDelete extends android.app.Dialog {
    private Context context;
    private int gravity, position;
    private boolean cancelable;
    private TextView tvUpdate, tvDelete;

    public DialogCommentUpdateDelete(@NonNull Context context, int gravity, boolean cancelable,int position) {
        super(context);
        this.context = context;
        this.gravity = gravity;
        this.cancelable = cancelable;
        this.position  = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_comment_update_delete);

        tvUpdate = findViewById(R.id.tvUpdate);
        tvDelete = findViewById(R.id.tvDelete);

        setUI();
        processEvents();
    }

    private void setUI(){
        Window window = getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        setCancelable(cancelable);
    }

    private void processEvents(){
        tvUpdate.setOnClickListener(view -> {
//            deleteComment(position);
            dismiss();
        });
        tvDelete.setOnClickListener(view -> {
            DialogCommentNotifyDelete dlCommentNotifyDelete = new DialogCommentNotifyDelete(context, Gravity.CENTER,true, position);
            dlCommentNotifyDelete.show();
            dismiss();
        });
    }
}

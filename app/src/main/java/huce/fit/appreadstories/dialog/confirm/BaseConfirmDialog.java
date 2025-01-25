package huce.fit.appreadstories.dialog.confirm;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.dialog.BaseDialog;

public class BaseConfirmDialog extends BaseDialog implements BaseConfirmDialogView {

    public TextView tvTitle, tvContent, tvYes, tvNo;

    BaseConfirmDialog(Context context) {
        super(context);
        setDialog( R.layout.dialog_confirm);
        setWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, Gravity.CENTER, 0);
        init();
    }

    private void init() {
        tvTitle = mDialog.findViewById(R.id.tvTitle);
        tvContent = mDialog.findViewById(R.id.tvContent);
        tvYes = mDialog.findViewById(R.id.tvYes);
        tvNo = mDialog.findViewById(R.id.tvNo);
    }

    @Override
    public void setContent(String title, String content) {
        tvTitle.setText(title);
        tvContent.setText(content);
    }
}

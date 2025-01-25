package huce.fit.appreadstories.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

public class BaseDialog implements BaseDialogView {

    public Dialog mDialog;
    public Context mContext;

    protected BaseDialog(Context context) {
        mContext = context;
    }

    public void setDialog(int layoutId) {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(layoutId);
        // true click bên ngoài dialog có thể tắt dialog
//        mDialog.setCancelable(false);
    }

    @Override
    public void setWindow(int width, int height, int gravity, int animation) {
        Window window = mDialog.getWindow();
        if (window == null) {
            return;
        }

        //chiều dài, chiều cao dialog
        window.setLayout(width, height);
        //background
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = gravity;
        if (animation != 0) {
            windowAttributes.windowAnimations = animation;
        }
        window.setAttributes(windowAttributes);
    }

    @Override
    public void show() {
        mDialog.show();
    }

    @Override
    public void dismiss() {
        mDialog.dismiss();
    }
}

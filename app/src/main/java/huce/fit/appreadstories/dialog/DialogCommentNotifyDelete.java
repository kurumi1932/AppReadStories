package huce.fit.appreadstories.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.model.BinhLuan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogCommentNotifyDelete extends Dialog {
    private Context context;
    private int gravity, position;
    private boolean cancelable;
    public TextView tvYes, tvNo;

    public DialogCommentNotifyDelete(@NonNull Context context, int gravity, boolean cancelable, int position) {
        super(context);
        this.context = context;
        this.gravity = gravity;
        this.cancelable = cancelable;
        this.position = position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_dialog_comment_notify_delete);

        tvYes = findViewById(R.id.tvYes);
        tvNo = findViewById(R.id.tvNo);

        setUI();
        processEvents();
    }

    private void setUI() {
        Window window = getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        setCancelable(true);
    }

    private void processEvents(){
        tvYes.setOnClickListener(view -> {
            deleteComment(position);
            dismiss();
        });
        tvNo.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void deleteComment(int idComment) {
        Api.apiInterface().deleteComment(idComment).enqueue(new Callback<BinhLuan>() {
            @Override
            public void onResponse(Call<BinhLuan> call, Response<BinhLuan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        Toast.makeText(context, "Bình luận đã được xóa", Toast.LENGTH_SHORT).show();
                    }
                    if (response.body().getSuccess() == 2) {
                        Toast.makeText(context, "Lỗi xóa bình luận!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BinhLuan> call, Throwable t) {
                Toast.makeText(context, "lỗi đuòng đẫn xóa bình luận!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

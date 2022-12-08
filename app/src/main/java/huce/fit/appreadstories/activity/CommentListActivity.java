package huce.fit.appreadstories.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.adapters.CommentAdapter;
import huce.fit.appreadstories.model.BinhLuan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentListActivity extends AppCompatActivity {

    private final List<BinhLuan> listComment = new ArrayList<>(); //data source
    private CommentAdapter commentAdapter;
    private RecyclerView rcViewComment;
    private ImageView ivBack, ivSent;
    private EditText etComment;
    private TextView tvCommentLength;
    private int idStory, idAccount, idComment;
    private String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);

        getSharedPreferences();
        idStory = getIntent().getIntExtra("idStory", 0);

        rcViewComment = findViewById(R.id.rcViewComment);
        ivBack = findViewById(R.id.ivBack);
        ivSent = findViewById(R.id.ivSent);
        etComment = findViewById(R.id.etComment);
        tvCommentLength = findViewById(R.id.tvCommentLength);

        getData();
        rcView();
        processEvents();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        name = sharedPreferences.getString("name", "");
        Log.e("idAccount_Comment", String.valueOf(idAccount));
    }

    private void rcView() {
        commentAdapter = new CommentAdapter(listComment, (position, view, isLongClick) -> {
//            Log.e("position_cmt", String.valueOf(position));
            if (isLongClick) {
                checkCommentOfAccount(position);
            }
        });//Đổ dữ liệu lên adpter
        rcViewComment.setHasFixedSize(true);
        rcViewComment.setLayoutManager(new LinearLayoutManager(this));
        rcViewComment.setAdapter(commentAdapter);

        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcViewComment.addItemDecoration(itemDecoration);
    }

    public void getData() {
        Api.apiInterface().getListComment(idStory).enqueue(new Callback<List<BinhLuan>>() {
            @Override
            public void onResponse(@NonNull Call<List<BinhLuan>> call,@NonNull Response<List<BinhLuan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listComment.clear();
                    listComment.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BinhLuan>> call,@NonNull Throwable t) {
                Log.e("Err_CommentList", "getData", t);
            }
        });
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> finish());
        ivSent.setOnClickListener(v -> {
            String comment = etComment.getText().toString();
            if (comment.equals("")) {
                Toast.makeText(CommentListActivity.this, "Vui lòng nhập bình luận!", Toast.LENGTH_SHORT).show();
            } else {
                if (idComment == 0) {
                    addComment(comment);
                } else {
                    updateComment(comment);
                    idComment = 0;
                }
                etComment.setText("");
            }
        });
        etComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etComment.getLineCount() > 31) {
                    Toast.makeText(CommentListActivity.this, "Vượt quá sô dòng giới hạn đánh giá!", Toast.LENGTH_SHORT).show();
                    etComment.getText().delete(etComment.getText().length() - 1, etComment.getText().length());
                }
                tvCommentLength.setText(String.format(Locale.getDefault(),"%d/2400", s.toString().length()));
            }
        });
    }

    private void addComment(String comment) {
        Api.apiInterface().addCommnet(idStory, idAccount, name, comment).enqueue(new Callback<BinhLuan>() {
            @Override
            public void onResponse(@NonNull Call<BinhLuan> call,@NonNull Response<BinhLuan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCommentsuccess() == 1) {
                        getData();
                    } else {
                        Toast.makeText(CommentListActivity.this, "Lỗi! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BinhLuan> call,@NonNull Throwable t) {
                Log.e("Err_CommentList", "addComment1", t);
            }
        });
    }

    private void updateComment(String comment) {
        Api.apiInterface().updateCommnet(idComment, comment).enqueue(new Callback<BinhLuan>() {
            @Override
            public void onResponse(@NonNull Call<BinhLuan> call,@NonNull Response<BinhLuan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCommentsuccess() == 1) {
                        Toast.makeText(CommentListActivity.this, "Bình luận đã được sửa", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                    if (response.body().getCommentsuccess() == 2) {
                        Toast.makeText(CommentListActivity.this, "Lỗi sửa bình luận!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BinhLuan> call,@NonNull Throwable t) {
                Log.e("Err_CommentList", "updateComment", t);
            }
        });
    }

    private void checkCommentOfAccount(int position) {
        Api.apiInterface().checkCommentOfAccount(position, idAccount).enqueue(new Callback<BinhLuan>() {
            @Override
            public void onResponse(@NonNull Call<BinhLuan> call,@NonNull Response<BinhLuan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCommentsuccess() == 1) {
                        String comment = response.body().getBinhluan();
                        openDialogCommentUpdateDelete(position, comment);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BinhLuan> call,@NonNull Throwable t) {
                Log.e("Err_CommentList", "checkCommentOfAccount", t);
            }
        });
    }

    private void openDialogCommentUpdateDelete(int position, String comment) {
        final Dialog dialogCommentDelete = new Dialog(this);
        dialogCommentDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCommentDelete.setContentView(R.layout.dialog_comment_update_delete);

        Window window = dialogCommentDelete.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // vị trí dialog
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.BOTTOM;
        window.setAttributes(windowAttributes);

        // click bên ngoài dialog có thể out
        dialogCommentDelete.setCancelable(true);

        TextView tvUpdate = dialogCommentDelete.findViewById(R.id.tvUpdate);
        TextView tvDelete = dialogCommentDelete.findViewById(R.id.tvDelete);

        tvUpdate.setOnClickListener(view -> {
            etComment.setText(comment);
            idComment = position;
            dialogCommentDelete.dismiss();
        });
        tvDelete.setOnClickListener(view -> {
            openDialogNotifyYesNo(position);
            dialogCommentDelete.dismiss();
        });

        dialogCommentDelete.show();
    }

    @SuppressLint("SetTextI18n")
    private void openDialogNotifyYesNo(int position) {
        final Dialog dialogNotify = new Dialog(this);
        dialogNotify.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNotify.setContentView(R.layout.dialog_notify_yes_no);

        Window window = dialogNotify.getWindow();
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
        dialogNotify.setCancelable(true);

        TextView tvTitle, tvContent, tvYes, tvNo;
        tvTitle = dialogNotify.findViewById(R.id.tvTitle);
        tvContent = dialogNotify.findViewById(R.id.tvContent);
        tvYes = dialogNotify.findViewById(R.id.tvYes);
        tvNo = dialogNotify.findViewById(R.id.tvNo);

        tvTitle.setText("Xóa bình luận");
        tvContent.setText("Bạn có muốn xóa bình luận không?");

        tvYes.setOnClickListener(view -> {
            deleteComment(position);
            dialogNotify.dismiss();
        });
        tvNo.setOnClickListener(view -> dialogNotify.dismiss());

        dialogNotify.show();
    }

    private void deleteComment(int position) {
        Api.apiInterface().deleteComment(position).enqueue(new Callback<BinhLuan>() {
            @Override
            public void onResponse(@NonNull Call<BinhLuan> call,@NonNull Response<BinhLuan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getCommentsuccess() == 1) {
                        Toast.makeText(CommentListActivity.this, "Bình luận đã được xóa", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                    if (response.body().getCommentsuccess() == 2) {
                        Toast.makeText(CommentListActivity.this, "Lỗi xóa bình luận!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BinhLuan> call,@NonNull Throwable t) {
                Log.e("Err_CommentList", "deleteComment", t);
            }
        });
    }
}

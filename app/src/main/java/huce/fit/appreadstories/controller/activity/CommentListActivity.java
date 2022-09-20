package huce.fit.appreadstories.controller.activity;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.controller.adapters.CommentAdapter;
import huce.fit.appreadstories.model.BinhLuan;
import huce.fit.appreadstories.model.TaiKhoan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentListActivity extends AppCompatActivity {

    private List<BinhLuan> listComment = new ArrayList<>(); //data source
    private CommentAdapter commentAdapter;
    private RecyclerView rcViewComment;
    private ImageView ivBack, ivSent;
    private EditText etComment;
    private int idStory, idAccount, idComment;

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

        getData();
        rcView();
        processEvents();
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("CheckLogin", MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idAccount_Comment", String.valueOf(idAccount));
    }

    private void rcView() {
        commentAdapter = new CommentAdapter(listComment, (position, view) -> {
            Log.e("position_cmt", String.valueOf(position));
            idComment = position;
            checkCommentOfAccount();
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
            public void onResponse(Call<List<BinhLuan>> call, Response<List<BinhLuan>> response) {
                if (response.isSuccessful()) {
                    listComment.clear();
                    listComment.addAll(response.body());
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BinhLuan>> call, Throwable t) {
                Toast.makeText(CommentListActivity.this, "Lỗi hiển thị bình luận!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processEvents() {
        ivBack.setOnClickListener(v -> {
            finish();
        });
        ivSent.setOnClickListener(v -> {
            if (etComment.length() > 1200) {
                Toast.makeText(CommentListActivity.this, "Quá số lượng từ giới hạn " + etComment.length() + "/840!", Toast.LENGTH_SHORT).show();
            } else {
                addComment();
                etComment.setText("");
            }
        });
    }

    private void addComment() {
        String comment = etComment.getText().toString();
        Api.apiInterface().getAccount(idAccount).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Api.apiInterface().addCommnet(idStory, idAccount, response.body().getTenhienthi(), comment).enqueue(new Callback<BinhLuan>() {
                        @Override
                        public void onResponse(Call<BinhLuan> call1, Response<BinhLuan> response1) {
                            if (response1.isSuccessful() && response1.body() != null) {
                                if (response1.body().getSuccess() == 1) {
                                    getData();
                                } else {
                                    Toast.makeText(CommentListActivity.this, "Lỗi! Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<BinhLuan> call1, Throwable t1) {
                            Log.e("Err_addComment", t1.toString());
                            Toast.makeText(CommentListActivity.this, "Lỗi đường dẫn đăng bình luận!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Toast.makeText(CommentListActivity.this, "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkCommentOfAccount() {
        Api.apiInterface().checkCommentOfAccount(idComment, idAccount).enqueue(new Callback<BinhLuan>() {
            @Override
            public void onResponse(Call<BinhLuan> call, Response<BinhLuan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        openDialogCommentUpdateDelete();
                    }
                }
            }

            @Override
            public void onFailure(Call<BinhLuan> call, Throwable t) {
                Log.e("Err_CommentAdapter", t.toString());
            }
        });
    }

    private void openDialogCommentUpdateDelete() {
        final Dialog dialogCommentDelete = new Dialog(this);
        dialogCommentDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCommentDelete.setContentView(R.layout.layout_dialog_comment_update_delete);

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
//            deleteComment(position);
            dialogCommentDelete.dismiss();
        });
        tvDelete.setOnClickListener(view -> {
            openDialogCommentNotifyDelete();
            dialogCommentDelete.dismiss();
        });

        dialogCommentDelete.show();
    }

    private void openDialogCommentNotifyDelete() {
        final Dialog dialogCommentDelete = new Dialog(this);
        dialogCommentDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCommentDelete.setContentView(R.layout.layout_dialog_comment_notify_delete);

        Window window = dialogCommentDelete.getWindow();
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
        dialogCommentDelete.setCancelable(true);

        TextView tvYes = dialogCommentDelete.findViewById(R.id.tvYes);
        TextView tvNo = dialogCommentDelete.findViewById(R.id.tvNo);

        tvYes.setOnClickListener(view -> {
            deleteComment();
            dialogCommentDelete.dismiss();
        });
        tvNo.setOnClickListener(view -> {
            dialogCommentDelete.dismiss();
        });

        dialogCommentDelete.show();
    }

    private void deleteComment() {
        Api.apiInterface().deleteComment(idComment).enqueue(new Callback<BinhLuan>() {
            @Override
            public void onResponse(Call<BinhLuan> call, Response<BinhLuan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getSuccess() == 1) {
                        Toast.makeText(CommentListActivity.this, "Bình luận đã được xóa", Toast.LENGTH_SHORT).show();
                        getData();
                    }
                    if (response.body().getSuccess() == 2) {
                        Toast.makeText(CommentListActivity.this, "Lỗi xóa bình luận!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<BinhLuan> call, Throwable t) {
                Toast.makeText(CommentListActivity.this, "lỗi đuòng đẫn xóa bình luận!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

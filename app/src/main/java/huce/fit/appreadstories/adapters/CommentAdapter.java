package huce.fit.appreadstories.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.model.BinhLuan;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHoder> {
    private List<BinhLuan> listComment;
    private ClickListener clickListener;


    public CommentAdapter(List<BinhLuan> listComment, ClickListener clickListener) {
        this.listComment = listComment;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CommentHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_comment_item, parent, false);
        return new CommentHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHoder holder, int position) {
        BinhLuan bl = listComment.get(position);
        if (bl == null) {
            return;
        } else {
            holder.tvName.setText(bl.getTenhienthi());
            holder.tvComment.setText(bl.getBinhluan());
        }
    }

    @Override
    public int getItemCount() {
        if (listComment != null && listComment.size() > 0)
            return listComment.size();
        else
            return 0;
    }

    public class CommentHoder extends RecyclerView.ViewHolder {
        private TextView tvName, tvComment;
        private ConstraintLayout constraintLayoutCommentItem;

        public CommentHoder(@NonNull View itemView) {
            super(itemView);
            constraintLayoutCommentItem = itemView.findViewById(R.id.constraintLayoutCommentItem);
            tvName = itemView.findViewById(R.id.tvName);
            tvComment = itemView.findViewById(R.id.tvComment);

            //rút ngắn 1 đoạn bình luận dài
            tvComment.setOnClickListener(view -> {
                if (tvComment.getLineCount() > 4) {
                    Log.e("LineCount1", String.valueOf(tvComment.getLineCount()));
                    tvComment.setMaxLines(4);
                    Log.e("LineCount2", String.valueOf(tvComment.length()));
                }
                if (tvComment.getLineCount() == 4) {
                    tvComment.setMaxLines(31);
                }
            });

            constraintLayoutCommentItem.setOnLongClickListener(view -> {
                int position = getBindingAdapterPosition();
                int idComment = listComment.get(position).getMabinhluan();
                clickListener.onItemClick(idComment, view);
                return false;
            });
        }
    }
}

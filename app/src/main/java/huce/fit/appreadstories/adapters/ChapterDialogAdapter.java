package huce.fit.appreadstories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.model.ChuongTruyen;

public class ChapterDialogAdapter extends RecyclerView.Adapter<ChapterDialogAdapter.ChapterDialogHoder> {
    private List<ChuongTruyen> listChapter;
    private List<ChuongTruyen> listChapterOld;
    private ClickListener clickListener;

    public ChapterDialogAdapter(List<ChuongTruyen> listChapter, ClickListener clickListener) {
        this.listChapter = listChapter;
        this.listChapterOld = listChapter;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ChapterDialogHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_chapter_item, parent, false);
        return new ChapterDialogHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterDialogHoder holder, int position) {
        ChuongTruyen ct = listChapter.get(position);
        if (ct == null) {
            return;
        } else {
            holder.tvNumberChapter.setText(ct.getSochuong());
            holder.tvChapter.setText(ct.getTenchuong());
        }
    }

    @Override
    public int getItemCount() {
        if (listChapter != null && listChapter.size() > 0)
            return listChapter.size();
        else
            return 0;
    }

    public class ChapterDialogHoder extends RecyclerView.ViewHolder {
        private TextView tvNumberChapter, tvChapter;

        public ChapterDialogHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tvChapter = itemView.findViewById(R.id.tvChapter);

            itemView.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                int idChapter = listChapter.get(position).getMachuong();
                clickListener.onItemClick(idChapter, view);
            });
        }
    }
}

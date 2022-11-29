package huce.fit.appreadstories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.model.ChuongTruyen;

public class ChapterBasicAdapter extends RecyclerView.Adapter<ChapterBasicAdapter.ChapterBasicHoder>{
    private List<ChuongTruyen> listChapter;

    public ChapterBasicAdapter(List<ChuongTruyen> listChapter) {
        this.listChapter = listChapter;
    }

    @NonNull
    @Override
    public ChapterBasicAdapter.ChapterBasicHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chapter_item, parent, false);
        return new ChapterBasicAdapter.ChapterBasicHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterBasicAdapter.ChapterBasicHoder holder, int position) {
        ChuongTruyen ct = listChapter.get(position);
        if (ct == null) {
            return;
        } else {
            holder.tvNumberChapter.setText(ct.getSochuong());
            holder.tvChapter.setText(ct.getTenchuong());
            holder.tvPostDay.setText(ct.getThoigiandang());
        }
    }

    @Override
    public int getItemCount() {
        if (listChapter != null && listChapter.size() > 0)
            return listChapter.size();
        else
            return 0;
    }



    public class ChapterBasicHoder extends RecyclerView.ViewHolder {
        private TextView tvNumberChapter, tvChapter, tvPostDay;

        public ChapterBasicHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvPostDay = itemView.findViewById(R.id.tvPostDay);
        }
    }
}

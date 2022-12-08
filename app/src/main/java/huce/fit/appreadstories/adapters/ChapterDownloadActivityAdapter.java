package huce.fit.appreadstories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.model.ChuongTruyen;

public class ChapterDownloadActivityAdapter extends RecyclerView.Adapter<ChapterDownloadActivityAdapter.ChapterBasicHoder> {
    private final List<ChuongTruyen> listChapter;

    public ChapterDownloadActivityAdapter(List<ChuongTruyen> listChapter) {
        this.listChapter = listChapter;
    }

    @NonNull
    @Override
    public ChapterDownloadActivityAdapter.ChapterBasicHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chapter_item, parent, false);
        return new ChapterDownloadActivityAdapter.ChapterBasicHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterDownloadActivityAdapter.ChapterBasicHoder holder, int position) {
        ChuongTruyen ct = listChapter.get(position);
        if (ct != null) {
            holder.tvNumberChapter.setText(String.format(Locale.getDefault(), "%s.", ct.getSochuong()));
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

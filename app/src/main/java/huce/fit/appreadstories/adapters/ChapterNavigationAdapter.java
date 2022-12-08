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
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.model.ChuongTruyen;

public class ChapterNavigationAdapter extends RecyclerView.Adapter<ChapterNavigationAdapter.ChapterDownloadHoder> {
    private final List<ChuongTruyen> listChapter;
    private final ClickListener clickListener;

    public ChapterNavigationAdapter(List<ChuongTruyen> listChapter, ClickListener clickListener) {
        this.listChapter = listChapter;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ChapterDownloadHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_chapter_list_item, parent, false);
        return new ChapterDownloadHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterDownloadHoder holder, int position) {
        ChuongTruyen ct = listChapter.get(position);
        holder.itemView.setOnClickListener(view -> {
            int idChapter = listChapter.get(position).getMachuong();
            clickListener.onItemClick(idChapter, view, false);
        });
        if (ct != null) {
            holder.tvNumberChapter.setText(String.format(Locale.getDefault(), "%s.", ct.getSochuong()));
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

    public class ChapterDownloadHoder extends RecyclerView.ViewHolder {
        private TextView tvNumberChapter, tvChapter;

        public ChapterDownloadHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tvChapter = itemView.findViewById(R.id.tvChapter);
        }
    }
}

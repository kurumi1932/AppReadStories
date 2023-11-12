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
import huce.fit.appreadstories.sqlite.Chapter;

public class ChapterNavigationDownloadAdapter extends RecyclerView.Adapter<ChapterNavigationDownloadAdapter.ChapterNavigationDownloadHoder> {
    private final List<Chapter> listChapter;
    private final ClickListener clickListener;

    public ChapterNavigationDownloadAdapter(List<Chapter> listChapter, ClickListener clickListener) {
        this.listChapter = listChapter;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ChapterNavigationDownloadAdapter.ChapterNavigationDownloadHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.nav_chapter_list_item, parent, false);
        return new ChapterNavigationDownloadAdapter.ChapterNavigationDownloadHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterNavigationDownloadAdapter.ChapterNavigationDownloadHoder holder, int position) {
        Chapter c = listChapter.get(position);
        holder.itemView.setOnClickListener(view -> {
            int idChapter = listChapter.get(position).getIdChapter();
            clickListener.onItemClick(idChapter, view, false);
        });
        if (c != null) {
            holder.tvNumberChapter.setText(String.format(Locale.getDefault(), "%s.", c.getNumberChapter()));
            holder.tvChapter.setText(c.getNameChapter());
        }
    }

    @Override
    public int getItemCount() {
        if (listChapter != null && listChapter.size() > 0)
            return listChapter.size();
        else
            return 0;
    }

    public class ChapterNavigationDownloadHoder extends RecyclerView.ViewHolder {
        private TextView tvNumberChapter, tvChapter;

        public ChapterNavigationDownloadHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tvChapter = itemView.findViewById(R.id.tvChapter);
        }
    }
}

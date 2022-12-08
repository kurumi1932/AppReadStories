package huce.fit.appreadstories.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.sqlite.Chapter;
import huce.fit.appreadstories.sqlite.ChapterRead;

public class ChapterDownloadAdapter extends RecyclerView.Adapter<ChapterDownloadAdapter.ChapterDownloadHoder> {
    private final List<Chapter> listChapter;
    private final List<ChapterRead> listChapterReadOld;
    private final List<ChapterRead> listChapterRead = new ArrayList<>();
    private final ClickListener clickListener;
    private final Context context;
    private final int idChapterReading;

    public ChapterDownloadAdapter(Context context, List<Chapter> listChapter, List<ChapterRead> listChapterRead, int idChapterReading, ClickListener clickListener) {
        this.context = context;
        this.listChapter = listChapter;
        this.listChapterReadOld = listChapterRead;
        this.idChapterReading = idChapterReading;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ChapterDownloadAdapter.ChapterDownloadHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chapter_item, parent, false);
        return new ChapterDownloadAdapter.ChapterDownloadHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterDownloadAdapter.ChapterDownloadHoder holder, int position) {
        int count = 0;
        holder.itemView.setOnClickListener(view -> {
            int idChapter = listChapter.get(position).getIdChapter();
            clickListener.onItemClick(idChapter, view, false);
        });

        if (listChapterRead.size() == 0) {
            if (count == 0) {
                for (ChapterRead cr : listChapterReadOld) {
                    if (cr.getIdChapter() != idChapterReading) {
                        listChapterRead.add(cr);
                    }
                }
            } else {
                listChapterRead.clear();
            }
        }

        Chapter c = listChapter.get(position);
        if (c != null) {
            holder.tvNumberChapter.setText(String.format(Locale.getDefault(), "%s.", c.getNumberChapter()));
            holder.tvChapter.setText(c.getNameChapter());
            holder.tvPostDay.setText(c.getPostDay());

            int idChapter = c.getIdChapter();

            if (idChapter == idChapterReading) {
                holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.orange));
                holder.tvChapter.setTextColor(context.getResources().getColor(R.color.orange));
                holder.tvPostDay.setTextColor(context.getResources().getColor(R.color.orange));
            }

            //so sánh mã chương trong danh sách truyện đã đọc với mã chương
            if (listChapterRead.size() > 0 && idChapterReading > 0) {
                for (int i = 0; i < listChapterRead.size(); i++) {
                    int idChapterRead = listChapterRead.get(i).getIdChapter();

                    if (idChapterRead == idChapter) {
                        holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.dim_gray));
                        holder.tvChapter.setTextColor(context.getResources().getColor(R.color.dim_gray));
                        holder.tvPostDay.setTextColor(context.getResources().getColor(R.color.dim_gray));

                        listChapterRead.remove(listChapterRead.get(i));
                        count++;
                        break;
                    }
                    if (idChapterRead != idChapter && idChapterReading != idChapter) {
                        holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.black));
                        holder.tvChapter.setTextColor(context.getResources().getColor(R.color.black));
                        holder.tvPostDay.setTextColor(context.getResources().getColor(R.color.black));
                    }
                }
            }
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
        private TextView tvNumberChapter, tv, tvChapter, tvPostDay;

        public ChapterDownloadHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvPostDay = itemView.findViewById(R.id.tvPostDay);
        }
    }
}

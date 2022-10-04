package huce.fit.appreadstories.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.model.ChuongTruyen;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterHoder> implements Filterable {
    private List<ChuongTruyen> listChapter;
    private List<ChuongTruyen> listChapterOld;
    private List<ChuongTruyen> listChapterRead;
    private ClickListener clickListener;
    private Context context;
    private int chapterRead;

    public ChapterAdapter(Context context, List<ChuongTruyen> listChapter, List<ChuongTruyen> listChapterRead, int chapterRead, ClickListener clickListener) {
        this.context = context;
        this.listChapter = listChapter;
        this.listChapterOld = listChapter;
        this.listChapterRead = listChapterRead;
        this.chapterRead = chapterRead;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ChapterHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chapter_item, parent, false);
        return new ChapterHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterHoder holder, int position) {
        ChuongTruyen ct = listChapter.get(position);
        if (ct == null) {
            return;
        } else {
            holder.tvNumberChapter.setText(ct.getSochuong());
            holder.tvChapter.setText(ct.getTenchuong());
            holder.tvPostDay.setText(ct.getThoigiandang());

            //so sánh mã chương trong danh sách truyện đã đọc với mã chương
            if (listChapterRead.size() > 0) {
                for (int i = 0; i < listChapterRead.size(); i++) {
//                    Log.e("listChapter", String.valueOf(listChapterRead.get(i).getMachuong()));
//                    Log.e("idStory", String.valueOf(ct.getMachuong()));
                    if (listChapterRead.get(i).getMachuong() == ct.getMachuong()) {
                        holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.dim_gray));
                        holder.tv.setTextColor(context.getResources().getColor(R.color.dim_gray));
                        holder.tvChapter.setTextColor(context.getResources().getColor(R.color.dim_gray));

                        listChapterRead.remove(listChapterRead.get(i));
                        break;
                    }
                }
            }

            if (chapterRead > 0) {
//                Log.e("chapterRead", String.valueOf(chapterRead));
//                Log.e("listChapter", String.valueOf(ct.getMachuong()));
                if (ct.getMachuong() == chapterRead) {
                    holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.orange));
                    holder.tv.setTextColor(context.getResources().getColor(R.color.orange));
                    holder.tvChapter.setTextColor(context.getResources().getColor(R.color.orange));
                    holder.tvPostDay.setTextColor(context.getResources().getColor(R.color.orange));
                    chapterRead = 0;
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

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String strSearch = constraint.toString();
                if (strSearch.isEmpty()) {
                    listChapter = listChapterOld;
                } else {
                    List<ChuongTruyen> listChapterNew = new ArrayList<>();
                    for (ChuongTruyen chuong : listChapterOld) {
                        if (chuong.getTenchuong().toLowerCase().contains(strSearch.toLowerCase(Locale.ROOT))) {
                            listChapterNew.add(chuong);
                        }
                    }
                    listChapter = listChapterNew;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listChapter;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listChapter = (List<ChuongTruyen>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ChapterHoder extends RecyclerView.ViewHolder {
        private TextView tvNumberChapter, tv, tvChapter, tvPostDay;

        public ChapterHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tv = itemView.findViewById(R.id.tv);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvPostDay = itemView.findViewById(R.id.tvPostDay);

            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getBindingAdapterPosition();
                    String name = listChapter.get(position).getTenchuong();
                    for (int i = 0; i < listChapterOld.size(); i++) {
                        if (listChapterOld.get(i).getTenchuong().equals(name)) {
                            position = i;
                            break;
                        }
                    }
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(position, itemView);
                    }
                }
            });
        }
    }
}

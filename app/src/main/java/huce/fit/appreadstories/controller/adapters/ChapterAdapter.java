package huce.fit.appreadstories.controller.adapters;

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
    private ClickListener clickListener;

    public ChapterAdapter(List<ChuongTruyen> listChapter, ClickListener clickListener) {
        this.listChapter = listChapter;
        this.listChapterOld = listChapter;
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
        private TextView tvNumberChapter, tvChapter, tvPostDay;

        public ChapterHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvPostDay = itemView.findViewById(R.id.tvPostDay);

            itemView.setOnClickListener(v -> {
                if (clickListener !=null){
                    int position = getBindingAdapterPosition();
                    String name = listChapter.get(position).getTenchuong();
                    for (int i=0 ; i <listChapterOld.size() ; i++ ){
                        if(listChapterOld.get(i).getTenchuong().equals(name)){
                            position = i;
                            break;
                        }
                    }
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(position,itemView);
                    }
                }
            });
        }
    }
}

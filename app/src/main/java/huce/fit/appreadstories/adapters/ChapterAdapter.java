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

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.model.ChuongTruyen;

public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterHoder> {
    private List<ChuongTruyen> listChapter;
    private List<ChuongTruyen> listChapterReadOld;
    private List<ChuongTruyen> listChapterRead = new ArrayList<>();
    private ClickListener clickListener;
    private Context context;
    private int idChapterReading, count;

    public ChapterAdapter(Context context, List<ChuongTruyen> listChapter, List<ChuongTruyen> listChapterRead, int idChapterReading, ClickListener clickListener) {
        this.context = context;
        this.listChapter = listChapter;
        this.listChapterReadOld = listChapterRead;
        this.idChapterReading = idChapterReading;
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
        count = 0;
        if (listChapterRead.size() == 0 && count == 0) {
            for (ChuongTruyen c : listChapterReadOld) {
                if (c.getMachuong() != idChapterReading) {
                    listChapterRead.add(c);
                }
            }
        }

        if(listChapterRead.size() == 0 && count != 0){
            listChapterRead.clear();
        }

        ChuongTruyen ct = listChapter.get(position);
        if (ct == null) {
            return;
        } else {
            holder.tvNumberChapter.setText(ct.getSochuong());
            holder.tvChapter.setText(ct.getTenchuong());
            holder.tvPostDay.setText(ct.getThoigiandang());
//            Log.e("listChapterRead1:", String.valueOf(listChapterRead));
//            Log.e("idChapterReading:", String.valueOf(idChapterReading));
//            Log.e("idChapter:", String.valueOf(ct.getMachuong()));

            int idChapter = ct.getMachuong();

            if (idChapter == idChapterReading) {
                holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.orange));
                holder.tv.setTextColor(context.getResources().getColor(R.color.orange));
                holder.tvChapter.setTextColor(context.getResources().getColor(R.color.orange));
                holder.tvPostDay.setTextColor(context.getResources().getColor(R.color.orange));
            }

            //so sánh mã chương trong danh sách truyện đã đọc với mã chương
            if (listChapterRead.size() > 0 && idChapterReading > 0) {
                for (int i = 0; i < listChapterRead.size(); i++) {
                    int idChapterRead = listChapterRead.get(i).getMachuong();
//                    Log.e("listChapterRead2:", String.valueOf(idChapterRead));

                    if (idChapterRead == idChapter) {
                        holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.dim_gray));
                        holder.tv.setTextColor(context.getResources().getColor(R.color.dim_gray));
                        holder.tvChapter.setTextColor(context.getResources().getColor(R.color.dim_gray));
                        holder.tvPostDay.setTextColor(context.getResources().getColor(R.color.dim_gray));

                        listChapterRead.remove(listChapterRead.get(i));
                        count++;
//                        Log.e("if:", "1");
                        break;
                    }
                    if(idChapterRead != idChapter && idChapterReading != idChapter){
                        holder.tvNumberChapter.setTextColor(context.getResources().getColor(R.color.black));
                        holder.tv.setTextColor(context.getResources().getColor(R.color.black));
                        holder.tvChapter.setTextColor(context.getResources().getColor(R.color.black));
                        holder.tvPostDay.setTextColor(context.getResources().getColor(R.color.black));
//                        Log.e("if:", "2");
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

    public class ChapterHoder extends RecyclerView.ViewHolder {
        private TextView tvNumberChapter, tv, tvChapter, tvPostDay;

        public ChapterHoder(@NonNull View itemView) {
            super(itemView);
            tvNumberChapter = itemView.findViewById(R.id.tvNumberChapter);
            tv = itemView.findViewById(R.id.tv);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            tvPostDay = itemView.findViewById(R.id.tvPostDay);

            itemView.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                int idChapter = listChapter.get(position).getMachuong();
                clickListener.onItemClick(idChapter, view);
            });
        }
    }
}

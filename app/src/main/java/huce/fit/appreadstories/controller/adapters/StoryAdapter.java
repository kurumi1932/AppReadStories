package huce.fit.appreadstories.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.model.Truyen;


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHoder> implements Filterable {
    private List<Truyen> listStory;
    private List<Truyen> listStoryOld;
    private ClickListener clickListener;


    public StoryAdapter(List<Truyen> listStory, ClickListener clickListener) {
        this.listStoryOld = listStory;
        this.listStory = listStory;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public StoryHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_story_item, parent, false);
        return new StoryHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryHoder holder, int position) {
        Truyen tc = listStory.get(position);
        if (tc == null) {
            return;
        } else {
            holder.tvStoryName.setText(tc.getTentruyen());
            holder.tvAuthor.setText(tc.getTacgia());
            holder.tvStatus.setText(tc.getTrangthai());
            holder.tvChapter.setText(String.valueOf(tc.getSochuong()));
            Picasso.get().load(tc.getAnh())
                    .into(holder.ivStory);
        }
    }

    @Override
    public int getItemCount() {
        if (listStory != null && listStory.size() > 0)
            return listStory.size();
        else
            return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()) {
                    listStory = listStoryOld;
                } else {
                    List<Truyen> listStoryNew = new ArrayList<>();
                    for (Truyen tc : listStoryOld) {
                        if (tc.getTentruyen().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            listStoryNew.add(tc);
                        }
                    }
                    listStory = listStoryNew;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listStory;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listStory = (List<Truyen>) results.values;
            }
        };
    }

    public class StoryHoder extends RecyclerView.ViewHolder {
        private TextView tvStoryName, tvAuthor, tvStatus, tvChapter;
        private ImageView ivStory;

        public StoryHoder(@NonNull View itemView) {
            super(itemView);
            tvStoryName = itemView.findViewById(R.id.tvStoryName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            ivStory = itemView.findViewById(R.id.ivStory);

            itemView.setOnClickListener((v -> {
                if (clickListener !=null){
                    int position = getBindingAdapterPosition();
                    String name = listStory.get(position).getTentruyen();
                    for (int i=0 ; i <listStoryOld.size() ; i++ ){
                        if(listStoryOld.get(i).getTentruyen().equals(name)){
                            position = i;
                            break;
                        }
                    }
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(position,itemView);
                    }
                }
            }));
        }
    }
}

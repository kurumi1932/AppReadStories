package huce.fit.appreadstories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.model.Truyen;


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHoder> {
    private List<Truyen> listStory;
    private ClickListener clickListener;


    public StoryAdapter(List<Truyen> listStory, ClickListener clickListener) {
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
            holder.tvAge.setText(String.valueOf(tc.getGioihantuoi()));
            holder.tvStatus.setText(tc.getTrangthai());
            holder.tvChapter.setText(String.valueOf(tc.getTongchuong()));
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

    public class StoryHoder extends RecyclerView.ViewHolder {
        private TextView tvStoryName, tvAuthor, tvAge, tvStatus, tvChapter;
        private ImageView ivStory;

        public StoryHoder(@NonNull View itemView) {
            super(itemView);
            tvStoryName = itemView.findViewById(R.id.tvStoryName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            ivStory = itemView.findViewById(R.id.ivStory);

            itemView.setOnClickListener((view -> {
                int position = getBindingAdapterPosition();
                int idStory = listStory.get(position).getMatruyen();
                clickListener.onItemClick(idStory, view);
            }));
        }
    }
}

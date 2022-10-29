package huce.fit.appreadstories.controller.adapters;

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
import huce.fit.appreadstories.sqlite.Story;


public class StoryDownloadAdapter extends RecyclerView.Adapter<StoryDownloadAdapter.StoryDownloadHoder> {
    private List<Story> listStoryDownload;
    private ClickListener clickListener;


    public StoryDownloadAdapter(List<Story> listStoryDownload, ClickListener clickListener) {
        this.listStoryDownload = listStoryDownload;
        this.clickListener = clickListener;
    }


    @NonNull
    @Override
    public StoryDownloadHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_story_item, parent, false);
        return new StoryDownloadHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryDownloadHoder holder, int position) {
        Story story = listStoryDownload.get(position);
        if (story == null) {
            return;
        } else {
            holder.tvStoryName.setText(story.getNameStory());
            holder.tvAuthor.setText(story.getAuthor());
            holder.tvStatus.setText(story.getStatus());
            holder.tvChapter.setText(String.valueOf(story.getSumChapter()));
            Picasso.get().load(story.getImage())
                    .into(holder.ivStory);
        }
    }

    @Override
    public int getItemCount() {
        if (listStoryDownload != null && listStoryDownload.size() > 0)
            return listStoryDownload.size();
        else
            return 0;
    }


    public class StoryDownloadHoder extends RecyclerView.ViewHolder {
        private TextView tvStoryName, tvAuthor, tvStatus, tvChapter;
        private ImageView ivStory;

        public StoryDownloadHoder(@NonNull View itemView) {
            super(itemView);
            tvStoryName = itemView.findViewById(R.id.tvStoryName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            ivStory = itemView.findViewById(R.id.ivStory);

            itemView.setOnClickListener((v -> {
                int position = getBindingAdapterPosition();
                int idStory = listStoryDownload.get(position).getIdStory();
                clickListener.onItemClick(idStory, v);
            }));
        }
    }
}

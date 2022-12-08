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
import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.interfaces.ClickListener;
import huce.fit.appreadstories.sqlite.Story;


public class StoryDownloadAdapter extends RecyclerView.Adapter<StoryDownloadAdapter.StoryDownloadHoder> {
    private final List<Story> listStoryDownload;
    private final ClickListener clickListener;

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
        if (story != null) {
            holder.itemView.setOnClickListener(view -> {
                int idStory = listStoryDownload.get(position).getIdStory();
                clickListener.onItemClick(idStory, view, false);
            });
            holder.itemView.setOnLongClickListener(view -> {
                int idStory = listStoryDownload.get(position).getIdStory();
                clickListener.onItemClick(idStory, view, true);
                return false;
            });

            holder.tvStoryName.setText(story.getNameStory());
            holder.tvAuthor.setText(story.getAuthor());
            holder.tvAge.setText(String.valueOf(story.getAge()));
            holder.tvStatus.setText(story.getStatus());
            holder.tvChapter.setText(String.valueOf(story.getSumChapter()));
            holder.tvNewChapter.setText(String.format(Locale.getDefault(), "Có %d chương mới chưa tải", story.getNewChapter()));
            if (story.getNewChapter() > 0) {
                holder.tvNewChapter.setVisibility(View.VISIBLE);
            } else {
                holder.tvNewChapter.setVisibility(View.GONE);
            }
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
        private TextView tvStoryName, tvAuthor, tvAge, tvStatus, tvChapter, tvNewChapter;
        private ImageView ivStory;

        public StoryDownloadHoder(@NonNull View itemView) {
            super(itemView);
            tvStoryName = itemView.findViewById(R.id.tvStoryName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            ivStory = itemView.findViewById(R.id.ivStory);
            tvNewChapter = itemView.findViewById(R.id.tvNewChapter);
        }
    }
}

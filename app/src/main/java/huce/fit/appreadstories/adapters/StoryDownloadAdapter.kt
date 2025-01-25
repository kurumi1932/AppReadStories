package huce.fit.appreadstories.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.sqlite.Story;


public class StoryDownloadAdapter extends RecyclerView.Adapter<StoryDownloadAdapter.StoryDownloadHolder> {
    private final List<Story> mListStory = new ArrayList<>();
    private final ClickListener clickListener;

    public StoryDownloadAdapter(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataStory(List<Story> listStory) {
        mListStory.clear();
        mListStory.addAll(listStory);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StoryDownloadHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_story_item, parent, false);
        return new StoryDownloadHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryDownloadHolder holder, int position) {
        Story story = mListStory.get(position);
        if (story != null) {
            holder.itemView.setOnClickListener(view -> {
                int idStory = mListStory.get(position).getIdStory();
                clickListener.onItemClick(idStory, view, false);
            });
            holder.itemView.setOnLongClickListener(view -> {
                int idStory = mListStory.get(position).getIdStory();
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
        if (mListStory != null && mListStory.size() > 0)
            return mListStory.size();
        else
            return 0;
    }


    public static class StoryDownloadHolder extends RecyclerView.ViewHolder {
        private final TextView tvStoryName, tvAuthor, tvAge, tvStatus, tvChapter, tvNewChapter;
        private final ImageView ivStory;

        public StoryDownloadHolder(@NonNull View itemView) {
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

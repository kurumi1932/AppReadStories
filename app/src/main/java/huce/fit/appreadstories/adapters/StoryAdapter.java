package huce.fit.appreadstories.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
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

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.model.Truyen;


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryHolder> {
    private final List<Truyen> mListStory = new ArrayList<>();
    private final ClickListener mClickListener;


    public StoryAdapter(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataStory(List<Truyen> listStory) {
        mListStory.clear();
        mListStory.addAll(listStory);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_story_item, parent, false);
        return new StoryHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryHolder holder, int position) {
        Truyen tc = mListStory.get(position);
        holder.itemView.setOnClickListener((view -> {
            int idStory = mListStory.get(position).getMatruyen();
            mClickListener.onItemClick(idStory, view, false);
        }));

        if (tc != null) {
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
        Log.e("StoryAdapter","NHT itemCount= "+mListStory.size());
        return mListStory.size();
    }

    public static class StoryHolder extends RecyclerView.ViewHolder {
        private final TextView tvStoryName, tvAuthor, tvAge, tvStatus, tvChapter;
        private final ImageView ivStory;

        public StoryHolder(@NonNull View itemView) {
            super(itemView);
            tvStoryName = itemView.findViewById(R.id.tvStoryName);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvChapter = itemView.findViewById(R.id.tvChapter);
            ivStory = itemView.findViewById(R.id.ivStory);
        }
    }
}

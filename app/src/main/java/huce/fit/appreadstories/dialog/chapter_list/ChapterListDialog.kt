package huce.fit.appreadstories.dialog.chapter_list;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.adapters.ChapterDialogAdapter;
import huce.fit.appreadstories.chapter.information.ChapterInformationView;
import huce.fit.appreadstories.dialog.BaseDialog;
import huce.fit.appreadstories.model.Chapter;
import huce.fit.appreadstories.model.Story;

public class ChapterListDialog extends BaseDialog implements ChapterListDialogView {

    private final ChapterInformationView mChapterInformationView;
    private ChapterDialogAdapter mChapterDialogAdapter;
    private ImageView ivStory, ivReverse, ivClose;
    private TextView tvStoryName, tvAuthor;
    private SearchView svChapter;
    private RecyclerView rcViewChapter;
    private LinearLayoutManager mLinearLayoutManager;

    public ChapterListDialog(ChapterInformationView chapterInformationView, Context context) {
        super(context);
        mChapterInformationView = chapterInformationView;
        setDialog(R.layout.dialog_chapter_list);
        setWindow(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT, Gravity.START, 0);
        init();
        processEvents();
    }

    private void init() {
        ivStory = mDialog.findViewById(R.id.ivStory);
        ivReverse = mDialog.findViewById(R.id.ivReverse);
        ivClose = mDialog.findViewById(R.id.ivClose);
        tvStoryName = mDialog.findViewById(R.id.tvStoryName);
        tvAuthor = mDialog.findViewById(R.id.tvAuthor);
        svChapter = mDialog.findViewById(R.id.svChapter);
        rcViewChapter = mDialog.findViewById(R.id.rcChapterListOnChapterRead);

        mChapterDialogAdapter = new ChapterDialogAdapter();
        mLinearLayoutManager = new LinearLayoutManager(mContext);
        rcViewChapter.setLayoutManager(mLinearLayoutManager);
        rcViewChapter.setAdapter(mChapterDialogAdapter);
        ChapterListDialogPresenter mChapterListDialogPresenter = new ChapterListDialogImpl(mChapterInformationView, this);
        mChapterListDialogPresenter.getChapterList();
    }

    @Override
    public void setChapterList(List<Chapter> chapterList) {
        mChapterDialogAdapter.setChapterList(chapterList);
    }

    @Override
    public void setData(Story story) {
        tvStoryName.setText(story.getStoryName());
        tvAuthor.setText(story.getAuthor());
        Picasso.get().load(story.getImage()).into(ivStory);
    }

    private void processEvents() {
        mChapterDialogAdapter.clickListener((position, view1, isLongClick) -> mChapterInformationView.moveChapter(position));

        ivReverse.setOnClickListener(view -> {
            if (mLinearLayoutManager != null) {
                mChapterDialogAdapter.reverseData();
                rcViewChapter.setAdapter(mChapterDialogAdapter);
            }
        });

        svChapter.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mChapterDialogAdapter.getFilter().filter(newText);
                return false;
            }
        });

        ivClose.setOnClickListener(view -> mDialog.dismiss());
    }
}

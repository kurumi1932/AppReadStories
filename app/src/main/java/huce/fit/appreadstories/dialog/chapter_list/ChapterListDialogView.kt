package huce.fit.appreadstories.dialog.chapter_list;

import java.util.List;

import huce.fit.appreadstories.dialog.BaseDialogView;
import huce.fit.appreadstories.model.Chapter;
import huce.fit.appreadstories.model.Story;

public interface ChapterListDialogView extends BaseDialogView {
    void setData(Story story);
    void setChapterList(List<Chapter> chapterList);
}

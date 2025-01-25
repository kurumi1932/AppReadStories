package huce.fit.appreadstories.story.list.download;

import java.util.List;

import huce.fit.appreadstories.model.Story;

public interface StoryDownloadView {

    void setData(List<Story> storyList);
    void openStoryDownload();
    void deleteStoryDownload();
}

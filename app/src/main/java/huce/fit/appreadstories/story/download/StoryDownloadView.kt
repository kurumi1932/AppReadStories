package huce.fit.appreadstories.story.download;

import java.util.List;

import huce.fit.appreadstories.sqlite.Story;

public interface StoryDownloadView {

    void setData(List<Story> listStory);
}

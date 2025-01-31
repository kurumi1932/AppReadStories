package huce.fit.appreadstories.story.list.online;

import java.util.List;

import huce.fit.appreadstories.model.Story;

public interface StoryView {

    void getData(int page);
    void setData(List<Story> storyList);
}

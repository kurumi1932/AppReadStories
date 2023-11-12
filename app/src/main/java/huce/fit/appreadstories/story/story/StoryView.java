package huce.fit.appreadstories.story.story;

import java.util.List;

import huce.fit.appreadstories.model.Truyen;

public interface StoryView {

    void hide();
    void getData(int page);
    void setData(List<Truyen> listStory);
}

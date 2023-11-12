package huce.fit.appreadstories.story.story;

import huce.fit.appreadstories.story.BaseListStoryPresenter;

public interface StoryPresenter extends BaseListStoryPresenter {

    void totalPage();
    void getData(int page);
    void loadNextPage();
    boolean isLoading();
    boolean isLastPage();
    void swipeData();
}

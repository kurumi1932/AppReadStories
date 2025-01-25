package huce.fit.appreadstories.story.list.online;

import huce.fit.appreadstories.story.list.BaseStoryListPresenter;

public interface StoryPresenter extends BaseStoryListPresenter {

    void totalPage();
    void getStory(int page);
    void loadNextPage();
    boolean isLoading();
    boolean isLastPage();
    void swipeData();
}

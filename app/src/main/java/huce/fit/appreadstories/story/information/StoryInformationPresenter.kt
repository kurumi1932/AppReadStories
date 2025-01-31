package huce.fit.appreadstories.story.information;

import huce.fit.appreadstories.story.list.BaseStoryListPresenter;

public interface StoryInformationPresenter extends BaseStoryListPresenter {

    void start();
    int getStoryId();
    void showRate(int ratePoint);
    void getDataOffline();
    boolean checkAge();
    void checkInteractive();
    void checkFollowStory();
    void checkFollowStoryDownload();
    void likeStory();
    void followStory();
    void readStory();
    void readStoryDownload();

    void checkRateOfAccount();
    void addRate(int ratePoint, String rate);
    void updateRate(int ratePoint, String rate);
    void deleteRate();
    void enterComment();
    void enterDownload();
    void enterChapter();
    void enterReadStory();
}
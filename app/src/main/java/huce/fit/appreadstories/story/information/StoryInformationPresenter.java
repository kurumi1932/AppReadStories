package huce.fit.appreadstories.story.information;

import huce.fit.appreadstories.story.BaseListStoryPresenter;

public interface StoryInformationPresenter extends BaseListStoryPresenter {

    void checkStoryService();
    int getStoryId();
    void getData();
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
    void updateRate(int rateId, int ratePoint, String rate);
    void deleteRate(int rateId);

    boolean getIsRate();
    boolean enterComment();
    void enterDownload();
    void enterChapter();
    void enterReadStory();
}
package huce.fit.appreadstories.story.information;

import huce.fit.appreadstories.model.DanhGia;
import huce.fit.appreadstories.model.Truyen;
import huce.fit.appreadstories.sqlite.Story;

public interface StoryInformationView {

    void setData(Truyen truyen);
    void setData(Story story);
    void checkInteractive(int isLike, String likeNumber);
    void followStory(int isFollow);
    void getRate(DanhGia danhGia);
    void reloadViewPaper(String text);
}
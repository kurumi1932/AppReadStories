package huce.fit.appreadstories.story.filter;

import huce.fit.appreadstories.story.BaseListStoryPresenter;

public interface StoryFilterPresenter extends BaseListStoryPresenter {

    void setSpeciesId(int mSpeciesId);
    void setStatusId(int mStatusId);
    int getSpeciesId();
    int getStatusId();
    void setSpecies(String species);
    void setStatus(String status);
    void getData();
}

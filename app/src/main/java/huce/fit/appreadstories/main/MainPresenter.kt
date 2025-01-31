package huce.fit.appreadstories.main;

public interface MainPresenter {

    void addFragment(int id);
    void changeFragment(int id);
    int backPressed();
    int getId();
    void removeCount();
}

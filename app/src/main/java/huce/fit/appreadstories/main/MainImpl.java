package huce.fit.appreadstories.main;

import android.content.Context;

import java.util.HashMap;

public class MainImpl implements MainPresenter {

    private static final String TAG = "MainImpl";
    private final HashMap<Integer, Integer> mFragmentOld = new HashMap<>();
    private final int NUMBER = 1;
    int mCount = NUMBER;

    public MainImpl(MainView mainView) {
    }


    @Override
    public void addFragment(int id) {
        mFragmentOld.put(mCount, id);
    }

    @Override
    public void changeFragment(int id) {
        if (mCount == 0) {
            mCount = NUMBER;
        }
        mFragmentOld.put(++mCount, id);
    }

    @Override
    public int backPressed() {
        return --mCount;
    }

    @Override
    public int getId() {
        return mFragmentOld.get(mCount);
    }

    @Override
    public void removeCount() {
        if (mCount != NUMBER) {
            mFragmentOld.remove(mCount);
        }
    }
}

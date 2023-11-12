package huce.fit.appreadstories.main;

import android.content.Context;

import java.util.HashMap;

import huce.fit.appreadstories.checknetwork.CheckNetwork;

public class MainImpl implements MainPresenter{

    private final CheckNetwork mCheckNetwork;
    private final HashMap<Integer, Integer> mFragmentOld = new HashMap<>();
    int mCount=1;

    public MainImpl(MainView context){
        mCheckNetwork = new CheckNetwork((Context) context);
    }

    @Override
    public boolean isNetwork(){
        return mCheckNetwork.isNetwork();
    }

    @Override
    public void addFragment(int id) {
        mFragmentOld.put(mCount, id);
    }

    @Override
    public void changeFragment(int id) {
        if (mCount == 0)
            mCount = 1;
        mFragmentOld.put(++mCount, id);
    }

    @Override
    public int backPressed() {
        return -- mCount;
    }

    @Override
    public int getId() {
        return mFragmentOld.get(mCount);
    }

    @Override
    public void removeCount() {
        if (mCount != 1)
            mFragmentOld.remove(mCount);
    }
}

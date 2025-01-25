package huce.fit.appreadstories.account.login;

import huce.fit.appreadstories.account.BaseAccountPresenter;

public interface AccountPresenter extends BaseAccountPresenter {

    void login(String username, String password);
}

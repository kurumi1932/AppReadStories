package huce.fit.appreadstories.account.register;

import huce.fit.appreadstories.account.BaseAccountPresenter;

public interface AccountRegisterPresenter extends BaseAccountPresenter {

    void register(String username, String password, String email, String name, String birthday);
    void openDatePicker();
}

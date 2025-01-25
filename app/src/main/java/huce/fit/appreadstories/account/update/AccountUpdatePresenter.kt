package huce.fit.appreadstories.account.update;

import huce.fit.appreadstories.account.BaseAccountPresenter;

public interface AccountUpdatePresenter extends BaseAccountPresenter {

    void updateAccount(int idAccount, String email, String name, String birthday);
    void changePassword(int idAccount, String oldPass, String newPass);
    void openDatePicker();
}

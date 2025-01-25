package huce.fit.appreadstories.account.update;

public interface AccountUpdateView {

    void getInfoAccount(String name, String email, String birthday);
    void update(int status);
    void changeDatePicker(String dateStr);
}

package huce.fit.appreadstories.dialog.setting.color;

public interface ColorDialogPresenter {

    void setTextColorPickerDialog();

    void setBackgroundColorPickerDialog();

    void viewTextColor(String hexVal);

    void viewBackgroundColor(String hexVal);

    void setTextColor();

    void setBackgroundColor();
}

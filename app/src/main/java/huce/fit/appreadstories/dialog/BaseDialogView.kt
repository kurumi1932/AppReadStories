package huce.fit.appreadstories.dialog;

public interface BaseDialogView {

    void setWindow(int width, int height, int gravity, int animation);
    void show();
    void dismiss();
}

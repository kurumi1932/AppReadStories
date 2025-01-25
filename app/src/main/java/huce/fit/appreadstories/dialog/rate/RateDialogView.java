package huce.fit.appreadstories.dialog.rate;

import huce.fit.appreadstories.dialog.BaseDialogView;
import huce.fit.appreadstories.model.Rate;

public interface RateDialogView extends BaseDialogView {

    void setData(Rate rate);
    void openConfirmDiaLog(int ratePoint, String rate);
    void noRatePoint();
}

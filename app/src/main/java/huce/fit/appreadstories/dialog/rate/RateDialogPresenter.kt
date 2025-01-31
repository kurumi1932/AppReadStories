package huce.fit.appreadstories.dialog.rate;

import huce.fit.appreadstories.model.Rate;

public interface RateDialogPresenter {

    Rate getRate();
    void setRatePoint(int ratePoint);
    void rate(String rate);
}

package huce.fit.appreadstories.account.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.account.login.AccountLoginActivity;
import huce.fit.appreadstories.account.update.AccountUpdateActivity;
import huce.fit.appreadstories.activity.GuideActivity;
import huce.fit.appreadstories.main.MainActivity;
import huce.fit.appreadstories.main.MainView;


public class AccountManagerFragment extends Fragment implements AccountManagerView {

    private AccountManagerPresenter mAccountManagerPresenter;
    private TextView tvName, tvEditAccount, tvGuide, tvLogOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_manager, container, false);
        init(view);
        processEvents();
        return view;
    }

    private void init(View view) {
        tvName = view.findViewById(R.id.tvName);
        tvEditAccount = view.findViewById(R.id.tvEditAccount);
        tvGuide = view.findViewById(R.id.tvGuide);
        tvLogOut = view.findViewById(R.id.tvLogOut);
        mAccountManagerPresenter = new AccountManagerImpl(this, getActivity());
    }

    @Override
    public void setName(String name) {
        tvName.setText(name);
    }

    private void processEvents() {
        tvEditAccount.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountUpdateActivity.class);
            startActivity(intent);
        });
        tvGuide.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GuideActivity.class);
            startActivity(intent);
        });
        tvLogOut.setOnClickListener(v -> {
            mAccountManagerPresenter.logout();
            Intent intent = new Intent(getActivity(), AccountLoginActivity.class);
            startActivity(intent);

            MainView mainView = new MainActivity();
            mainView.closeMain();
        });
    }
}
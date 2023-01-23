package huce.fit.appreadstories.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.activity.AccountLoginActivity;
import huce.fit.appreadstories.activity.AccountUpdateActivity;
import huce.fit.appreadstories.activity.DeveloperInfoActivity;
import huce.fit.appreadstories.activity.GuideActivity;
import huce.fit.appreadstories.activity.MainActivity;


public class AccountFragment extends Fragment {

    private TextView tvEditAccount, tvGuide, tvDevelopInfo, tvLogOut;
    private int idAccount;
    private String name;
    private final MainActivity main = new MainActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        TextView tvName = view.findViewById(R.id.tvName);
        tvEditAccount = view.findViewById(R.id.tvEditAccount);
        tvGuide = view.findViewById(R.id.tvGuide);
        tvDevelopInfo = view.findViewById(R.id.tvDevelopInfo);
        tvLogOut = view.findViewById(R.id.tvLogOut);

        tvName.setText(name);

        processEvents();

        return view;
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        name = sharedPreferences.getString("name", "");
        Log.e("idFragmentAccount", String.valueOf(idAccount));
    }

    private void processEvents() {
        tvEditAccount.setOnClickListener(v -> { //lambda
            Intent intent = new Intent(getActivity(), AccountUpdateActivity.class);
            intent.putExtra("idAccount", idAccount);
            startActivity(intent);
        });
        tvGuide.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), GuideActivity.class);
            startActivity(intent);
        });
        tvDevelopInfo.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DeveloperInfoActivity.class);
            startActivity(intent);
        });
        tvLogOut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountLoginActivity.class);
            startActivity(intent);

            setSharedPreferences();
            main.closeMainActivity();
        });
    }

    private void setSharedPreferences() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.remove("idAccount");
        edit.apply();
    }
}
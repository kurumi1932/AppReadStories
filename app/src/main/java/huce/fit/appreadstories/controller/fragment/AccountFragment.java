package huce.fit.appreadstories.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.api.Api;
import huce.fit.appreadstories.checknetwork.CheckNetwork;
import huce.fit.appreadstories.controller.activity.AccountLoginActivity;
import huce.fit.appreadstories.controller.activity.AccountUpdateActivity;
import huce.fit.appreadstories.controller.activity.ContactInforActivity;
import huce.fit.appreadstories.controller.activity.MainActivity;
import huce.fit.appreadstories.model.TaiKhoan;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccountFragment extends Fragment {

    private ConstraintLayout clFragmentAccount;
    private TextView tvName, tvEditAccount, tvContactInfor, tvLogOut;
    private Button btCheckConnection;
    private int idAccount;
    private MainActivity main = new MainActivity();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSharedPreferences();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        clFragmentAccount = view.findViewById(R.id.clFragmentAccount);
        tvName = view.findViewById(R.id.tvName);
        tvEditAccount = view.findViewById(R.id.tvEditAccount);
        tvContactInfor = view.findViewById(R.id.tvContactInfor);
        tvLogOut = view.findViewById(R.id.tvLogOut);
        btCheckConnection = view.findViewById(R.id.btCheckConnection);

        if (isNetwork()) {
            show();
        } else {
            hide();
        }
        processEvents();

        return view;
    }

    private void getSharedPreferences() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
        idAccount = sharedPreferences.getInt("idAccount", 0);
        Log.e("idFragmentAccount", String.valueOf(idAccount));
    }

    private boolean isNetwork() {
        CheckNetwork checkNetwork = new CheckNetwork(getActivity());
        return checkNetwork.isNetwork();
    }

    private void show() {
        getAccount();
        clFragmentAccount.setVisibility(View.VISIBLE);
        btCheckConnection.setVisibility(View.GONE);
    }

    private void hide() {
        clFragmentAccount.setVisibility(View.GONE);
        btCheckConnection.setVisibility(View.VISIBLE);
    }

    private void processEvents() {
        tvEditAccount.setOnClickListener(v -> { //lambda
            if (isNetwork()) {
                Intent intent = new Intent(getActivity(), AccountUpdateActivity.class);
                intent.putExtra("idAccount", idAccount);
                startActivity(intent);
            } else {
                hide();
            }
        });
        tvContactInfor.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ContactInforActivity.class);
            startActivity(intent);
        });
        tvLogOut.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), AccountLoginActivity.class);
            startActivity(intent);

            SharedPreferences sh = getActivity().getSharedPreferences("CheckLogin", Context.MODE_PRIVATE);
            SharedPreferences.Editor myedit = sh.edit();
            myedit.remove("idAccount");
            myedit.commit();

            main.closeMainActivity();
        });

        btCheckConnection.setOnClickListener(v -> {
            if (isNetwork()) {
                show();
            } else {
                Toast.makeText(getActivity(), "Không có kết nối mạng!\nVui lòng thử lại.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getAccount() {
        Api.apiInterface().getAccount(idAccount).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getAccountsuccess() == 1) {
                        tvName.setText(response.body().getTenhienthi());
                    } else {
                        Toast.makeText(getActivity(), "Không tìm thấy tài khoản!", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable t) {
                Log.e("E123_AccountFagment", t.getMessage());
            }
        });
    }
}
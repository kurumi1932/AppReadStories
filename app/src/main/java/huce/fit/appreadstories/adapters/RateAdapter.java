package huce.fit.appreadstories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import huce.fit.appreadstories.R;
import huce.fit.appreadstories.model.DanhGia;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.RateHoder> {
    private final List<DanhGia> listRate;

    public RateAdapter(List<DanhGia> listRate) {
        this.listRate = listRate;
    }

    @NonNull
    @Override
    public RateAdapter.RateHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_rate_item, parent, false);
        return new RateAdapter.RateHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RateAdapter.RateHoder holder, int position) {
        DanhGia dg = listRate.get(position);
        if (dg != null) {
            holder.tvName.setText(dg.getTenhienthi());
            int pointRate = dg.getDiemdanhgia();
            if (pointRate >= 1) {
                holder.ivRate1.setImageResource(R.drawable.ic_rate);
            }
            if (pointRate >= 2) {
                holder.ivRate2.setImageResource(R.drawable.ic_rate);
            }
            if (pointRate >= 3) {
                holder.ivRate3.setImageResource(R.drawable.ic_rate);
            }
            if (pointRate >= 4) {
                holder.ivRate4.setImageResource(R.drawable.ic_rate);
            }
            if (pointRate == 5) {
                holder.ivRate5.setImageResource(R.drawable.ic_rate);
            }
            holder.tvRate.setText(dg.getDanhgia());
        }
    }

    @Override
    public int getItemCount() {
        if (listRate != null && listRate.size() > 0)
            return listRate.size();
        else
            return 0;
    }

    public class RateHoder extends RecyclerView.ViewHolder {
        private TextView tvName, tvRate;
        private ImageView ivRate1, ivRate2, ivRate3, ivRate4, ivRate5;

        public RateHoder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            ivRate1 = itemView.findViewById(R.id.ivRate1);
            ivRate2 = itemView.findViewById(R.id.ivRate2);
            ivRate3 = itemView.findViewById(R.id.ivRate3);
            ivRate4 = itemView.findViewById(R.id.ivRate4);
            ivRate5 = itemView.findViewById(R.id.ivRate5);
            tvRate = itemView.findViewById(R.id.tvRate);
        }

    }
}

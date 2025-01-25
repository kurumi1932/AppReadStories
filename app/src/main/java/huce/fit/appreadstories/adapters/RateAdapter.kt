package huce.fit.appreadstories.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import huce.fit.appreadstories.R
import huce.fit.appreadstories.model.Rate

class RateAdapter : RecyclerView.Adapter<RateAdapter.RateHolder>() {

    class RateHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvRate: TextView = itemView.findViewById(R.id.tvRate)
        val ivRate1: ImageView = itemView.findViewById(R.id.ivRate1)
        val ivRate2: ImageView = itemView.findViewById(R.id.ivRate2)
        val ivRate3: ImageView = itemView.findViewById(R.id.ivRate3)
        val ivRate4: ImageView = itemView.findViewById(R.id.ivRate4)
        val ivRate5: ImageView = itemView.findViewById(R.id.ivRate5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_rate_item, parent, false)
        return RateHolder(view)
    }

    override fun onBindViewHolder(holder: RateHolder, position: Int) {
        val rate = mRateList[position]
        holder.tvName.text = rate.displayName
        val ratePoint = rate.ratePoint
        if (ratePoint > 0) {
            holder.ivRate1.setImageResource(R.drawable.ic_rate)
            if (ratePoint > 1) {
                holder.ivRate2.setImageResource(R.drawable.ic_rate)
                if (ratePoint > 2) {
                    holder.ivRate3.setImageResource(R.drawable.ic_rate)
                    if (ratePoint > 3) {
                        holder.ivRate4.setImageResource(R.drawable.ic_rate)
                        if (ratePoint == 5) {
                            holder.ivRate5.setImageResource(R.drawable.ic_rate)
                        }
                    }
                }
            }
        }
        holder.tvRate.text = rate.rate
    }

    override fun getItemCount(): Int {
        return mRateList.size
    }

    private var mRateList: MutableList<Rate> = mutableListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setDataRate(rateList: MutableList<Rate>) {
        mRateList = rateList
        notifyDataSetChanged()
    }
}

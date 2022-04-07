package com.example.currency.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.example.currency.databinding.ItemRvBinding
import com.example.currency.model.Money


class MyCurrencyAdapter(var list: List<Money>,val rvClick: RvClick): RecyclerView.Adapter<MyCurrencyAdapter.VH>() {

    inner class VH(private var itemRV: ItemRvBinding):RecyclerView.ViewHolder(itemRV.root){
        @SuppressLint("SetTextI18n")
        fun onBind(money: Money){
            itemRV.name.text = money.CcyNm_UZ
            itemRV.rate.text =  money.Rate + " sum"
            itemRV.root.setOnClickListener {
                rvClick.onItemClick(money)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
    interface RvClick{
        fun onItemClick(money: Money)
    }
}
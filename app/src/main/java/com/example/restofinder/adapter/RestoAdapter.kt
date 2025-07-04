package com.example.restofinder.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.restofinder.R
import com.example.restofinder.model.RestoModel

class RestoAdapter(private var list: List<RestoModel>) :
    RecyclerView.Adapter<RestoAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nama = itemView.findViewById<TextView>(R.id.tv_resto)
        val alamat = itemView.findViewById<TextView>(R.id.tv_alamat)
        val rating = itemView.findViewById<TextView>(R.id.tv_jarakRating)
        val jamBuka = itemView.findViewById<TextView>(R.id.tv_jamBuka)
        val img = itemView.findViewById<ImageView>(R.id.iv_resto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resto, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resto = list[position]
        holder.nama.text = resto.nama
        holder.alamat.text = resto.alamat
        holder.rating.text = "${"%.1f".format(resto.jarak)} Km | Rating ${resto.rating ?: "-"}"
        holder.jamBuka.text = if (resto.jamBuka == true) "Buka" else "Tutup"
        resto.foto?.let {
            holder.img.setImageBitmap(it)
        } ?: run {
            holder.img.setImageResource(R.drawable.placeholder)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newList: List<RestoModel>) {
        list = newList
        notifyDataSetChanged()
    }
}
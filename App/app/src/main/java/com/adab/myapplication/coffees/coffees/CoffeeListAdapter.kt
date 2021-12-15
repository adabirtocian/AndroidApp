package com.adab.myapplication.coffees.coffees

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.adab.myapplication.R
import com.adab.myapplication.coffees.coffee.CoffeeEditFragment
import com.adab.myapplication.coffees.data.Coffee
import com.adab.myapplication.core.TAG

class CoffeeListAdapter (
    private val fragment: Fragment,
) : RecyclerView.Adapter<CoffeeListAdapter.ViewHolder>() {

    var items = emptyList<Coffee>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener = View.OnClickListener { view ->
        val item = view.tag as Coffee
        fragment.findNavController().navigate(R.id.action_CoffeeListFragment_to_CoffeeEditFragment, Bundle().apply {
            putString(CoffeeEditFragment.COFFEE_ID, item._id)
        })
    };

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_coffee, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val item = items[position]
        holder.textView.text = item.originName
        holder.itemView.tag = item
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            textView = view.findViewById(R.id.text)
        }
    }
}

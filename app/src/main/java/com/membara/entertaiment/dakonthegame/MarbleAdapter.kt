package com.membara.entertaiment.dakonthegame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.membara.entertaiment.dakonthegame.databinding.ItemMarbleBinding

class MarbleAdapter(
    private val marbles: MutableList<Int>,
    private val context: Context,
    private val activity: PlayActivity
) :
    RecyclerView.Adapter<MarbleAdapter.ViewHolder>() {

    private lateinit var binding: ItemMarbleBinding

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemMarbleBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return marbles.size
    }
}
package com.membara.entertaiment.dakonthegame

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.membara.entertaiment.dakonthegame.databinding.ItemContainerBinding

class ContainerAdapter(
    private var containers: MutableList<Int>,
    private val player: Int,
    private val context: Context,
    private val activity: PlayActivity
) :
    RecyclerView.Adapter<ContainerAdapter.ViewHolder>() {

    private lateinit var binding: ItemContainerBinding

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemContainerBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        checkView(position)
        containerOnClick(position)
    }

    override fun getItemCount(): Int {
        return containers.size
    }

    private fun containerOnClick(position: Int) {
        if (containers[position] != 0) {
            binding.llContainer.setOnClickListener {
                activity.timer = -200
                if (activity.playerTurn == player) {
                    activity.moveMarble(
                        containers[position],
                        position,
                        player,
                        "start"
                    )
                }
            }

            checkView(position)
        }
    }

    private fun checkView(position: Int) {
        when (val container = containers[position]) {
            0 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_0)
                binding.tvMarbles.visibility = View.GONE
            }
            1 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_1)
                binding.tvMarbles.visibility = View.GONE
            }
            2 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_2)
                binding.tvMarbles.visibility = View.GONE
            }
            3 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_3)
                binding.tvMarbles.visibility = View.GONE
            }
            4 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_4)
                binding.tvMarbles.visibility = View.GONE
            }
            5 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_5)
                binding.tvMarbles.visibility = View.GONE
            }
            6 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_6)
                binding.tvMarbles.visibility = View.GONE
            }
            7 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_7)
                binding.tvMarbles.visibility = View.GONE
            }
            8 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_8)
                binding.tvMarbles.visibility = View.GONE
            }
            9 -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_9)
                binding.tvMarbles.visibility = View.GONE
            }
            else -> {
                binding.llContainer.setBackgroundResource(R.drawable.bg_0)
                binding.tvMarbles.visibility = View.VISIBLE
                binding.tvMarbles.text = container.toString()
            }
        }
    }
}
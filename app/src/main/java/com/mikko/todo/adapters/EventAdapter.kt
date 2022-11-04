package com.mikko.todo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mikko.todo.R
import com.mikko.todo.databinding.EventBinding

class EventsAdapter: RecyclerView.Adapter<EventsAdapter.EventsViewHolder>(){

    var items = mutableListOf<String>("1", "2", "3", "4")
        set(value) {
            val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize() = field.size

                override fun getNewListSize() = value.size

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                    field[oldItemPosition] == value[newItemPosition]


                override fun areContentsTheSame(
                    oldItemPosition: Int,
                    newItemPosition: Int
                ) =  field[oldItemPosition] == value[newItemPosition]


            }, true)

            diff.dispatchUpdatesTo(this)
            field = value
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.event, parent, false))
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bind(items[position])

        println("element was bind")
    }

    override fun getItemCount() = items.size

    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = EventBinding.bind(view)

        fun bind(text: String) {
            binding.textField.text = text
            binding.checkbox.setOnCheckedChangeListener { compoundButton, _ ->
                if (compoundButton.isChecked) {
                    items.remove(text)
                    binding.checkbox.isChecked = false
                    notifyDataSetChanged()
                }
            }
        }
    }


}
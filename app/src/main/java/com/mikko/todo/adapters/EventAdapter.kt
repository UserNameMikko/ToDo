package com.mikko.todo.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mikko.todo.R
import com.mikko.todo.databinding.EventBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EventsAdapter(
    val checkList: () -> Unit
): ListAdapter<String, EventsAdapter.EventsViewHolder>(differ){
    var context: Context? = null
    var items = mutableListOf<String>()
    /*override fun submitList(list: MutableList<String>){

        super.submitList(list)
    }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.event, parent, false))
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bind(currentList[position])
        println("element was bind")
    }


    override fun getItemCount() = currentList.size

    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = EventBinding.bind(view)

        fun bind(text: String) {
            Log.e("text of event", text)
            binding.textField.text = text
            checkList()
            binding.checkbox.setOnCheckedChangeListener { compoundButton, _ ->
                if (compoundButton.isChecked) {
                    MainScope().launch {
                        delay(500)
                        //items.remove(text)
                        var list: MutableList<String> = currentList.toMutableList()
                        list.remove(text)
                        submitList(list)
                        delay(500)
                        //checkList()
                        binding.checkbox.isChecked = false

                    }
                    if (context != null) {
                        Toast.makeText(
                            context,
                            "Congratulations!",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }
                checkList()
            }
        }
    }
    companion object {
        val differ = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

        }
    }
}
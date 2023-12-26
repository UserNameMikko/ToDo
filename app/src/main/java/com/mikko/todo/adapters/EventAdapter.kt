package com.mikko.todo.adapters

import android.animation.ObjectAnimator
import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mikko.todo.R
import com.mikko.todo.databinding.EventBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EventsAdapter(
    val checkList: () -> Unit,
    val isDarkTheme: () -> Boolean
): ListAdapter<String, EventsAdapter.EventsViewHolder>(differ) {
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
        val prefs = context?.getSharedPreferences("app_theme", Context.MODE_PRIVATE)
        val isDark = prefs?.getInt("apptheme", 0)?:0
        holder.bind(currentList[position], isDark)
        Log.d("Binding","element was bind")
    }


    override fun getItemCount() = currentList.size

    inner class EventsViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = EventBinding.bind(view)

        fun bind(text: String, isDark: Int) {
            checkList()
            println("isdark: $isDark")
            var isExpanded = false
            Log.e("text of event", text)
            binding.textField.text = text

            if (isDark == 1) {
                binding.textField.setTextColor(ContextCompat.getColor(context!!, R.color.white))
                binding.buttonExpandNote.setImageResource(R.drawable.arrow_up_dark)
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(context!!, R.color.default_gray)
                )

            } else  {
                binding.textField.setTextColor(
                    ContextCompat.getColor(context!!, R.color.default_gray)
                )
                binding.buttonExpandNote.setImageResource(R.drawable.arrow_up)
                binding.root.setBackgroundColor(ContextCompat.getColor(context!!, R.color.white))
            }
            if (binding.textField.text.length < 25) {
                /*if (isDarkTheme()) {
                    binding.buttonExpandNote.setImageResource(R.drawable.arrow_up_dark)

                } else {
                    binding.buttonExpandNote.setImageResource(R.drawable.arrow_up)
                }
            } else {*/
                binding.buttonExpandNote.visibility = View.GONE
            }
            val animationDown = ObjectAnimator.ofFloat(
                binding.buttonExpandNote, "rotationX", 180f, 0f
            )
            val animationUp = ObjectAnimator.ofFloat(
                binding.buttonExpandNote, "rotationX", 0f, 180f
            )

            binding.buttonExpandNote.setOnClickListener {
                if (!isExpanded){
                    binding.textField.maxLines = Int.MAX_VALUE
                    //animationDown.duration = expand(binding.textField)
                    //animationDown.interpolator = AccelerateDecelerateInterpolator()
                    animationDown.start()

                } else {
                    binding.textField.maxLines = 1
                    //animationUp.duration = collapse(binding.textField)
                    //animationUp.interpolator = AccelerateDecelerateInterpolator()
                      animationUp.start()
                }
                isExpanded = !isExpanded
            }


            binding.checkbox.setOnCheckedChangeListener { compoundButton, _ ->
                if (compoundButton.isChecked) {
                    MainScope().launch {
                        delay(500)
                        //items.remove(text)
                        val list: MutableList<String> = currentList.toMutableList()
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
package com.mikko.todo

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.Toast
import com.mikko.todo.adapters.EventsAdapter
import com.mikko.todo.databinding.FragmentFirstBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = EventsAdapter { checkList() }
    private var items = mutableListOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.start_anim)
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val animFab = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_anim)
        binding.fab.startAnimation(animFab)
        adapter.context = binding.root.context
        binding.recyclerEvents.startAnimation(anim)
        binding.noEvents.startAnimation(anim)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerEvents.adapter = adapter
        restoreData()
        checkList()
        when (binding.root.context.resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                binding.sadFace.setImageResource(R.drawable.sad_face_light_theme)
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                binding.sadFace.setImageResource(R.drawable.sad_face_dark_theme)
            }
        }
        println("list = ${adapter.currentList}" +
                "SIZE = ${adapter.currentList.size}")
        binding.fab.setOnClickListener {
            //adapter.items.add("$count")
            val text = EditText(requireActivity())
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Add new note")
                .setView(text)
                .setMessage("What do you want to do today?")
                .setPositiveButton("ADD") { _, _ ->
                    if (text.text.toString().isNotEmpty()){
                        items = adapter.currentList.toMutableList()
                        items.add(text.text.toString())
                        adapter.submitList(items)
                        Toast.makeText(requireContext(), "added", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(
                            requireContext(), "Your input is empty", Toast.LENGTH_LONG
                        ).show()
                    }

                }
                .setNegativeButton("Discard"){ dialog, id ->
                    Toast.makeText(requireContext(), "canceled", Toast.LENGTH_LONG).show()
                }
                //.show()
                .create()
            if (dialog.window != null)
                dialog.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
            dialog.show()
            when (binding.root.context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                        Color.parseColor("#2B2C34")
                    )
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                        Color.parseColor("#2B2C34")
                    )
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                        Color.parseColor("#FFFFFF")
                    )
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                        Color.parseColor("#FFFFFF")
                    )
                }
            }
        }
        binding.fab.setOnLongClickListener {
            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Erase All")
                .setMessage("Do you really want to erase all data?")
                .setPositiveButton("Yes") { _, _ ->
                        adapter.submitList(mutableListOf())
                        items = mutableListOf()
                        checkList()
                        Toast.makeText(
                            requireContext(), "data erased", Toast.LENGTH_LONG
                        ).show()
                }
                .setNegativeButton("No"){ _, _ ->
                    Toast.makeText(requireContext(), "canceled", Toast.LENGTH_LONG).show()
                }
                .show()
            when (binding.root.context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                        Color.parseColor("#2B2C34")
                    )
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                        Color.parseColor("#2B2C34")
                    )
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                        Color.parseColor("#FFFFFF")
                    )
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                        Color.parseColor("#FFFFFF")
                    )
                }
            }
            true
        }
    }

    private fun checkList() {
        val showAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.dialog_show)
        if (adapter.currentList.isNotEmpty()) {
            binding.sadFace.visibility = View.GONE
            Log.e("Is Not Empty", "af")

            binding.noEvents.visibility = View.GONE
        } else {
            Log.e("Is Empty", "tr")
            binding.sadFace.visibility = View.VISIBLE
            when (binding.root.context.resources.configuration.uiMode and
                    Configuration.UI_MODE_NIGHT_MASK) {
                Configuration.UI_MODE_NIGHT_NO -> {
                    binding.sadFace.setImageResource(R.drawable.sad_face_light_theme)
                }
                Configuration.UI_MODE_NIGHT_YES -> {
                    binding.sadFace.setImageResource(R.drawable.sad_face_dark_theme)
                }
            }
            binding.sadFace.startAnimation(showAnim)
            binding.noEvents.startAnimation(showAnim)
            binding.noEvents.visibility = View.VISIBLE
        }

    }
    private fun restoreData() {
        val prefs = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
        val encodedList = prefs.getString("TodoList", null)
        if (encodedList != null) {
            val decodedList = Json.decodeFromString<MutableList<String>>(encodedList)
            items = decodedList
            adapter.submitList(items)
        } else adapter.submitList(mutableListOf())
        Log.d("RESTORE", "SUCCESS")
    }
    private fun saveData(list: MutableList<String>) {
        val prefs = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val encodedList = Json.encodeToString(list)
        editor.putString("TodoList",encodedList)
            .apply()
        Log.d("SAVE", "SUCCESS")
    }

    override fun onPause() {
        super.onPause()
        saveData(adapter.currentList)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
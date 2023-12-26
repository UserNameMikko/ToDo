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
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import com.dolatkia.animatedThemeManager.AppTheme
import com.dolatkia.animatedThemeManager.ThemeFragment
import com.dolatkia.animatedThemeManager.ThemeManager
import com.google.android.material.snackbar.Snackbar
import com.mikko.todo.adapters.EventsAdapter
import com.mikko.todo.databinding.FragmentFirstBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : ThemeFragment() {

    private var _binding: FragmentFirstBinding? = null
    private var isDarkTheme = false

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!


    private val adapter = EventsAdapter( {checkList()}, { isDarkTheme()})
    private var items = mutableListOf<String>()
    private var title = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding.recyclerEvents.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 5 && binding.fab.isShown)
                    binding.fab.hide()
                if (dy < -5 && !binding.fab.isShown)
                    binding.fab.show()
                if (!binding.recyclerEvents.canScrollVertically(-1))
                    binding.fab.show()

            }
        })
        restoreData()
        checkList()
        if (isDarkTheme()) {
                binding.sadFace.setImageResource(R.drawable.sad_face_light_theme)
        } else {
            binding.sadFace.setImageResource(R.drawable.sad_face_dark_theme)
        }
        println("list = ${adapter.currentList}" +
                "SIZE = ${adapter.currentList.size}")
        binding.fab.setOnClickListener {
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
            if (isDarkTheme()){
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                    Color.parseColor("#FFFFFF")
                )
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                    Color.parseColor("#FFFFFF")
                )

            } else {
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                    Color.parseColor("#2B2C34")
                )
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                    Color.parseColor("#2B2C34")
                )
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
                if (isDarkTheme()){

                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                        Color.parseColor("#FFFFFF")
                    )
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                        Color.parseColor("#FFFFFF")
                    )

                } else {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(
                        Color.parseColor("#2B2C34")
                    )
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(
                        Color.parseColor("#2B2C34")
                    )
                }
            true
        }
    }

    override fun syncTheme(appTheme: AppTheme) {
        val isDark = appTheme.id() != 0
        isDarkTheme = isDark
        if (isDark){
            binding.sadFace.setImageResource(R.drawable.sad_face_dark_theme)
            binding.title.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.noEvents.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            binding.sadFace.setImageResource(R.drawable.sad_face_light_theme)
            binding.title.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.default_gray)
            )
            binding.noEvents.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.default_gray)
            )
        }

        Snackbar.make(binding.root, "${appTheme.id()}",Snackbar.LENGTH_LONG).show()
    }


    private fun checkList() {
        val showAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.dialog_show)
        val size = adapter.currentList.size
        title = "Wow, $size ${if(size > 1 || size == 0) "notes" else "note"}"
        binding.title.text = title
        if (adapter.currentList.isNotEmpty()) {
            binding.sadFace.visibility = View.GONE
            Log.e("Is Not Empty", "af")

            binding.noEvents.visibility = View.GONE
        } else {
            Log.e("Is Empty", "tr")
            binding.sadFace.visibility = View.VISIBLE
            if (isDarkTheme()) {
                binding.sadFace.setImageResource(R.drawable.sad_face_dark_theme)
            }else {
                binding.sadFace.setImageResource(R.drawable.sad_face_light_theme)
            }

            binding.sadFace.startAnimation(showAnim)
            binding.noEvents.startAnimation(showAnim)
            binding.noEvents.visibility = View.VISIBLE
        }


    }

    private fun isDarkTheme() : Boolean {
        Log.e("DARKTHEME", "${resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES}" )
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
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
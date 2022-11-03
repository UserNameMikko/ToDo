package com.mikko.todo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.mikko.todo.adapters.EventsAdapter
import com.mikko.todo.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val adapter = EventsAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.start_anim)
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        binding.recyclerEvents.startAnimation(anim)
        binding.noEvents.startAnimation(anim)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerEvents.adapter = adapter
        if(binding.recyclerEvents.adapter?.itemCount != 0)
            binding.noEvents.visibility = View.GONE
        println("list = ${adapter.items}" +
                "SIZE = ${binding.recyclerEvents.adapter?.itemCount}")

        /*binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.toStartActivity.setOnClickListener {
            startActivity(Intent(requireActivity(), StartActivity::class.java))
            requireActivity().finish()
        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
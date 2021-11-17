package com.example.hseobshaga.fragment.home

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.hseobshaga.R
import com.example.hseobshaga.databinding.ActivityHomeBinding
import com.example.hseobshaga.databinding.FragmentHomeBinding
import com.example.hseobshaga.databinding.FragmentProfileBinding
import com.example.hseobshaga.fragment.profile.HomeUiState
import com.example.hseobshaga.fragment.profile.ProfileAction
import com.example.hseobshaga.fragment.profile.ProfileFragment
import com.example.hseobshaga.fragment.profile.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import android.widget.ArrayAdapter
import com.example.hseobshaga.adapters.CustomListViewAdapter


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    companion object{
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeState()
    }

    private fun renderUI(state: HomeUiState){

        state.profile?.let{
            binding.roomNumberTV.text = "room " + it.room
        }

        val arrayOfNeighbours = ArrayList<String>()
        state.userNeighbours.forEach{
            arrayOfNeighbours.add(it.firstName + " " + it.secondName)
        }
        Log.d("UserArray", arrayOfNeighbours.toString() + "le")

        val adapter = CustomListViewAdapter(requireActivity(),arrayOfNeighbours)
        binding.listViewOfNeighbours.adapter = adapter
    }


    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.flowState.collect { state ->
                    renderUI(state)
                }
            }
        }
    }



}
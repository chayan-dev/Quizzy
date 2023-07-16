package com.example.quizzy.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentHomeBinding
import com.example.quizzy.ui.viewmodels.MainViewModel

class HomeFragment : Fragment() {

  lateinit var binding: FragmentHomeBinding
  private val viewModel by activityViewModels<MainViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    binding = FragmentHomeBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.getQuestions()

    binding.btnStart.setOnClickListener {
      if(binding.etName1.text?.isNotBlank() == true && binding.etName2.text?.isNotBlank() == true){
        viewModel.setPlayersName(binding.etName1.text.toString(),binding.etName2.text.toString())
        findNavController().navigate(R.id.action_homeFragment_to_questionFragment)
      } else{
        Toast.makeText(context,"Emter name for both players", Toast.LENGTH_SHORT).show()
      }
    }

  }

}
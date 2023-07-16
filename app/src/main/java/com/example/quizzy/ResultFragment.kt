package com.example.quizzy

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.quizzy.MainViewModel
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentResultBinding


class ResultFragment : Fragment() {

  lateinit var binding: FragmentResultBinding
  private val viewModel by activityViewModels<MainViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentResultBinding.inflate(inflater, container, false)
    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    viewModel.calculateCorrectAns()

    binding.btnSave1.text  = viewModel.playerName1
    binding.btnSave2.text  = viewModel.playerName2

    binding.tvScore1.text = "${viewModel.playerName1} : ${viewModel.correctAnsPlayer1} out of ${viewModel.scores.value?.size?.div(2)} correct"
    binding.tvScore2.text = "${viewModel.playerName2} : ${viewModel.correctAnsPlayer2} out of ${viewModel.scores.value?.size?.div(2)} correct"

    if(viewModel.isMatchTied()){
      binding.tvCongo.text = "Match Tie"
      binding.btnFinish.text = "Play Tie-breaker"
      binding.tvName.visibility = View.GONE
    }else{
      binding.tvCongo.text = "Hey, Congratulations"
      binding.btnFinish.text = "FINISH"
      if(viewModel.playerScore1>viewModel.playerScore2){
        binding.tvName.text = viewModel.playerName1
      }else{
        binding.tvName.text = viewModel.playerName2
      }
    }

    binding.btnFinish.setOnClickListener {
      //is match is tied
      if(viewModel.isMatchTied()){
        viewModel.matchTiedAction()
        findNavController().navigate(R.id.action_resultFragment_to_questionFragment)
      }
    }
  }

}
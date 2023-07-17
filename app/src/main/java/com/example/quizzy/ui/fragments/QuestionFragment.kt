package com.example.quizzy.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentQuestionBinding
import com.example.quizzy.ui.fragments.OptionsAdapter
import com.example.quizzy.ui.viewmodels.MainViewModel
import com.example.quizzy.utils.Constants.FINISH_BTN
import com.example.quizzy.utils.Constants.SUBMIT_BTN

class QuestionFragment : Fragment() {

  private lateinit var binding: FragmentQuestionBinding
  private lateinit var optionsAdapter: OptionsAdapter
  private val viewModel by activityViewModels<MainViewModel>()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    // Inflate the layout for this fragment
    binding = FragmentQuestionBinding.inflate(inflater, container, false)

    optionsAdapter = OptionsAdapter(listOf(), -1) { onOptionClickAction(it) }
    binding.rvOptions.layoutManager = LinearLayoutManager(context)
    binding.rvOptions.adapter = optionsAdapter

//    viewModel.getQuestions()

    return binding.root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    viewModel.currQuestion.observe(viewLifecycleOwner, Observer {
      binding.tvQuestion.text = it.question
      optionsAdapter.options = it.options
      optionsAdapter.correctOp = it.options.indexOf(it.correctAnswer)
      optionsAdapter.notifyDataSetChanged()
    })

    viewModel.btnText.observe(viewLifecycleOwner, Observer {
      binding.btnSubmit.text = it
    })

    viewModel.currentPosition.observe(viewLifecycleOwner, Observer {
      if((it+1)%2 == 0){
        binding.tvPlayer.text = getString(R.string.question_header, viewModel.playerName2)
      }else{
        binding.tvPlayer.text = getString(R.string.question_header, viewModel.playerName1)
      }
    })

    viewModel.selectedOption.observe(viewLifecycleOwner, Observer { option ->
      binding.btnSubmit.isEnabled = !(option==-1 && viewModel.isMatchTie)
    })

    binding.btnSubmit.setOnClickListener {
      if(binding.btnSubmit.text == FINISH_BTN){
        viewModel.calculateCorrectAns()
        viewModel.calculateScore()
        findNavController().navigate(R.id.action_questionFragment_to_resultFragment)
      }
      else{
        if(binding.btnSubmit.text == SUBMIT_BTN){
          optionsAdapter.showCorrectOption()
        }
        viewModel.submitActon()
      }
    }

    viewModel.scores.observe(viewLifecycleOwner, Observer {
      Log.d("scores_ld",it.toString())
    })


  }

  private fun onOptionClickAction(it: Int) {
    viewModel.setSelectedOption(it)
  }
}
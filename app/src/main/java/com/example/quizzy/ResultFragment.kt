package com.example.quizzy

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.drawToBitmap
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.quizzy.MainViewModel
import com.example.quizzy.R
import com.example.quizzy.databinding.FragmentResultBinding
import java.io.File
import java.io.FileOutputStream


class ResultFragment : Fragment() {

  lateinit var binding: FragmentResultBinding
  private val viewModel by activityViewModels<MainViewModel>()
  lateinit var playerName1: String
  lateinit var playerName2: String


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
    playerName1=viewModel.playerName1
    playerName2=viewModel.playerName2
    val correctAns1=viewModel.correctAnsPlayer1
    val correctAns2=viewModel.correctAnsPlayer2
    val totalQues = viewModel.scores.value?.size?.div(2)


    binding.btnSave1.text  = playerName1
    binding.btnSave2.text  = playerName2

    binding.tvScore1.text = "$playerName1 : $correctAns1 out of $totalQues correct"
    binding.tvScore2.text = "$playerName2 : $correctAns2 out of $totalQues correct"

    if(viewModel.isMatchTied()){
      viewModel.getQuestions()
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
      }else{
        findNavController().popBackStack(R.id.homeFragment,false)
      }
    }

    binding.btnSave1.setOnClickListener {
      saveScoreCard("$playerName1 : $correctAns1 out of $totalQues correct")
    }
    binding.btnSave2.setOnClickListener {
      saveScoreCard("$playerName2 : $correctAns2 out of $totalQues correct")
    }

  }

  private fun saveScoreCard(scoreText: String){
    val inflater = LayoutInflater.from(context)
    val rootView = inflater.inflate(R.layout.layout_score_card,null)

    val textView = rootView.findViewById<TextView>(R.id.tv_scorecard)
    textView.text = scoreText

    rootView.measure(
      View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
      View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    )
    rootView.layout(0, 0, rootView.measuredWidth, rootView.measuredHeight)

    val bitmap = Bitmap.createBitmap(
      rootView.measuredWidth,
      rootView.measuredHeight,
      Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    rootView.draw(canvas)

    val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "quizzy score")
    if (!directory.exists()) {
      directory.mkdirs()
    }

    val imageFile = File(directory , "score-card.png")
    val outputStream = FileOutputStream(imageFile)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.close()
    Toast.makeText(context, "Score card saved", Toast.LENGTH_SHORT).show()

  }


}
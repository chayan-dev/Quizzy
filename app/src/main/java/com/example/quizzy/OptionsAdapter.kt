package com.example.quizzy

import android.graphics.Color
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.quizzy.databinding.ItemOptionBinding

class OptionsAdapter(
  var options : List<String>,
  var correctOp: Int,
  val onOptionClick: (optionPosition: Int) -> Unit
): RecyclerView.Adapter<OptionsAdapter.OptionsViewHolder>() {

  private lateinit var binding: ItemOptionBinding
  private var selectedItemPosition = RecyclerView.NO_POSITION
//  var showIsCorrect = false
  var showAnswer = false

  inner class OptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionsViewHolder {
    return OptionsViewHolder(
      parent.context.getSystemService(LayoutInflater::class.java).inflate(
        R.layout.item_option,
        parent,
        false
      )
    )
  }

  override fun getItemCount(): Int {
    return options.size
  }

  override fun onBindViewHolder(holder: OptionsViewHolder, position: Int) {

    binding = ItemOptionBinding.bind(holder.itemView)
    binding.apply {
      tvOption.text = options[position]
      tvOption.setOnClickListener {

        val prevSelectedItemPosition = selectedItemPosition
        selectedItemPosition = holder.adapterPosition
        notifyItemChanged(selectedItemPosition)
        notifyItemChanged(prevSelectedItemPosition)

        onOptionClick(selectedItemPosition)
      }
    }

    if (selectedItemPosition == holder.adapterPosition) {
      selectedBg(binding)
    } else {
      defaultBg(binding)
    }

//    if(showIsCorrect){
//      Log.d("showCorrect, selected, correct, positon","$showIsCorrect,$selectedItemPosition,$correctOp,$position")
//      if (holder.adapterPosition==selectedItemPosition && selectedItemPosition!=correctOp){
//        binding.tvOption.setBackgroundResource(R.drawable.wrong_option_border_bg)
//        selectedItemPosition = RecyclerView.NO_POSITION
//        showIsCorrect = false
//      }
//    }

    if(showAnswer){
      Log.d("showAnswer, selected, correct, positon","$showAnswer,$selectedItemPosition,$correctOp,$position")
      if(holder.adapterPosition==correctOp){
        binding.tvOption.setBackgroundResource(R.drawable.correct_option_border_bg)
        showAnswer = false
//        showIsCorrect = false
        selectedItemPosition = RecyclerView.NO_POSITION
      }
    }
  }

  fun showCorrectOption(){
//    showIsCorrect = true
//    notifyItemChanged(selectedItemPosition)
    showAnswer = true
    notifyItemChanged(correctOp)
  }

  fun defaultBg(binding: ItemOptionBinding) {
    binding.apply {
      tvOption.setTextColor(Color.parseColor("#7A8089"))
      tvOption.typeface= Typeface.DEFAULT
      tvOption.setBackgroundResource(R.drawable.default_option_border_bg)
    }
  }

  fun selectedBg(binding: ItemOptionBinding) {
    binding.apply {
      tvOption.setTextColor(Color.parseColor("#363A43"))
      tvOption.setTypeface(tvOption.typeface,Typeface.BOLD)
      tvOption.setBackgroundResource(R.drawable.selected_option_border_bg)
    }
  }

}
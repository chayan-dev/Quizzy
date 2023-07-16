package com.example.quizzy.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizzy.api.models.QuestionType
import com.example.quizzy.data.QuestionsRepository
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {

  private val questionsRepository = QuestionsRepository()
  var questionList = mutableListOf<QuestionType>()
  var correctAnsPlayer1 = 0
  var correctAnsPlayer2 = 0
  lateinit var playerName1: String
  lateinit var playerName2: String

  private var _currentPosition : MutableLiveData<Int> = MutableLiveData(0)
  val currentPosition: MutableLiveData<Int> = _currentPosition

  private var _currQuestion = MutableLiveData<QuestionType>()
  val currQuestion: MutableLiveData<QuestionType> = _currQuestion

  private var _selectedOption : MutableLiveData<Int> = MutableLiveData(-1)
  val selectedOption: MutableLiveData<Int> = _selectedOption

  private var _scores = MutableLiveData<List<Int>>()
  val scores: MutableLiveData<List<Int>> = _scores

  private var _btnText = MutableLiveData<String>()
  val btnText: MutableLiveData<String> = _btnText

  var playerScore1 = 0
  var playerScore2 = 0
  var isMatchTie = false


//  init {
//    getQuestions()
//  }

  fun getQuestions() {
    viewModelScope.launch {
      val response = questionsRepository.getQuestions()
      val tempList = mutableListOf<QuestionType>()
      response.body()?.results?.forEach { ques ->
        val options: MutableList<String> = ques.incorrectAnswers.toMutableList()
        options.add(ques.correctAnswer)
        options.shuffle()
        tempList.add(QuestionType(ques.correctAnswer, options, ques.question, ques.type))
      }
      questionList = tempList
      setQuestion()
      Log.d("response", tempList.toString())
    }
  }

  fun setQuestion() {
    _selectedOption.value = -1
    _currQuestion.postValue(_currentPosition.value?.let { questionList[it] })
    changeBtnText()
  }

  fun setSelectedOption(optionPosition: Int){
    _selectedOption.value = optionPosition
    changeBtnText()
  }

  fun submitActon(){
    if(_btnText.value == "NEXT"){
      _currentPosition.value = _currentPosition.value?.plus(1)
      if (_currentPosition.value?.plus(1)!! <= questionList.size){
        setQuestion()
      }
      return
    }

    if(_selectedOption.value == -1){
      setScore(0)
      if(_currentPosition.value?.plus(1) == questionList.size) {
//        calculateCorrectAns()
        setBtnText("FINISH")
      }else if(_btnText.value=="SKIP"){
        Log.d("currPos1",_currentPosition.value.toString())
        _currentPosition.value = _currentPosition.value?.plus(1)
        Log.d("currPos2",_currentPosition.value.toString())
        if (_currentPosition.value?.plus(1)!! <= questionList.size){
          setQuestion()
        }
      }else{
        setBtnText("NEXT")
      }
    }
    else{
      val ques = _currQuestion.value
      if(ques?.correctAnswer!= selectedOption.value?.let { ques?.options?.get(it) }) {
        setScore(-2)
      }
      else{
        setScore(5)
      }
      Log.d("currentPos", _currentPosition.value.toString())
      if(_currentPosition.value?.plus(1) == questionList.size) {
        if(isMatchTie){
          //lin 108 has to be here too if its last question and it is wrong, winner will be decided and show finish text
          //fetch new questions and add it to question list
          if(_currentPosition.value?.plus(1)?.rem(2) == 0 && !isLastScoreEqual()){
//            calculateCorrectAns()
            setBtnText("FINISH")
          }else {
            getQuestionsAndAddToExistingQues()
            setBtnText("NEXT")
          }
        }else{
//          calculateCorrectAns()
          setBtnText("FINISH")
        }
      }else{
        if(isMatchTie && _currentPosition.value?.plus(1)?.rem(2) == 0 && !isLastScoreEqual()){
//          val isEqualLastScore = isLastScoreEqual()
//          calculateCorrectAns()
          setBtnText("FINISH")
        }else {
          setBtnText("NEXT")
        }
      }
    }
  }

  private fun changeBtnText() {
    if(_selectedOption.value != -1 || isMatchTie){
      _btnText.value = "SUBMIT"
    }
    if(_selectedOption.value == -1 && !isMatchTie){
      _btnText.value = "SKIP"
    }
  }

  private fun setBtnText(str: String) {
    _btnText.value = str
  }

  fun setScore(score: Int) {
    _scores.value = _scores.value?.plus(score) ?: listOf(score)
  }

  fun calculateScore(){
    val list = _scores.value as List<Int>
    for (i in list.indices) {
      if (i % 2 == 0) {
        playerScore1 += list[i]
      } else {
        playerScore2 += list[i]
      }
    }
//    isMatchTie = isMatchTied()
//    if(isMatchTie) {
//      getQuestions()
//      _currentPosition.value=0
//    }
  }

  fun calculateCorrectAns(){
    val list = _scores.value as List<Int>
    for (i in list.indices) {
      if (i % 2 == 0 && list[i]==5) {
        correctAnsPlayer1++
      } else if(list[i]==5) {
        correctAnsPlayer2++
      }
    }
    Log.d("correct1", correctAnsPlayer1.toString())
    Log.d("correct2", correctAnsPlayer2.toString())
  }

  fun matchTiedAction(){
    isMatchTie = isMatchTied()
    _currentPosition.value = 0
//    getQuestions()

  }

  fun isMatchTied():Boolean{
//    calculateScore()
    return playerScore1==playerScore2
  }

  fun isLastScoreEqual(): Boolean{
    val lastIndex = _scores.value?.size?.minus(1)
    val player1 = lastIndex?.let { _scores.value?.get(it) }
    val player2 = lastIndex?.minus(1)?.let { _scores.value?.get(it) }
    return player1 == player2
  }

  fun getQuestionsAndAddToExistingQues() {
    viewModelScope.launch {
      val response = questionsRepository.getQuestions()
      val tempList = mutableListOf<QuestionType>()
      response.body()?.results?.forEach { ques ->
        val options: MutableList<String> = ques.incorrectAnswers.toMutableList()
        options.add(ques.correctAnswer)
        options.shuffle()
        tempList.add(QuestionType(ques.correctAnswer, options, ques.question, ques.type))
      }
      questionList.addAll(tempList)
      Log.d("tied_response", tempList.toString())
      Log.d("tied_ques", questionList.toString())


//      questionList = tempList
//      setQuestion()
//      Log.d("response", tempList.toString())
    }
  }

  fun setPlayersName(name1: String, name2: String){
    playerName1 = name1
    playerName2 = name2
  }

}
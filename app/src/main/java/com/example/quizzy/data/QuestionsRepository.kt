package com.example.quizzy.data

import com.example.quizzy.api.QuestionsClient


class QuestionsRepository {

  private val api = QuestionsClient.api

  suspend fun getQuestions() = api.getQuestions()
}
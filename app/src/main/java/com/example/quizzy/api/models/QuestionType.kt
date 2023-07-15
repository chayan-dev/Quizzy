package com.example.quizzy.api.models

import com.squareup.moshi.Json

data class QuestionType(
  val correctAnswer: String,
  val options: List<String>,
  val question: String,
  val type: String
)

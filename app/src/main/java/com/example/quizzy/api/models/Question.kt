package com.example.quizzy.api.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Question(
    @Json(name = "category")
    val category: String,
    @Json(name = "correct_answer")
    val correctAnswer: String,
    @Json(name = "difficulty")
    val difficulty: String,
    @Json(name = "incorrect_answers")
    val incorrectAnswers: List<String>,
    @Json(name = "question")
    val question: String,
    @Json(name = "type")
    val type: String
)
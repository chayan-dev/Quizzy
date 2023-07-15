package com.example.quizzy.api.models


import com.example.quizzy.api.models.Question
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class QuestionsResponse(
    @Json(name = "results")
    val results: List<Question>
)
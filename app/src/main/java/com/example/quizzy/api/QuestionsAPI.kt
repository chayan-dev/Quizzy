package com.example.quizzy.api

import com.example.quizzy.api.models.QuestionsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuestionsAPI {

  @GET("api.php")
  suspend fun getQuestions(
    @Query("amount") amount: Int? = 10
  ): Response<QuestionsResponse>

}
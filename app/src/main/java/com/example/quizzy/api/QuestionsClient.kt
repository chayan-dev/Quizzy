package com.example.quizzy.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object QuestionsClient {

  val retrofit = Retrofit.Builder()
      .baseUrl("https://opentdb.com/")
      .addConverterFactory(MoshiConverterFactory.create())
      .build()

  val api = retrofit.create(QuestionsAPI::class.java)

}
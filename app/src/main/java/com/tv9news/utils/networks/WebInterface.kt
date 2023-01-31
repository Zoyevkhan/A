package com.tv9news.utils.networks

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WebInterface {
    @POST(ApiConstants.API_MASTER_HIT)
    suspend fun getMasterHit(): Response<String>

    @POST(ApiConstants.API_HOME_DATA)
    suspend fun getHomeData(@Body data: String): Response<String>

    @POST(ApiConstants.API_DETAILS)
    suspend fun getDetails(@Body data: String): Response<String>

    @POST(ApiConstants.API_ARTICLES)
    suspend fun getArticles(@Body data: String): Response<String>

    @POST(ApiConstants.API_AUTHOR)
    suspend fun getAuthors(@Body data: String): Response<String>

    @POST(ApiConstants.API_AUTHOR_DETAILS)
    suspend fun getAuthorDetails(@Body data: String): Response<String>
}
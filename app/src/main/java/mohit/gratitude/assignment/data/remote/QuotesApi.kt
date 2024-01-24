package mohit.gratitude.assignment.data.remote

import mohit.gratitude.assignment.data.model.QuotesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface QuotesApi {

    @GET("/prod/dailyzen/?version=2")
    suspend fun getQuotes(
        @Query("date") date: String,
    ): Response<QuotesResponse>

}
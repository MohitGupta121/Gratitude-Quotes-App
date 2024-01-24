package mohit.gratitude.assignment.repository

import mohit.gratitude.assignment.data.model.QuotesResponse
import mohit.gratitude.assignment.data.remote.QuotesApi
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuotesRepository @Inject constructor(
    private val quotesApi: QuotesApi,
) {

    suspend fun getQuotes(date: String): Response<QuotesResponse> {
        return quotesApi.getQuotes(date)
    }
}
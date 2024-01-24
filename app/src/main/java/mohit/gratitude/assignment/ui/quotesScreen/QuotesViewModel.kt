package mohit.gratitude.assignment.ui.quotesScreen

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import mohit.gratitude.assignment.data.model.QuotesResponse
import mohit.gratitude.assignment.repository.QuotesRepository
import mohit.gratitude.assignment.util.DateUtil
import mohit.gratitude.assignment.util.NetworkUtil.Companion.hasInternetConnection
import mohit.gratitude.assignment.util.Resource
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class QuotesViewModel @Inject constructor(
    private val quoteRepository: QuotesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val quotesData: MutableLiveData<Resource<QuotesResponse>> = MutableLiveData()
    var quotesResponse: QuotesResponse? = null
    private val dateManager = DateUtil()

    init {
        getQuotes(dateManager.getCurrentDate())
    }

    fun getQuotes(date: String) = viewModelScope.launch {
        safeQuotesCall(date)
    }

    private suspend fun safeQuotesCall(date: String){
        quotesData.postValue(Resource.Loading())
        try{
            if(hasInternetConnection(context)){
                val response = quoteRepository.getQuotes(date)
                quotesData.postValue(handleQuotesResponse(response))
            }
            else{
                quotesData.postValue(Resource.Error("No Internet Connection"))
            }
        }
        catch (ex : Exception){
            when(ex){
                is IOException -> quotesData.postValue(Resource.Error("Network Failure"))
                else -> quotesData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun handleQuotesResponse(response: Response<QuotesResponse>): Resource<QuotesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                if (quotesResponse == null) {
                    quotesResponse = resultResponse
                } else {
                    val oldQuotes = quotesResponse?.let { HashSet(it) }
                    val newQuotes = HashSet(resultResponse)

                    if (oldQuotes != newQuotes) {
                        quotesResponse = resultResponse
                    }
                }

                return Resource.Success(quotesResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
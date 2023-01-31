package com.tv9news.shorts.viewmodels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tv9news.R
import com.tv9news.models.home.DataHome
import com.tv9news.utils.base.BaseViewModel
import com.tv9news.utils.helpers.AES
import com.tv9news.utils.helpers.UIEvent
import com.tv9news.utils.networks.ResponseValidator
import com.tv9news.utils.networks.RetrofitFactory
import kotlinx.coroutines.launch
import org.json.JSONObject

class ShortsViewModel(@NonNull application: Application
) : BaseViewModel(application) {

    fun callShortsApi(encryptionStr: String) {
        viewModelScope.launch(baseExceptionHandler) {
            sendSameEvents(UIEvent.onLoading)
            val webservice = RetrofitFactory.getInstance(context)
            val response = webservice.getArticles(encryptionStr)
            ResponseValidator.processResponse<String>(response) { status, msg, result ->
                if (status && result != null) {
                    sendSameEvents(UIEvent.Success(msg))
                    var jsonObject: JSONObject? = null
                    try {
                        val decryptdata =
                            AES.decrypt(result, AES.generatekeyAPI(), AES.generateVectorAPI())
                        jsonObject = JSONObject(decryptdata)
                        if (jsonObject.getBoolean("status")) {
//                            val detailsData: HomeApi =
//                                Gson().fromJson(jsonObject.toString(), HomeApi::class.java)
//                            _shortsDataList.value = detailsData.data
                        } else {
                            sendSameEvents(UIEvent.OnError(jsonObject.getString("message")))
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    sendSameEvents(UIEvent.OnError(context.getString(R.string.no_data_found)))
                }
            }
        }
    }


    var _shortsDataList = MutableLiveData(emptyList<DataHome>())
    val shortsDataList: LiveData<List<DataHome>> get() = _shortsDataList

    override fun onCleared() {
        super.onCleared()
    }
}
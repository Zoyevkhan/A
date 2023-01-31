package com.tv9news.home.activities.viewmodels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tv9news.R
import com.tv9news.models.home.DataHome
import com.tv9news.models.home.HomeApi
import com.tv9news.utils.base.BaseViewModel
import com.tv9news.utils.helpers.AES
import com.tv9news.utils.helpers.UIEvent
import com.tv9news.utils.networks.ResponseValidator
import com.tv9news.utils.networks.RetrofitFactory
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class HomeViewModel(@NonNull application: Application) : BaseViewModel(application) {
    fun callHomeApi(encryptionStr: String, type: String) {
        viewModelScope.launch(baseExceptionHandler) {
            sendSameEvents(UIEvent.onLoading)
            val webservice = RetrofitFactory.getInstance(context)
            var response: Response<String>
            if (type == "home") {
                response = webservice.getHomeData(encryptionStr)
            } else {
                response = webservice.getArticles(encryptionStr)
            }
            ResponseValidator.processResponse(response) { status, msg, result ->
                if (status && result != null) {
                    var jsonObject: JSONObject? = null
                    try {
                        val decryptdata =
                            AES.decrypt(result, AES.generatekeyAPI(), AES.generateVectorAPI())
                        jsonObject = JSONObject(decryptdata)
                        if (jsonObject.getBoolean("status")){
                            sendSameEvents(UIEvent.Success(msg))
                            val homeData: HomeApi =
                                Gson().fromJson(jsonObject.toString(), HomeApi::class.java)
                            _homeDataList.value = homeData.data
                        }else{
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

    //home Success Live data
    var _homeDataList = MutableLiveData(emptyList<DataHome>())
    val homeDataList: LiveData<List<DataHome>> get() = _homeDataList

    override fun onCleared() {
        super.onCleared()
    }


}
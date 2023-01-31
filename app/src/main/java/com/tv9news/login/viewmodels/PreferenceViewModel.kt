package com.tv9news.login.viewmodels

import android.app.Application
import android.app.SharedElementCallback
import android.util.Log
import androidx.annotation.NonNull
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.RoomDatabase
import com.google.gson.Gson
import com.tv9news.R
import com.tv9news.models.masterHit.*
import com.tv9news.room.LocalRoomDatabase
import com.tv9news.utils.base.BaseViewModel
import com.tv9news.utils.helpers.AES
import com.tv9news.utils.helpers.UIEvent
import com.tv9news.utils.networks.ResponseValidator
import com.tv9news.utils.networks.RetrofitFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class PreferenceViewModel(
    @NonNull application: Application
) : BaseViewModel(application) {

    fun callPreferenceApi() {
        viewModelScope.launch(baseExceptionHandler) {
            sendSameEvents(UIEvent.onLoading)
            val webservice = RetrofitFactory.getInstance(context)
            val response = webservice.getMasterHit()
            ResponseValidator.processResponse<String>(response) { status, msg, result ->
                if (status && result != null) {
                    sendSameEvents(UIEvent.Success(msg))
                    var jsonObject: JSONObject? = null
                    try {
                        val decryptdata =
                            AES.decrypt(result, AES.generatekeyAPI(), AES.generateVectorAPI())
                        jsonObject = JSONObject(decryptdata)
                        if (jsonObject.getBoolean("status")) {
                            val masterHitData: MasterApi =
                                Gson().fromJson(jsonObject.toString(), MasterApi::class.java)
                            _languagePreferenceList.value = masterHitData.data.language
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

    //Language Preference Success Live data
    private var _languagePreferenceList = MutableLiveData(emptyList<Language>())
    val languagePreferenceList: LiveData<List<Language>> get() = _languagePreferenceList

    // State Preference Success Live data
    private var _statePreferenceList = MutableLiveData(emptyList<State>())
    val statePreferenceList: LiveData<List<State>> get() = _statePreferenceList

    // Category Preference Success Live data
    private var _categoryPreferenceList = MutableLiveData(emptyList<Category>())
    val categoryPreferenceList: LiveData<List<Category>> get() = _categoryPreferenceList

    //masterHit Success Live data
    private var _menuData: MutableLiveData<Menu> = MutableLiveData()
    val menuData: LiveData<Menu> get() = _menuData

    //masterHit Success Live data
    private var _metaData: MutableLiveData<LangMeta> = MutableLiveData()
    val metaData: LiveData<LangMeta> get() = _metaData

    fun updateCategoryAndState(position: Int) {
        _categoryPreferenceList.value = _languagePreferenceList.value?.get(position)?.category
        _statePreferenceList.value = _languagePreferenceList.value?.get(position)?.states
        _metaData.value = _languagePreferenceList.value?.get(position)?.lang_meta
        _menuData.value = _languagePreferenceList.value?.get(position)?.menu
    }

     fun savePreferenceToRoom(
         callback: ()->Unit
     ) {
        Log.d("zoyeb", "onCleared: "+_languagePreferenceList.value)
        var language = _languagePreferenceList.value?.filter {
            it.isSelected
        }?.get(0)
        language?.category=language?.category?.filter {
            it.isSelected
        }!!
        language.states=language.states.filter {
            it.isSelected
        }
        viewModelScope.launch(Dispatchers.IO) {
            var languageDB = LocalRoomDatabase.getDatabase(context).languageDao()
            languageDB.nukeTable()
            languageDB.insertLanguage(language)
            callback.invoke()
        }
    }
}
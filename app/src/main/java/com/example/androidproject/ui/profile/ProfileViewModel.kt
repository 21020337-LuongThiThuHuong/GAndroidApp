package com.example.androidproject.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray
import org.json.JSONObject

class ProfileViewModel : ViewModel() {
    private val _fullName = MutableLiveData<String>()
    val fullName: LiveData<String> get() = _fullName

    private val _position = MutableLiveData<String>()
    val position: LiveData<String> get() = _position

    private val _historyList = MutableLiveData<List<HistoryItem>>()
    val historyList: LiveData<List<HistoryItem>> get() = _historyList

    fun loadProfileData(jsonString: String) {
        val jsonObject = JSONObject(jsonString)
        _fullName.value = jsonObject.getString("full_name")
        _position.value = jsonObject.getString("position")

        val historyArray = jsonObject.getJSONArray("history")
        val historyItems = mutableListOf<HistoryItem>()

        for (i in historyArray.length() - 1 downTo 0) {
            val historyObject = historyArray.getJSONObject(i)
            val title = historyObject.getString("title")
            val isUp = historyObject.getBoolean("is_up")
            historyItems.add(HistoryItem(title, isUp))
        }

        _historyList.value = historyItems
    }
}

data class HistoryItem(val description: String, val isUp: Boolean)
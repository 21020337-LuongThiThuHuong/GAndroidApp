package com.example.androidproject.ui.string

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StringViewModel : ViewModel() {
    private val _resultString = MutableLiveData<String>()
    val resultString: LiveData<String> get() = _resultString

    private val _errorMessage =
        MutableLiveData<String>().apply {
            value = "⚠️ Vui lòng nhập một chuỗi"
        }
    val errorMessage: LiveData<String> get() = _errorMessage

    fun processInputString(inputString: String) {
        if (inputString.isEmpty()) {
            _errorMessage.value = "⚠️ Vui lòng nhập một chuỗi"
        } else {
            val charCountMap = mutableMapOf<Char, Int>()

            for (char in inputString) {
                charCountMap[char] = charCountMap.getOrDefault(char, 0) + 1
            }

            val resultString =
                charCountMap.entries
                    .mapIndexed { index, entry ->
                        "${index + 1}. <b><font color='#34A049'>${entry.key}</font></b>: <font color='#6200EA'>${entry.value}</font>"
                    }.joinToString(separator = "<br>")

            _resultString.value = resultString
        }
    }
}

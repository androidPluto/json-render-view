package com.example

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream

class JSONReaderViewModel(application: Application) : AndroidViewModel(application) {

    val jsonString: LiveData<String>
        get() = _jsonString
    private val _jsonString = MutableLiveData<String>()

    fun parseJson(inputStream: InputStream) {
        viewModelScope.launch {
            try {
                val length = inputStream.available()
                val buffer = ByteArray(length)
                inputStream.read(buffer)
                val result = String(buffer, Charsets.UTF_8)
                inputStream.close()
                _jsonString.postValue(result)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}

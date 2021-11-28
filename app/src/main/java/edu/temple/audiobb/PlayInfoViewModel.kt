package edu.temple.audiobb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlayInfoViewModel : ViewModel() {
    private val playInfo: MutableLiveData<PlayInfo> by lazy {
        MutableLiveData<PlayInfo>().also {
            loadPlayInfo()
        }
    }

    fun getPlayInfo(): LiveData<PlayInfo> {
        return playInfo
    }

    fun setPlayInfo(_PlayInfo: PlayInfo?){
        playInfo.value = _PlayInfo

    }

    fun togglePlay(): Boolean{
        var result = false
        if(playInfo.value != null) {
            playInfo.value!!.isPlaying = playInfo.value!!.isPlaying != true
            result = playInfo.value!!.isPlaying
        }

        return result
    }

    fun setPlayTime(_time: Int): Boolean{
        var isSuccessful = false

        //if(playInfo.value != null) {
            //if(_time <= playInfo.value!!.maxRuntime){
                playInfo.value!!.currentTime = _time
                isSuccessful = true
            //}
        //}

        return isSuccessful
    }

    fun getPlayTime(): Int{
        var result = 0

        if(playInfo.value != null) {
            result = playInfo.value!!.currentTime
        }

        return result
    }

    private fun loadPlayInfo() {

    }


}
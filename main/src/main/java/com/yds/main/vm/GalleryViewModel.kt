package com.yds.main.vm

import android.app.Application
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class GalleryViewModel(val app: Application) : AndroidViewModel(app) {

    val imageDataList = mutableListOf<Uri>()
    val imageUriList = MutableLiveData<MutableList<Uri>>()
    var page = 0

    fun readPic() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                val mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                val projImage = arrayOf<String>(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.SIZE,
                    MediaStore.Images.Media.DISPLAY_NAME
                )
                val cursor = app.contentResolver.query(
                    mImageUri,
                    projImage,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    arrayOf("image/jpeg", "image/png"),
                    MediaStore.Images.Media.DATE_MODIFIED + " desc"
                )
                val uriList = mutableListOf<Uri>()
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        val path =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                        val uri = Uri.fromFile(File(path))
                        uriList.add(uri)
                    }
                }
                imageDataList.addAll(uriList)
                val list = mutableListOf<Uri>()

                if ((page + 1) * 20 < imageDataList.size) {
                    for (i in (page * 20) until (page + 1) * 20) {
                        list.add(imageDataList[i])
                    }
                } else {
                    list.addAll(imageDataList)
                }
                imageUriList.postValue(list)
                page = 1
            }
        }
    }

    fun getPicData(page: Int) {
        val list = imageUriList.value ?: mutableListOf<Uri>()
        if ((page + 1) * 20 < imageDataList.size) {
            for (i in (page * 20) until (page + 1) * 20) {
                list.add(imageDataList[i])
            }
        } else {
            list.addAll(imageDataList)
        }
        imageUriList.value = list
    }
}
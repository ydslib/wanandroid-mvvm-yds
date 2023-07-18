package com.yds.main.vm

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.pow

class GalleryViewModel(val app: Application) : AndroidViewModel(app) {

    val imageDataList = mutableListOf<Uri>()
    val imageUriList = MutableLiveData<MutableList<Uri>>()
    val imageBitmapList = MutableLiveData<MutableList<Bitmap>>()
    var page = 0

    companion object {
        const val REFRESH = 0
        const val LOADMORE = 1
        const val SHARED_ELEMENT_NAME = "share_image"
    }

    fun readPic() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                println("current-thread:${Thread.currentThread().name}")
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
                        val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                        if (index == -1) continue
                        val path =
                            cursor.getString(index)
                        val uri = Uri.fromFile(File(path))
                        uriList.add(uri)
                    }
                }
                imageDataList.addAll(uriList)
                val list = getOnePageData()
                imageUriList.postValue(list)
                //转化为bitmap
                val bitmapList = setImageBitmapList(list)
                imageBitmapList.postValue(bitmapList)
                page = 1
            }
        }
    }

    /**
     * 根据页数获取一页数据
     */
    fun getOnePageData(): MutableList<Uri> {
        val list = mutableListOf<Uri>()
        if ((page + 1) * 20 < imageDataList.size) {
            for (i in (page * 20) until (page + 1) * 20) {
                list.add(imageDataList[i])
            }
        } else {
            list.addAll(imageDataList)
        }
        page++
        return list
    }

    fun setImageBitmapList(list: MutableList<Uri>): MutableList<Bitmap> {
        val bitmapList = mutableListOf<Bitmap>()
        list.forEach {
            it.path?.let { path ->
                val bitmap = decodeUriToBitmap(path)
                bitmapList.add(bitmap)
            }
        }
        return bitmapList
    }

    fun decodeUriToBitmap(path: String): Bitmap {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
        val bitmap = BitmapFactory.decodeFile(path, option)

        initSampleSize(option)
        println("width:${option.outWidth},height:${option.outHeight}")
        option.inJustDecodeBounds = false
        option.inPreferredConfig = Bitmap.Config.RGB_565
        option.inMutable = true
        option.inBitmap = bitmap
        return BitmapFactory.decodeFile(path, option)
    }

    private fun initSampleSize(options: BitmapFactory.Options) {
        var i = 0
        while (options.outWidth shr i >= 360 && options.outHeight shr i >= 360) {
            i++
        }
        println("${options.outWidth shr i}----$i")
        options.inSampleSize = 2.0.pow(i.toDouble()).toInt()
    }

    fun getPicData(state: Int) {
        setPageWithState(state)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val list = getOnePageData()
                val bitmapList = setDataWithState(state, list)
                imageBitmapList.postValue(bitmapList)
            }
        }
    }

    fun setDataWithState(state: Int, list: MutableList<Uri>): MutableList<Bitmap> {
        val bitmapList = mutableListOf<Bitmap>()
        when (state) {
            REFRESH -> {
            }
            LOADMORE -> {
                imageBitmapList?.value?.let {
                    bitmapList.addAll(it)
                }
                val tmpList = mutableListOf<Uri>()
                imageUriList.value?.let {
                    tmpList.addAll(it)
                }
                tmpList.addAll(list)
                imageUriList.postValue(tmpList)
            }
        }
        bitmapList.addAll(setImageBitmapList(list))
        return bitmapList
    }

    fun setPageWithState(state: Int) {
        when (state) {
            REFRESH -> {
                page = 0
            }
        }
    }
}
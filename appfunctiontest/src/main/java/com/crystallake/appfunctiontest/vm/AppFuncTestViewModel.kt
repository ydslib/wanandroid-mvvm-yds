package com.crystallake.appfunctiontest.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.ToastUtils
import com.crystallake.apkpatchlib.NativeLib
import com.crystallake.appfunctiontest.AppFuncTest
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.system.measureTimeMillis

class AppFuncTestViewModel : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        LogUtils.e("异常了:${throwable.localizedMessage}")
        throwable.printStackTrace()
    }

    /**
     * 文件后缀名
     */
    private val suffix = "txt"

    /**
     * 旧文件
     */
    private val oldFile = File(PathUtils.getInternalAppFilesPath(), "old.$suffix")

    /**
     * 新文件
     */
    private val newFile = File(PathUtils.getInternalAppFilesPath(), "new.$suffix")

    /**
     * 补丁文件
     */
    private val patchFile = File(PathUtils.getInternalAppFilesPath(), "patch.$suffix")

    /**
     * 合并后的文件
     */
    private val combineFile = File(PathUtils.getInternalAppFilesPath(), "combine.$suffix")

    fun fileDiff() {
        viewModelScope.launch(exceptionHandler) {
            val measureTimeMillis = measureTimeMillis {
                withContext(Dispatchers.IO) {
                    if (!oldFile.exists() || !newFile.exists()) {
                        ToastUtils.showShort("对比包缺失")
                        return@withContext
                    }
                }
                val result = NativeLib.diff(
                    newFile.absolutePath,
                    oldFile.absolutePath,
                    patchFile.absolutePath
                )
            }
            LogUtils.i("生成补丁文件耗时:${measureTimeMillis}")
            LogUtils.i("oldFileSize:${FileUtils.getSize(oldFile)}")
            LogUtils.i("newFileSize:${FileUtils.getSize(newFile)}")
            LogUtils.i("patchFileSize:${FileUtils.getSize(patchFile)}")
        }
    }

    fun filePatch() {
        viewModelScope.launch(exceptionHandler) {
            val measureTimeMillis = measureTimeMillis {
                withContext(Dispatchers.IO){
                    LogUtils.e(PathUtils.getExternalAppFilesPath())
                    if (!oldFile.exists()||!patchFile.exists()){
                        ToastUtils.showShort("补丁文件或旧文件缺失")
                        return@withContext
                    }

                    /*合并补丁包，耗时操作，记得放在子线程  返回值 0表示成功*/
                    val result = NativeLib.patch(
                        oldFile.absolutePath,
                        patchFile.absolutePath,
                        combineFile.absolutePath
                    )
                }
            }
            LogUtils.i("合并补丁文件耗时:${measureTimeMillis}")
            LogUtils.i("newFile MD5:${FileUtils.getFileMD5ToString(newFile)}")
            LogUtils.i("combineFile MD5:${FileUtils.getFileMD5ToString(combineFile)}")
        }
    }

}
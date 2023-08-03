package com.crystallake.sdklog.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.crystallake.sdklog.util.JsonHelper
import java.io.Serializable

@Entity(tableName = "logDataModel")
data class LogDataModel(
    @PrimaryKey
    var key: String,
    var time: String? = null,
    var params: String? = null,
    var data: String? = null
) : Serializable {
    override fun toString(): String {
        return JsonHelper.toJson(this)
    }
}

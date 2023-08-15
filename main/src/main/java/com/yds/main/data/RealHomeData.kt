package com.yds.main.data

data class FuncItem(
    val icon: String? = null,
    val localIcon: Int? = null,
    val title: String? = null,
    val router: String? = null,
    val baseUrl: String? = null
)

data class FuncItemData(
    var itemType: Int? = null,
    val funcItemList: MutableList<FuncItem>? = null,
    val title:String?=null
)

data class RealHomeData(
    //功能入口
    var funcItemData: FuncItemData? = null,
    var titleItemData: FuncItemData?=null
)
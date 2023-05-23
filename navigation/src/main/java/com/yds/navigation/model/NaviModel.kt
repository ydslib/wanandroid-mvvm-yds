package com.yds.navigation.model

data class NaviModel(
    val name: String? = null,
    val articles: List<NaviArticle>? = null,
    val cid: Int? = null
)

data class NaviArticle(
    val adminAdd: Boolean? = null,
    val audit: Int? = null,
    val author: String? = null,
    val chapterId: Int? = null,
    val chapterName: String? = null,
    val courseId: Int? = null,
    val link: String? = null,
    val niceDate: String? = null,
    val niceShareDate: String? = null,
    val title: String? = null
)

package com.crystallake.knowledgehierarchy.model

data class KnowledgeModel(
    val children: List<KnowledgeModel>? = null,
    val courseId: Int? = null,
    val id: Int? = null,
    val name: String? = null,
    val order: Int? = null,
    val parentChapterId: Int? = null,
    val type: Int? = null,
    val userControlSetTop: Boolean? = null,
    val visible: Int? = null
)
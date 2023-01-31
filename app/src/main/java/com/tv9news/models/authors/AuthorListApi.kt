package com.tv9news.models.authors


class AuthorListApi(
    val status: Boolean,
    val message: String,
    val data: List<AuthorDataList>
)
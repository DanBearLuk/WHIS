package com.danluk.whis.json.classes

import com.google.gson.annotations.SerializedName

data class TitlesPage (
    @SerializedName("pageInfo" ) var pageInfo : PageInfo?        = PageInfo(),
    @SerializedName("media"    ) var media    : ArrayList<Media> = arrayListOf()
)

data class PageInfo (
    @SerializedName("total"       ) var total       : Int?     = null,
    @SerializedName("currentPage" ) var currentPage : Int?     = null,
    @SerializedName("lastPage"    ) var lastPage    : Int?     = null,
    @SerializedName("hasNextPage" ) var hasNextPage : Boolean? = null,
    @SerializedName("perPage"     ) var perPage     : Int?     = null
)
package com.danluk.whis.json.classes

import com.google.gson.annotations.SerializedName

data class WHISApiResponse (
    @SerializedName("success" ) var success : Boolean? = null,
    @SerializedName("message" ) var message : String?  = null,
    @SerializedName("user"    ) var user    : User?    = User()
)

data class SavedTitleInfo (
    @SerializedName("id"             ) var id             : Int?    = null,
    @SerializedName("status"         ) var status         : String? = null,
    @SerializedName("finishedAmount" ) var finishedAmount : Int?    = null
)

data class User (
    @SerializedName("username"  ) var username  : String?              = null,
    @SerializedName("savedList" ) var savedList : ArrayList<SavedTitleInfo> = arrayListOf()
)
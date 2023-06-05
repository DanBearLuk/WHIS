package com.danluk.whis.json.classes

import com.google.gson.annotations.SerializedName

data class WHISApiRequest (
    @SerializedName("edit"    ) var editList   : ArrayList<SavedTitleInfo> = arrayListOf(),
    @SerializedName("remove"  ) var removeList : ArrayList<Int> = arrayListOf()
)

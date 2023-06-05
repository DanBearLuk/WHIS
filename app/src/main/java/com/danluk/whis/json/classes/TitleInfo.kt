package com.danluk.whis.json.classes

import com.google.gson.annotations.SerializedName

data class TitleInfo (
    @SerializedName("Media" ) var Media : Media? = Media()
)

data class Title (
    @SerializedName("english" ) var english : String? = null,
    @SerializedName("romaji"  ) var romaji  : String? = null
)

data class CoverImage (
    @SerializedName("large" ) var large : String? = null
)

data class Media (
    @SerializedName("id"              ) var id              : Int?            = null,
    @SerializedName("type"            ) var type            : String?         = null,
    @SerializedName("title"           ) var title           : Title?          = Title(),
    @SerializedName("coverImage"      ) var coverImage      : CoverImage?     = CoverImage(),
    @SerializedName("countryOfOrigin" ) var countryOfOrigin : String?         = null,
    @SerializedName("source"          ) var source          : String?         = null,
    @SerializedName("format"          ) var format          : String?         = null,
    @SerializedName("startDate"       ) var startDate       : StartDate?      = StartDate(),
    @SerializedName("status"          ) var status          : String?         = null,
    @SerializedName("season"          ) var season          : String?         = null,
    @SerializedName("popularity"      ) var popularity      : Int?            = null,
    @SerializedName("tags"            ) var tags            : ArrayList<Tag> = arrayListOf(),
    @SerializedName("description"     ) var description     : String?         = null,
    @SerializedName("episodes"        ) var episodes        : Int?            = null,
    @SerializedName("chapters"        ) var chapters        : Int?            = null,
)

data class Tag (
    @SerializedName("name" ) var name : String? = null,
    @SerializedName("rank" ) var rank : Int?    = null
)

data class StartDate (
    @SerializedName("year"  ) var year  : Int? = null,
    @SerializedName("month" ) var month : Int? = null,
    @SerializedName("day"   ) var day   : Int? = null
)



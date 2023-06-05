package com.danluk.whis.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.danluk.whis.json.classes.TitleInfo
import com.danluk.whis.json.classes.TitlesPage
import com.danluk.whis.json.classes.WHISApiRequest
import com.danluk.whis.json.classes.WHISApiResponse
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.Charset

enum class TitleType {
    ANIME, MANGA, ALL
}

const val serverAddress = "http://192.168.1.152:2700"

fun getTitles(
    page: Int,
    pageSize: Int = 50,
    context: Context,
    type: TitleType? = null,
    name: String? = null,
    ids: Array<Int>? = null,
    callback: (TitlesPage) -> Unit
) {
    val url = "https://graphql.anilist.co"

    val pageArgs = "page: \$page, perPage: \$perPage"
    var queryArgs = "\$page: Int, \$perPage: Int"
    var mediaArgs = "sort: TRENDING_DESC"

    if (type != null && type != TitleType.ALL) {
        queryArgs += ", \$type: MediaType"
        mediaArgs += ", type: \$type"
    }
    if (name != null) {
        queryArgs += ", \$search: String"
        mediaArgs += ", search: \$search"
    }
    if (ids != null) {
        queryArgs += ", \$ids: [Int]"
        mediaArgs += ", id_in: \$ids"
    }

    val query = """
        query (${queryArgs}) {
            Page (${pageArgs}) {
                pageInfo {
                    total
                    currentPage
                    lastPage
                    hasNextPage
                    perPage
                }
                media (${mediaArgs}) {
                    id
                    title {
                        english
                        romaji
                    }
                    description
                    coverImage {
                        large
                    }
                    type
                    episodes
                    chapters
                }
            }
        }
    """.trimIndent()
    val variables = listOfNotNull(
        "page" to page,
        "perPage" to pageSize,
        "type" to type?.name,
        "search" to name,
        "ids" to ids
    ).toMap()

    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url,
        JSONObject(mapOf(
            "query" to query,
            "variables" to variables
        )),
        {   val titlesPage = it.getJSONObject("data").getJSONObject("Page")
            callback(Gson().fromJson(titlesPage.toString(), TitlesPage::class.java)) },
        { error ->
            error.fillInStackTrace()
        }
    )

    VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest)
}

fun getTitleInfo(
    id: Int,
    context: Context,
    callback: (TitleInfo) -> Unit
) {
    val url = "https://graphql.anilist.co"

    val query = """
        query (${"$"}id: Int) {
            Media (id: ${"$"}id) {
                type
                title {
                  english
                  romaji
                }
                coverImage {
                  large
                }
                countryOfOrigin
                source
                format
                startDate {
                  year
                  month
                  day
                }
                status
                season
                popularity
                tags {
                  name
                  rank
                }
                description(asHtml: false)
            }
        }
    """.trimIndent()
    val variables = listOfNotNull(
        "id" to id
    ).toMap()

    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url,
        JSONObject(mapOf(
            "query" to query,
            "variables" to variables
        )),
        {
            val titleInfo = it.getJSONObject("data")
            callback(Gson().fromJson(titleInfo.toString(), TitleInfo::class.java))
        },
        { error ->
            error.fillInStackTrace()
        }
    )

    VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest)
}

fun createAccount(
    username: String,
    password: String,
    context: Context,
    successCallback: (WHISApiResponse) -> Unit,
    errorCallback: ((WHISApiResponse) -> Unit)? = null
) {
    val url = "${serverAddress}/api/users/createAccount"

    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url,
        JSONObject(mapOf(
            "username" to username,
            "password" to password
        )),
        {
            successCallback(Gson().fromJson(it.toString(), WHISApiResponse::class.java))
        },
        {
            if (errorCallback == null) return@JsonObjectRequest

            errorCallback(getResponseFromError(it))
        }
    )

    VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest)
}

fun login(
    username: String,
    password: String,
    context: Context,
    successCallback: (WHISApiResponse) -> Unit,
    errorCallback: ((WHISApiResponse) -> Unit)? = null
) {
    val url = "${serverAddress}/api/users/login"

    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url,
        JSONObject(mapOf(
            "username" to username,
            "password" to password
        )),
        {
            successCallback(Gson().fromJson(it.toString(), WHISApiResponse::class.java))
        },
        {
            if (errorCallback == null) return@JsonObjectRequest

            errorCallback(getResponseFromError(it))
        }
    )

    VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest)
}

fun loginByToken(
    context: Context,
    successCallback: (WHISApiResponse) -> Unit,
    errorCallback: ((WHISApiResponse) -> Unit)? = null
) {
    val url = "${serverAddress}/api/users/login"

    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url,
        JSONObject(),
        {
            successCallback(Gson().fromJson(it.toString(), WHISApiResponse::class.java))
        },
        {
            if (errorCallback == null) return@JsonObjectRequest

            errorCallback(getResponseFromError(it))
        }
    )

    VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest)
}

fun editList(
    request: WHISApiRequest,
    context: Context,
    successCallback: (WHISApiResponse) -> Unit,
    errorCallback: ((WHISApiResponse) -> Unit)? = null
) {
    val url = "${serverAddress}/api/editList"

    val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url,
        JSONObject(Gson().toJson(request)),
        {
            successCallback(Gson().fromJson(it.toString(), WHISApiResponse::class.java))
        },
        {
            if (errorCallback == null) return@JsonObjectRequest

            errorCallback(getResponseFromError(it))
        }
    )

    VolleySingletone.getInstance(context).addToRequestQueue(jsonObjectRequest)
}

fun deauthorize(
    context: Context,
    successCallback: (WHISApiResponse) -> Unit,
    errorCallback: ((WHISApiResponse) -> Unit)? = null
) {
    val url = "${serverAddress}/api/users/deauthorize"

    val stringRequest = JsonObjectRequest(Request.Method.GET, url,
        JSONObject(),
        {
            successCallback(Gson().fromJson(it.toString(), WHISApiResponse::class.java))
        },
        {
            if (errorCallback != null) {
                errorCallback(WHISApiResponse(false, "Unknown error"))
            }
        }
    ).setShouldRetryServerErrors(false)

    VolleySingletone.getInstance(context).addToRequestQueue(stringRequest)
}

fun getResponseFromError(error: VolleyError): WHISApiResponse {
    val data = error.networkResponse.data

    return if (data == null) {
        WHISApiResponse(false, "Unknown error")
    } else {
        val text = String(data, Charset.defaultCharset())

        if (text[0] == '{') {
            Gson().fromJson(text, WHISApiResponse::class.java)
        } else {
            WHISApiResponse(false, "Unknown error")
        }
    }
}

class VolleySingletone constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingletone? = null
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingletone(context).also {
                    INSTANCE = it
                }
            }
    }

    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue.add(req)
    }
}

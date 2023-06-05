package com.danluk.whis.classes

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.danluk.whis.api.createAccount
import com.danluk.whis.api.deauthorize
import com.danluk.whis.api.editList
import com.danluk.whis.api.getTitles
import com.danluk.whis.api.loginByToken
import com.danluk.whis.api.login
import com.danluk.whis.json.classes.Media
import com.danluk.whis.json.classes.SavedTitleInfo
import com.danluk.whis.json.classes.User
import com.danluk.whis.json.classes.WHISApiRequest
import com.danluk.whis.json.classes.WHISApiResponse

class User {
    var isAuthorized by mutableStateOf(false)
    var username by mutableStateOf<String?>(null)
    val savedTitlesList = mutableStateListOf<SavedTitleInfo>()
    val savedTitlesMedia = mutableStateListOf<Media>()

    fun editSavedList(
        context: Context,
        edit: ArrayList<SavedTitleInfo> = arrayListOf(),
        remove: ArrayList<Int> = arrayListOf()
    ) {
        editList(
            WHISApiRequest(
                edit,
                remove
            ),
            context,
            { response ->
                val newSavedList = response.user!!.savedList

                val ids = newSavedList.map { it.id!! }
                val newIds = ids
                    .filter { id -> !this.savedTitlesList.any { it.id == id }}
                val removedIds = this.savedTitlesList
                    .filter { title -> !ids.any { it == title.id }}
                    .map { it.id }

                getTitles(1, newIds.size, context, ids = newIds.toTypedArray()) { titles ->
                    this.savedTitlesList.clear()
                    this.savedTitlesList.addAll(newSavedList)

                    this.savedTitlesMedia.removeIf { removedIds.contains(it.id) }
                    this.savedTitlesMedia.addAll(titles.media)
                }
            }
        )
    }

    fun signIn(
        context: Context,
        username: String,
        password: String,
        errorCallback: ((WHISApiResponse) -> Unit)? = null
    ) {
        createAccount(
            username,
            password,
            context,
            {
                updateUser(context, it.user!!)
            },
            {
                errorCallback?.invoke(it)
            }
        )
    }

    fun logIn(
        context: Context,
        username: String,
        password: String,
        errorCallback: ((WHISApiResponse) -> Unit)? = null
    ) {
        login(
            username,
            password,
            context,
            {
                updateUser(context, it.user!!)
            },
            {
                errorCallback?.invoke(it)
            }
        )
    }

    fun tryToLoginByToken(context: Context) {
        loginByToken(
            context,
            {
                updateUser(context, it.user!!)
            }
        )
    }

    fun deauth(context: Context) {
        deauthorize(
            context,
            successCallback = {
                this.isAuthorized = false
            }
        )
    }

    private fun updateUser(context: Context, user: User) {
        this.isAuthorized = true
        this.username = user.username

        this.savedTitlesList.clear()
        this.savedTitlesList.addAll(user.savedList)

        val ids = user.savedList.map { it.id!! }
        getTitles(1, ids.size, context, ids = ids.toTypedArray()) { titles ->
            this.savedTitlesMedia.clear()
            this.savedTitlesMedia.addAll(titles.media)
        }
    }
}

package com.danluk.whis

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.danluk.whis.api.TitleType
import com.danluk.whis.classes.User
import com.danluk.whis.components.NavBar
import com.danluk.whis.data.TitlesPagingSource
import com.danluk.whis.json.classes.Media
import com.danluk.whis.ui.theme.WHISTheme
import com.danluk.whis.utils.SaveableCookieStore
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@ExperimentalFoundationApi
@ExperimentalLayoutApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = this.applicationContext

            val saveableCookieStore = SaveableCookieStore(context)
            val cookieManager = CookieManager(saveableCookieStore, CookiePolicy.ACCEPT_ALL)
            CookieHandler.setDefault(cookieManager)

            val navController = rememberAnimatedNavController()

            val viewModel: WHISViewModel = viewModel(
                factory = WHISViewModelFactory(context.applicationContext)
            )

            LaunchedEffect(Unit) {
                viewModel.user.tryToLoginByToken(context)
            }

            WHISTheme {
                Scaffold(
                    containerColor = Color.Transparent,
                    bottomBar = { NavBar(navController) }
                ) { innerPadding ->
                    AnimationNav(navController, innerPadding = innerPadding)
                }
            }
        }
    }
}

val types = listOf(TitleType.ANIME, TitleType.ALL, TitleType.MANGA)

class WHISViewModel(
    val context: Context,
) : ViewModel() {
    val titlesMap = mapOf(
        *(
            types.map {
                it to Pager(
                    PagingConfig(pageSize = 50)
                ) {
                    TitlesPagingSource(context, type = it)
                }.flow.cachedIn(viewModelScope)
            }
        ).toTypedArray()
    )

    val titlesGridState = mapOf(
        *(
            types.map {
                it to LazyGridState()
            }
        ).toTypedArray()
    )

    var searchPrompt: String = ""
    var searchResults: MutableState<Flow<PagingData<Media>>?> = mutableStateOf(null)

    val user = User()
}

class WHISViewModelFactory(
    val context: Context
): ViewModelProvider.Factory {
    @Override
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WHISViewModel(context) as T
    }
}

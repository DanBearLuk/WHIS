package com.danluk.whis.classes

import androidx.annotation.DrawableRes
import com.danluk.whis.R

sealed class Screen(
    val route: String,
    @DrawableRes val iconId: Int = -1
) {
    object Home: Screen(
        "Home",
        R.drawable.home
    )

    object Profile: Screen(
        "Profile",
        R.drawable.person
    )

    object Search: Screen(
        "Search",
        R.drawable.search
    )

    object TitleInfo: Screen(
        "TitleInfo/{titleId}"
    )
}
package com.danluk.whis.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.EaseInQuad
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.danluk.whis.classes.Screen
import com.danluk.whis.utils.navigateTo
import com.danluk.whis.utils.shadow
import com.danluk.whis.utils.toDp
import com.danluk.whis.utils.toPx

@Composable
fun NavBar(navController: NavHostController) {
    val screens = listOf(
        Screen.Search,
        Screen.Home,
        Screen.Profile
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val initialX = (screenWidth.dp / 2F - 8.dp).value.toPx
    var elemCords by remember { mutableStateOf(Offset(initialX, 30.toPx)) }

    val indicatorXProgression by animateIntAsState(
        targetValue = (elemCords.x - 23.toPx).toInt(),
        animationSpec = tween(
            250,
            easing = CubicBezierEasing(.0F, .0F, .41F, 1.5F)
        )
    )

    val indicatorAnimProgression by animateFloatAsState(
        targetValue = if (currentDestination?.route?.contains("TitleInfo") == true) 0F else 1F,
        animationSpec = tween(250, easing = EaseInQuad)
    )

    Box(
        modifier = Modifier
            .padding(8.dp, 0.dp, 8.dp, 8.dp)
            .fillMaxWidth()
            .height(60.dp)
            .shadow(
                color = MaterialTheme.colorScheme.outlineVariant,
                borderRadius = 46.dp,
                blurRadius = 8.dp
            )
            .background(
                MaterialTheme.colorScheme.surface,
                shape = CircleShape
            )
    ) {
        NavBarIndicator(
            indicatorXProgression.toDp,
            7.dp,
            indicatorAnimProgression
        )

        LazyRow(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(screens.size, key = { index -> screens[index].route!! }) { index ->
                val screen = screens[index]

                val selected = currentDestination
                    ?.hierarchy
                    ?.any { it.route == screen.route } == true

                Box(
                    modifier = Modifier
                        .height(46.dp)
                        .width(46.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                navigateTo(
                                    navController,
                                    screen.route
                                )
                            }
                        )
                        .onGloballyPositioned { cords ->
                            if (selected) {
                                elemCords = cords.boundsInParent().center
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painterResource(id = screen.iconId),
                        contentDescription = screen.route,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .height(28.dp)
                            .width(28.dp)
                    )
                }
            }
        }
    }
}

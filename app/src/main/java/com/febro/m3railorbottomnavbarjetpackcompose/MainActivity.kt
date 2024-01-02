package com.febro.m3railorbottomnavbarjetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.febro.m3railorbottomnavbarjetpackcompose.ui.theme.M3RailOrBottomNavBarJetpackComposeTheme

data class NavigationItem(
    val title: String,
    val unselectedIcon: ImageVector,
    val selectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
)

// ref: https://www.youtube.com/watch?v=c8XP_Ee7iqY
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val items = listOf(
                NavigationItem(
                    title = "Home",
                    selectedIcon = Icons.Filled.Home,
                    unselectedIcon = Icons.Outlined.Home,
                    hasNews = false,
                ),
                NavigationItem(
                    title = "Chat",
                    selectedIcon = Icons.Filled.Email,
                    unselectedIcon = Icons.Outlined.Email,
                    hasNews = false,
                    badgeCount = 45
                ),
                NavigationItem(
                    title = "Settings",
                    selectedIcon = Icons.Filled.Settings,
                    unselectedIcon = Icons.Outlined.Settings,
                    hasNews = true,
                ),
            )

            M3RailOrBottomNavBarJetpackComposeTheme(
                darkTheme = false
            ) {
                val windowClass = calculateWindowSizeClass(this)
                val showNavigationRail = windowClass.widthSizeClass != WindowWidthSizeClass.Compact

                // It behaves similarly to remember, but the stored value will survive the activity or process recreation using the saved instance state mechanism (for example it happens when the screen is rotated in the Android application)
                var selectedItemIndex by rememberSaveable {
                    mutableStateOf(0)
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(

                        bottomBar = {
                            if (!showNavigationRail) {
                                NavigationBar {
                                    items.forEachIndexed { index, item ->
                                        NavigationBarItem(
                                            selected = selectedItemIndex == index,
                                            onClick = {
                                                selectedItemIndex = index
                                                // navController.navigate(item.title)
                                            },
                                            alwaysShowLabel = false,
                                            label = {
                                                Text(text = item.title)
                                            },
                                            icon = {
                                                BadgedBox(
                                                    badge = {
                                                        if(item.badgeCount != null) {
                                                            Badge {
                                                                Text(
                                                                    text = item.badgeCount.toString()
                                                                )
                                                            }
                                                        } else if(item.hasNews) {
                                                            Badge()
                                                        }
                                                    }
                                                ) {
                                                    Icon(
                                                        imageVector = if (index == selectedItemIndex) {
                                                            item.selectedIcon
                                                        } else item.unselectedIcon,
                                                        contentDescription = item.title
                                                    )
                                                }

                                            }
                                        )
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                                .padding(
                                    start = if (showNavigationRail) 80.dp else 0.dp
                                )
                        ) {
                            items(100) {
                                Text(
                                    text = "Item$it",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                    }

                }
                if (showNavigationRail) {
                    NavigationSideBar(
                        items = items,
                        selectedItemIndex = selectedItemIndex,
                        onNavigate = { selectedItemIndex = it }
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationSideBar(
    items: List<NavigationItem>,
    selectedItemIndex: Int,
    onNavigate: (Int) -> Unit
) {
    NavigationRail(
//        header = {
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    imageVector = Icons.Default.Menu,
//                    contentDescription = "Menu"
//                )
//            }
//            FloatingActionButton(
//                onClick = { /*TODO*/ },
//                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Add"
//                )
//            }
//        },
        // thin line alongside rail navigation
        modifier = Modifier
            .background(MaterialTheme.colorScheme.inverseOnSurface)
            .offset(x = (-1).dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Bottom)
        ) {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    alwaysShowLabel = false,
                    selected = selectedItemIndex == index,
                    onClick = {
                        onNavigate(index)
                    },
                    icon = {
                        NavigationIcon(
                            item = item,
                            selected = selectedItemIndex == index
                        )
                    },
                    label = {
                        Text(text = item.title)
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationIcon(
    item: NavigationItem,
    selected: Boolean
) {
    BadgedBox(
        badge = {
            if (item.badgeCount != null) {
                Badge {
                    Text(text = item.badgeCount.toString())
                }
            } else if (item.hasNews) {
                Badge()
            }
        }
    ) {
        Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title
        )
    }
}
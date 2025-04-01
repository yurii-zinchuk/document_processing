package com.example.scandoc.presentation.screens.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.scandoc.presentation.screens.details.components.entities.TabEntities
import com.example.scandoc.presentation.screens.details.components.toolbar.Toolbar
import com.example.scandoc.presentation.screens.details.components.photos.TabPhotos
import com.example.scandoc.presentation.screens.details.components.tab_selector.Tab
import com.example.scandoc.presentation.screens.details.components.tab_selector.TabSelector
import com.example.scandoc.presentation.screens.details.components.text.TabText
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DetailsScreen(
    vm: DetailsScreenVM,
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState { Tab.entries.size }
        Toolbar(navController)
        TabSelector(pagerState.currentPage) {
            scope.launch {
                pagerState.animateScrollToPage(
                    Tab.entries.indexOf(it)
                )
            }
        }
        HorizontalPager(
            modifier =
            Modifier
                .fillMaxSize(),
            state = pagerState,
        ) {
            Tab.entries[it].let { tab ->
                when (tab) {
                    Tab.RAW -> TabPhotos(vm.photos.value)
                    Tab.TEXT -> TabText(vm.text.value)
                    Tab.ENTITIES -> TabEntities()
                }
            }
        }
    }
}

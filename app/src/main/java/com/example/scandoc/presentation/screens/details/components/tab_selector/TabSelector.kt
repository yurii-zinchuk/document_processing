package com.example.scandoc.presentation.screens.details.components.tab_selector

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
@ExperimentalMaterial3Api
fun TabSelector(
    selectedTabIndex: Int = 0,
    onTabSelected: (Tab) -> Unit,
) {
    var currentSelectedTabIndex by remember { mutableIntStateOf(selectedTabIndex) }
    PrimaryTabRow(
        selectedTabIndex = currentSelectedTabIndex,
    ) {
        Tab.entries.forEachIndexed { index, tab ->
            Tab(
                selected = index == selectedTabIndex,
                text = { Text(tab.displayName) },
                onClick = {
                    currentSelectedTabIndex = index
                    onTabSelected.invoke(tab)
                },
            )
        }
    }
    LaunchedEffect(selectedTabIndex) {
        currentSelectedTabIndex = selectedTabIndex
    }
}

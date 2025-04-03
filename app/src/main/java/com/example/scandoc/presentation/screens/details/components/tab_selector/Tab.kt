package com.example.scandoc.presentation.screens.details.components.tab_selector

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.scandoc.R

enum class Tab {
    PHOTOS,
    TEXT,
    ENTITIES;

    val displayName: String
        @Composable
        get() = stringResource(
            when (this) {
                PHOTOS -> R.string.details_screen_tab_photos_title
                TEXT -> R.string.details_screen_tab_text_title
                ENTITIES -> R.string.details_screen_tab_entities_title
            }
        )
}

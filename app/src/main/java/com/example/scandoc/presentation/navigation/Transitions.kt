package com.example.scandoc.presentation.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.navigation.NavBackStackEntry

private const val ANIMATION_DURATION = 300

val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        animationSpec = tween(ANIMATION_DURATION, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
    ) +
        fadeIn(
            animationSpec = tween(ANIMATION_DURATION, easing = LinearEasing),
        )
}

val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(
        animationSpec = tween(ANIMATION_DURATION, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.Start,
    )
}

val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    slideIntoContainer(
        animationSpec = tween(ANIMATION_DURATION, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.End,
    )
}

val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    slideOutOfContainer(
        animationSpec = tween(ANIMATION_DURATION, easing = LinearEasing),
        towards = AnimatedContentTransitionScope.SlideDirection.End,
    )
}

package com.template.apptemplate.ui

enum class Screen {
    HOME,
}

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
}
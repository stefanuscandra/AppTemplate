package com.template.apptemplate.ui

enum class Screen {
    HOME, DETAIL
}

sealed class NavigationItem(val route: String) {
    object Home : NavigationItem(Screen.HOME.name)
    object Detail : NavigationItem(Screen.DETAIL.name)

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach {
                append("/$it")
            }
        }
    }
}
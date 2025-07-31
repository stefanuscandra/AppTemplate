package com.template.apptemplate.ui

enum class Screen {
    MAIN,
    DETAIL,
}
sealed class NavigationItem(val route: String) {
    object Main : NavigationItem(Screen.MAIN.name)
    object Detail : NavigationItem("${Screen.DETAIL.name}?id={id}")
}
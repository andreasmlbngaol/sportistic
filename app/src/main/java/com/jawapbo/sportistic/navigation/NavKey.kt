@file:Suppress("unused")

package com.jawapbo.sportistic.navigation

import kotlinx.serialization.Serializable


sealed class NavKey {
    @Serializable
    data object Splash: NavKey()

    @Serializable
    data object SignIn: NavKey()

    @Serializable
    data object SignUp: NavKey()

    @Serializable
    data object Home: NavKey()

    @Serializable
    data object Maps: NavKey()
}
package com.example.watsapporder.presentation.screens.routes

import cafe.adriel.voyager.core.screen.Screen
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.presentation.screens.home.HomeScreen
import com.example.watsapporder.presentation.screens.login.LoginScreen
import com.example.watsapporder.presentation.screens.login.email.EmailLoginScreen
import com.example.watsapporder.presentation.screens.login.phone.PhoneLoginScreen

object Routes {
    val LOGIN_SCREEN = LoginScreen()

    val EMAIL_LOGIN_SCREEN = EmailLoginScreen()

    val PHONE_LOGIN_SCREEN = PhoneLoginScreen()

    val HOME_SCREEN: (LoggedUser) -> Screen = { loggedUser ->
        HomeScreen(loggedUser)
    }
}

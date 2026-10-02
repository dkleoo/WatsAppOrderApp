package com.example.watsapporder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.example.watsapporder.data.di.initKoin
import com.example.watsapporder.presentation.screens.routes.Routes
import com.example.watsapporder.presentation.theme.AppTheme

@Composable
@Preview
fun App() {
    remember { initKoin() }
    AppTheme {
        Navigator(screen = Routes.LOGIN_SCREEN) {
            CurrentScreen()
        }
    }
}

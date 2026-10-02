package com.example.watsapporder.platform

import com.example.watsapporder.data.local.InMemorySessionStore
import com.example.watsapporder.data.local.SessionStore
import com.example.watsapporder.data.remote.login.FirebaseAuthGateway
import com.example.watsapporder.data.remote.login.UnsupportedFirebaseAuthGateway
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

internal val unsupportedPlatformModule = module {
    single<FirebaseAuthGateway> { UnsupportedFirebaseAuthGateway() }
    single<SessionStore> { InMemorySessionStore() }
}

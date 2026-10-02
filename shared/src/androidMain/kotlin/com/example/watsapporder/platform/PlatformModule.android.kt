package com.example.watsapporder.platform

import com.example.watsapporder.data.local.AndroidSessionStore
import com.example.watsapporder.data.local.SessionStore
import com.example.watsapporder.data.remote.login.AndroidFirebaseAuthGateway
import com.example.watsapporder.data.remote.login.FirebaseAuthGateway
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<FirebaseAuthGateway> { AndroidFirebaseAuthGateway() }
    single<SessionStore> { AndroidSessionStore() }
}

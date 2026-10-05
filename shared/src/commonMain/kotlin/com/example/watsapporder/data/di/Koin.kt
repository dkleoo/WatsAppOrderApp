package com.example.watsapporder.data.di

import com.example.watsapporder.data.remote.input.InputsServices
import com.example.watsapporder.data.remote.input.InputsServicesImpl
import com.example.watsapporder.data.remote.login.AuthServices
import com.example.watsapporder.data.remote.login.AuthServicesImpl
import com.example.watsapporder.data.remote.order.OrdersServices
import com.example.watsapporder.data.remote.order.OrdersServicesImpl
import com.example.watsapporder.data.remote.order.OrdersSocketDataSource
import com.example.watsapporder.data.remote.product.ProductsServices
import com.example.watsapporder.data.remote.product.ProductsServicesImpl
import com.example.watsapporder.data.remote.store.StoreServices
import com.example.watsapporder.data.remote.store.StoreServicesImpl
import com.example.watsapporder.data.repositoyImpl.input.InputsRepositoryImpl
import com.example.watsapporder.data.repositoyImpl.login.AuthRepositoryImpl
import com.example.watsapporder.data.repositoyImpl.order.OrdersRepositoryImpl
import com.example.watsapporder.data.repositoyImpl.product.ProductsRepositoryImpl
import com.example.watsapporder.data.repositoyImpl.store.StoreRepositoryImpl
import com.example.watsapporder.domain.repository.input.InputsRepository
import com.example.watsapporder.domain.repository.login.AuthRepository
import com.example.watsapporder.domain.repository.order.OrdersRepository
import com.example.watsapporder.domain.repository.product.ProductsRepository
import com.example.watsapporder.domain.repository.store.StoreRepository
import com.example.watsapporder.domain.useCase.input.InputsUseCases
import com.example.watsapporder.domain.useCase.login.AuthUseCases
import com.example.watsapporder.domain.useCase.order.OrdersUseCases
import com.example.watsapporder.domain.useCase.product.ProductsUseCases
import com.example.watsapporder.data.mappers.LoggedUser
import com.example.watsapporder.domain.useCase.store.StoreUseCases
import com.example.watsapporder.platform.platformModule
import com.example.watsapporder.presentation.screens.home.HomeViewModel
import com.example.watsapporder.presentation.screens.home.create.CreateProductViewModel
import com.example.watsapporder.presentation.screens.home.inputs.InputsViewModel
import com.example.watsapporder.presentation.screens.home.orders.OrdersViewModel
import com.example.watsapporder.presentation.screens.home.store.StoreViewModel
import com.example.watsapporder.presentation.screens.login.LoginViewModel
import com.example.watsapporder.presentation.screens.login.email.EmailLoginViewModel
import com.example.watsapporder.presentation.screens.login.phone.PhoneLoginViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.mp.KoinPlatform

private val networkModule = module {
    single {
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(get<Json>())
            }
            install(WebSockets)
        }
    }
    single { OrdersSocketDataSource(get(), get()) }
}

private val serverModule = module {
    single<AuthServices> { AuthServicesImpl(get()) }
    single<ProductsServices> { ProductsServicesImpl(get()) }
    single<InputsServices> { InputsServicesImpl(get()) }
    single<StoreServices> { StoreServicesImpl(get()) }
    single<OrdersServices> { OrdersServicesImpl(get()) }
}

private val repositoryModule = module {
    single<AuthRepository> { AuthRepositoryImpl(get(), get(), get()) }
    single<ProductsRepository> { ProductsRepositoryImpl(get(), get()) }
    single<InputsRepository> { InputsRepositoryImpl(get(), get()) }
    single<StoreRepository> { StoreRepositoryImpl(get(), get()) }
    single<OrdersRepository> { OrdersRepositoryImpl(get(), get(), get()) }
}

private val useCasesModule = module {
    factory { AuthUseCases(get()) }
    factory { ProductsUseCases(get()) }
    factory { InputsUseCases(get()) }
    factory { StoreUseCases(get()) }
    factory { OrdersUseCases(get()) }
}

private val viewModelModule = module {
    factory { LoginViewModel(get()) }
    factory { EmailLoginViewModel(get()) }
    factory { PhoneLoginViewModel(get()) }
    factory { HomeViewModel(get()) }
    factory { InputsViewModel(get()) }
    factory { CreateProductViewModel(get(), get()) }
    factory { (user: LoggedUser) -> StoreViewModel(get(), user) }
    factory { OrdersViewModel(get()) }
}

val appModule = module {
    includes(
        networkModule,
        platformModule,
        serverModule,
        repositoryModule,
        useCasesModule,
        viewModelModule,
    )
}

fun initKoin() {
    if (KoinPlatform.getKoinOrNull() == null) {
        startKoin { modules(appModule) }
    }
}

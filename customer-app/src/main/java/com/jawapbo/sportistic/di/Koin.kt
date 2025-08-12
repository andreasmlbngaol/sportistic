package com.jawapbo.sportistic.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import com.jawapbo.sportistic.core.data.AuthDataStoreManagerImpl
import com.jawapbo.sportistic.core.data.AuthDataStoreSerializer
import com.jawapbo.sportistic.core.model.AccountService
import com.jawapbo.sportistic.core.model.FirebaseAccountService
import com.jawapbo.sportistic.features.auth.sign_in.SignInViewModel
import com.jawapbo.sportistic.features.main.home.HomeViewModel
import com.jawapbo.sportistic.features.main.maps.MapsViewModel
import com.jawapbo.sportistic.features.splash.SplashViewModel
import com.jawapbo.sportistic.shared.data.auth.AuthDataStore
import com.jawapbo.sportistic.shared.data.core.AuthDataStoreManager
import com.jawapbo.sportistic.shared.data.core.HttpClientFactory
import com.jawapbo.sportistic.shared.data.core.SportisticRepository
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import java.io.File

fun initKoin(
    appContext: Context,
    config: KoinAppDeclaration? = null
) {
    startKoin {
        androidContext(appContext)
        config?.invoke(this)
        modules(modules)
    }
}

val modules = module {
    single<AccountService> { FirebaseAccountService() }
    single<HttpClientEngine> { CIO.create() }
    single { HttpClientFactory.create(get(), get()) }
    single<AuthDataStoreManager> { AuthDataStoreManagerImpl(get()) }
    single<DataStore<AuthDataStore>> {
        DataStoreFactory.create(
            serializer = AuthDataStoreSerializer,
            produceFile = { File(androidContext().filesDir, "auth_tokens.json") }
        )
    }

    single { SportisticRepository(get()) }

    viewModelOf(::SignInViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::MapsViewModel)
}
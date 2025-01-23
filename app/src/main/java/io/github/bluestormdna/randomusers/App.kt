package io.github.bluestormdna.randomusers

import android.app.Application
import io.github.bluestormdna.randomusers.di.appModule
import io.github.bluestormdna.randomusers.di.networkModule
import io.github.bluestormdna.randomusers.di.persistenceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(appModule, persistenceModule, networkModule)
        }
    }
}
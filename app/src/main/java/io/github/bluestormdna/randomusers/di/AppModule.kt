package io.github.bluestormdna.randomusers.di

import io.github.bluestormdna.randomusers.data.paging.UserRemoteMediator
import io.github.bluestormdna.randomusers.data.repositories.DefaultUserRepository
import io.github.bluestormdna.randomusers.domain.repositories.UserRepository
import io.github.bluestormdna.randomusers.ui.userdetails.UserDetailsViewModel
import io.github.bluestormdna.randomusers.ui.users.UsersViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::UserRemoteMediator)
    singleOf(::DefaultUserRepository) { bind<UserRepository>() }
    factory { CoroutineScope(Dispatchers.Main.immediate + SupervisorJob()) }
    viewModelOf(::UsersViewModel)
    viewModelOf(::UserDetailsViewModel)
}

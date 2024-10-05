package com.ivan.spaceflightnews.di

import com.ivan.spaceflightnews.MainNavHostViewModel
import com.ivan.spaceflightnews.screens.details.DetailsViewModel
import com.ivan.spaceflightnews.common.LoginCore
import com.ivan.spaceflightnews.common.LoginDataStorage
import com.ivan.spaceflightnews.jobs.LogoutAfterTenMinutesWork
import com.ivan.spaceflightnews.screens.main.MainScreenViewModel
import com.ivan.spaceflightnews.screens.search.RecentSearchTermsStorage
import com.ivan.spaceflightnews.screens.search.SearchScreenViewModel
import com.ivan.spaceflightnews.screens.section.SectionScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.workmanager.dsl.worker
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    viewModel {
        MainScreenViewModel(
            get(named("articlesDataSource")),
            get(named("blogsDataSource")),
            get(named("reportsDataSource")),
            get()
        )
    }
    viewModel {
        SectionScreenViewModel(
            get()
        )
    }
    viewModel { SearchScreenViewModel(get()) }
    viewModel { DetailsViewModel() }

    viewModel { MainNavHostViewModel(get()) }

    single<LoginDataStorage> { LoginDataStorage(get()) }
    single<LoginCore> { LoginCore(androidContext(), get()) }

    single<RecentSearchTermsStorage> { RecentSearchTermsStorage(get()) }

    worker { LogoutAfterTenMinutesWork(get(), get(), get()) }
}
package ru.gms.mosopencontrol.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DependencyModule {

    @Provides
    fun provideContext(@ApplicationContext appContext: Context): Context =
        appContext

}

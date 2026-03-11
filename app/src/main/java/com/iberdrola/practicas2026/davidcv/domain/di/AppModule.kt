package com.iberdrola.practicas2026.davidcv.domain.di

import android.content.Context
import android.content.res.Resources
import com.iberdrola.practicas2026.davidcv.data.local.dao.BillDao
import com.iberdrola.practicas2026.davidcv.data.local.database.BillDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
//? Todos los modulos a los que haga referencia terminaran su ciclo de vida a la par con la de la clase Application
object AppModule {

    @Provides
    @Singleton
    fun provideResources ( @ApplicationContext context: Context ) : Resources {
        return context.resources
    }

    @Provides
    @Singleton
    fun provideBillDatabase ( @ApplicationContext context: Context ) : BillDatabase {
        return BillDatabase.getDatabase(context)
    }

    @Provides
    @Singleton
    fun provideBillDaO ( database : BillDatabase ) : BillDao {
        return database.billDao()
    }


}
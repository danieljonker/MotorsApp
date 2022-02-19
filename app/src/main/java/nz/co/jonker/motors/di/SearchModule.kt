package nz.co.jonker.motors.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import nz.co.jonker.motors.SearchRepo
import nz.co.jonker.motors.SearchRepoImpl
import nz.co.jonker.motors.SearchService
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
abstract class SearchModule {

    @Binds
    abstract fun bindSearchRepo(
        repoImpl: SearchRepoImpl
    ): SearchRepo

    companion object {
        @Provides
        fun providesSearchService(retrofit: Retrofit): SearchService {
            return retrofit.create(SearchService::class.java)
        }
    }
}
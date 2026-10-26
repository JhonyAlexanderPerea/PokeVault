package co.edu.uniquindio.pokevault.di

import co.edu.uniquindio.pokevault.data.api.PokeApiService
import co.edu.uniquindio.pokevault.data.repository.PokemonRepositoryImpl
import co.edu.uniquindio.pokevault.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providePokeApiService(): PokeApiService {
        return Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokeApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonRepository(apiService: PokeApiService): PokemonRepository {
        return PokemonRepositoryImpl(apiService)
    }
}

package ru.xmn.filmfilmfilm.services.film

import dagger.Module
import dagger.Provides
import ru.xmn.filmfilmfilm.services.film.FilmDataManager
import ru.xmn.filmfilmfilm.services.kudago.KudaGoManager
import ru.xmn.filmfilmfilm.services.omdb.OmdbManager
import ru.xmn.filmfilmfilm.services.tmdb.TmdbManager
import javax.inject.Singleton

@Module
class FilmDataModule {
    @Provides @Singleton
    fun providesFilmDataManager(tmdb: TmdbManager, omdb: OmdbManager, kudaGo: KudaGoManager): FilmDataManager
            = FilmDataManager(tmdb, omdb)
}
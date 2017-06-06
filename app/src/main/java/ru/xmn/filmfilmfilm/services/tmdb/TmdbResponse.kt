package ru.xmn.filmfilmfilm.services.tmdb


class TmdbMovieInfo(
        val adult: Boolean,
        val backdrop_path: String?,
        val belongs_to_collection: TmdbCollection?,
        val budget: Int,
        val genres: List<Genre>,
        val homepage: String,
        val id: Int,
        val imdb_id: String,
        val originalLanguage: String,
        val originalTitle: String,
        val overview: String,
        val popularity: Double,
        val poster_path: String?,
        val productionCompanies: List<ProductionCompany>,
        val productionCountries: List<TmdbLanguage>,
        val releaseDate: String,
        val revenue: Int,
        val runtime: Int?,
        val spoken_languages: List<TmdbLanguage>,
        val status: String,
        val tagline: String,
        val title: String,
        val video: Boolean,
        val vote_average: Double,
        val vote_count: Int
)

class Genre(
        val id: Int,
        val name: String
)

class ProductionCompany(
        val name: String,
        val id: Int
)

class TmdbLanguage(
        val iso6391: String,
        val name: String
)

class TmdbCollection(
        val id: Int?,
        val name: String?,
        val poster_path: String?,
        val backdrop_path: String?
)

class Cast(
        val castId: Int?,
        val character: String?,
        val creditId: String?,
        val id: Int?,
        val name: String?,
        val order: Int?,
        val profilePath: Any?
)

class Crew(
        val creditId: String?,
        val department: String?,
        val id: Int?,
        val job: String?,
        val name: String?,
        val profilePath: Any?
)

class TmdbCredits(
        val id: Int?,
        val cast: List<Cast>?,
        val crew: List<Crew>?
)
package ru.xmn.filmfilmfilm.kudago.data

class KudaGoShowingsResponse(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: List<KudaGoShowingResponse>
)

class KudaGoMoviesResponse(
        val count: Int,
        val next: String?,
        val previous: String?,
        val results: List<Movie>) {
    operator fun get(i: Int) = results[i]
}

class KudaGoShowingResponse(
        val id: Int,
        val movie: Movie,
        val place: Place,
        val datetime: Int,
        val threeD: Boolean,
        val imax: Boolean,
        val fourDx: Boolean,
        val originalLanguage: Boolean,
        val price: String?
)

class Place(
        val id: Int,
        val title: String,
        val slug: String,
        val address: String,
        val images: List<Image>,
        val siteUrl: String,
        val isClosed: Boolean
)

class Movie(
        val id: Int,
        val title: String,
        val poster: Poster,
        val description: String,
        val body_text: String,
        val genres: String,
        val original_title: String,
        val locale: String,
        val country: String,
        val year: String,
        val language: String,
        val mpaa_rating: String,
        val age_restriction: String,
        val stars: String,
        val director: String,
        val writer: String,
        val awards: String,
        val trailer: String,
        val images: String,
        val imdb_url: String,
        val imdb_rating: String
)

class Poster(val image: String?)


class Image(val image: String?)

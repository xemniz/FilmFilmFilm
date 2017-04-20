package api.kudago.data

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
        val id: Integer,
        val title: String,
        val slug: String,
        val address: String,
        val images: List<Image>,
        val siteUrl: String,
        val isClosed: Boolean
)

class Movie(
        val id: Integer,
        val title: String,
        val poster: Poster
)

class Poster(val image: String?)


class Image(val image: String?)

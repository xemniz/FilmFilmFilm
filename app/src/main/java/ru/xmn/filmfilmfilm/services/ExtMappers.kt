package ru.xmn.filmfilmfilm.services

import ru.xmn.filmfilmfilm.services.kudago.Movie

val Movie.imdbId: String?
    get() = """tt\d+""".toRegex().find(this.imdb_url)?.value

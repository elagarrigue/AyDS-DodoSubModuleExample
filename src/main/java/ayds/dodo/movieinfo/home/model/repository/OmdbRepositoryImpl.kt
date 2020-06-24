package ayds.dodo.movieinfo.home.model.repository

import ayds.dodo.movieinfo.home.model.entities.OmdbMovie
import ayds.dodo.movieinfo.home.model.entities.Rating
import ayds.dodo.movieinfo.omdb.external.ExternalService
import ayds.dodo.movieinfo.home.model.repository.local.LocalStorage
import ayds.dodo.movieinfo.omdb.external.omdb.Movie

internal class OmdbRepositoryImpl(
    private val localStorage: LocalStorage,
    private val externalService: ExternalService
) : OmdbRepository {

    override fun getMovie(title: String): OmdbMovie? {
        var movie = localStorage.getMovie(title)

        when {
            movie != null -> markMovieAsLocal(movie)
            else -> {
                try {
                    val dataMovie = externalService.getMovie(title)

                    movie = dataMovie.toOmdbMovie()

                    localStorage.saveMovie(title, movie)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return movie
    }

    private fun markMovieAsLocal(movie: OmdbMovie) {
        movie.isLocallyStoraged = true
    }

    private fun Movie.toOmdbMovie() : OmdbMovie  {
        val omdbMovie =  OmdbMovie()
        omdbMovie.title = title
        omdbMovie.year = year
        omdbMovie.plot = plot
        omdbMovie.director = director
        omdbMovie.actors = actors
        omdbMovie.posterUrl = posterUrl
        omdbMovie.isLocallyStoraged = false
        omdbMovie.ratings = ratings.map {
            Rating().apply {
                source = it.source
                value = it.value
            }
        }
        return omdbMovie
    }
}
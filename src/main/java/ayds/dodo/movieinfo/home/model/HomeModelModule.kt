package ayds.dodo.movieinfo.home.model

import ayds.dodo.movieinfo.home.model.repository.OmdbRepositoryImpl
import ayds.dodo.movieinfo.home.model.repository.local.sqldb.SqlDBImpl
import ayds.dodo.movieinfo.home.model.repository.local.sqldb.SqlQueriesImpl
import ayds.dodo.movieinfo.omdb.external.omdb.OmdbModule

object HomeModelModule {
    private val repository = OmdbRepositoryImpl(
        SqlDBImpl(SqlQueriesImpl()), OmdbModule.omdbService
    )

    val homeModel: HomeModel = HomeModelImpl(repository)
}
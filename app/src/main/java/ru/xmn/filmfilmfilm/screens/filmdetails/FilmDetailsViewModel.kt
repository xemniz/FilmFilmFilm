package ru.xmn.filmfilmfilm.screens.filmdetails

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class FilmDetailsViewModel: ViewModel(){
    @Inject
    lateinit var provider: FilmDetailsProvider
}


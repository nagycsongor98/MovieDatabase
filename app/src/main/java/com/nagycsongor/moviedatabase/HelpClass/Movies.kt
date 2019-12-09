package com.nagycsongor.moviedatabase.HelpClass

class Movies(_movieName: String,_movieOriginalName:String,_movieDetail:String,_poster: String?) {

    var movieName: String = ""
    var movieDetail: String = ""
    var moviePoster: String? = ""
    var movieTitleOriginal: String = ""

    init{
        this.movieName=_movieName
        this.movieTitleOriginal=_movieOriginalName
        this.movieDetail=_movieDetail
        this.moviePoster=_poster

    }
}
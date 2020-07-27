package com.dew.newsapplication.utility

import com.dew.newsapplication.model.NewsSource


/*
* this is the file to keep mock data
**/
fun getNewSource(): ArrayList<NewsSource> {
    val newsSource = arrayListOf<NewsSource>()
    newsSource.add(
        NewsSource(
            "google-news-in",
            "Google News (India)"
        )
    )
    newsSource.add(NewsSource("espn", "ESPN"))
    newsSource.add(NewsSource("abc-news", "ABC News"))
    newsSource.add(NewsSource("fox-news", "Fox News"))
    newsSource.add(NewsSource("axios", "Axios"))
    newsSource.add(NewsSource("bbc-news", "BBC News"))
    newsSource.add(NewsSource("bbc-sport", "BBC Sport"))
    newsSource.add(NewsSource("bloomberg", "bloomberg"))
    newsSource.add(NewsSource("cbc-news", "CBC News"))
    return newsSource
}

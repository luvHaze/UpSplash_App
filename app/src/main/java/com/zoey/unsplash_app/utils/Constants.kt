package com.zoey.unsplash_app.utils

object Constants {

    const val TAG: String = "로그"
}

enum class SEARCH_TYPE {
    PHOTO,
    USER
}

enum class RESPONSE_STATUS {
    OKAY,
    FAIL,
    NO_CONTENT
}

object API {
    const val BASE_URL = "https://api.unsplash.com/"
    const val CLIENT_ID = "5QVZ2fqLk6FFELQec_JUPFhz7IfcixZ5dzyMtj4IS1w"

    const val SEARCH_PHOTO = "search/photos"
    const val SEARCH_USER = "search/users"
}
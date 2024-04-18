package ru.vladsaybulin.model.search

enum class FilterType(val queryMapKey: QueryMapKey) {
    Kind(QueryMapKey.Kind),
    Status(QueryMapKey.Status),
    MyListStatus(QueryMapKey.MyList),
    Duration(QueryMapKey.Duration),
    Season(QueryMapKey.Season),
    Rating(QueryMapKey.Rating),
    Score(QueryMapKey.Score),
    Genre(QueryMapKey.Genre),
    Studio(QueryMapKey.Studio),
    Publisher(QueryMapKey.Publisher)
}
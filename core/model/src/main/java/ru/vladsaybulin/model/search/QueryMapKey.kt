package ru.vladsaybulin.model.search

enum class QueryMapKey(val serializedValue: String) {
    Order("order"),
    Kind("kind"),
    Status("status"),
    Season("season"),
    Score("score"),
    Duration("duration"),
    Rating("rating"),
    Genre("genre"),
    Studio("studio"),
    Publisher("publisher"),
    Franchise("franchise"),
    MyList("mylist"),
    Search("search"),
    Censored("Censored"),
    Ids("ids"),
    ExcludedIds("excluded_ids")
}
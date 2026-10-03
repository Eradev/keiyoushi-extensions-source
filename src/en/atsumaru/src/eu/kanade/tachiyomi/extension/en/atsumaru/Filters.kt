package eu.kanade.tachiyomi.extension.en.atsumaru

import eu.kanade.tachiyomi.source.model.Filter

internal class GenreFilter(genres: List<Genre>) :
    Filter.Group<Filter.TriState>(
        "Genres",
        genres.map { genre ->
            object : Filter.TriState(genre.name) {}
        },
    ) {
    val genreIds = genres.map { it.id }
}

// Split large list separate Groups.
internal class TagFilters(tags: List<Tag>) :
    Filter.Group<TagFilter>(
        "Tags",
        tags.sortedBy { it.name }.groupBy {
            val c = it.name.firstOrNull()?.uppercaseChar()

            when {
                c == null || c !in 'A'..'Z' -> "0-9"
                else -> c.toString()
            }
        }.map { (letters, tagsChunk) ->
            TagFilter(letters, tagsChunk)
        },
    )

internal class TagFilter(letters: String, tags: List<Tag>) :
    Filter.Group<TagTriState>(
        letters,
        tags.map { tag ->
            TagTriState(tag.name, tag.id, tag.requiresAdult)
        },
    )

internal class TagTriState(
    name: String,
    val id: String,
    val requiresAdult: Boolean,
) : Filter.TriState(name)

internal class TypeFilter(types: List<Type>) :
    Filter.Group<Filter.CheckBox>(
        "Manga Type",
        types.map { type ->
            object : Filter.CheckBox(type.name, false) {}
        },
    ) {
    val ids = types.map { it.id }
}

internal class StatusFilter(statuses: List<Status>) :
    Filter.Group<Filter.CheckBox>(
        "Publishing Status",
        statuses.map { status ->
            object : Filter.CheckBox(status.name, false) {}
        },
    ) {
    val ids = statuses.map { it.id }
}

internal class YearFilter : Filter.Text("Year (e.g., 2024)")

internal class MinChaptersFilter : Filter.Text("Minimum Chapters")

internal class SortFilter :
    Filter.Sort(
        "Sort By",
        arrayOf("Popularity", "Trending", "Date Added", "Release Date", "Top Rated", "Title"),
        Selection(0, false),
    ) {
    companion object {
        val VALUES = arrayOf("views", "trending", "dateAdded", "released", "mbRating", "title")
    }
}

internal class AdultFilter(state: Boolean) : Filter.CheckBox("Show Adult Content", state)

internal class OfficialFilter : Filter.CheckBox("Only Official Translations", false)

internal class Genre(val name: String, val id: String)

internal class Tag(
    val name: String,
    val id: String,
    val requiresAdult: Boolean = false,
)

internal class Type(val name: String, val id: String)

internal class Status(val name: String, val id: String)

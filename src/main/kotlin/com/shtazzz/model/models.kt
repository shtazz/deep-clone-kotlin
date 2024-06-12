package com.shtazzz.com.shtazzz.model

import java.util.Objects

class SimpleMan(
    var name: String,
    var age: Int,
    var favoriteBooks: List<String>,
) {
    override fun toString(): String {
        return "Man(name='$name', age=$age, favoriteBooks=$favoriteBooks)"
    }
}

class Man(
    var name: String,
    var age: Int,
    var favoriteBooks: MutableList<Book>,
) {

    override fun hashCode() = Objects.hash(name, age, favoriteBooks)

    override fun equals(other: Any?) =
        other is Man
                && name == other.name
                && age == other.age
                && favoriteBooks == other.favoriteBooks

    override fun toString(): String {
        return "Man(name='$name', " +
                "age=$age, " +
                "favoriteBooks=${favoriteBooks.map { it.name }}" +
                ")"

    }
}

class Book(
    var name: String,
    var author: String,
    var owner: Man?,
) {
    override fun hashCode() = Objects.hash(name, author)

    override fun equals(other: Any?) =
        other is Book
                && name == other.name
                && author == other.author

    override fun toString(): String {
        return "Book(name='$name', " +
                "author=$author, " +
                "owner=${owner?.name}" +
                ")"
    }
}

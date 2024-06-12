package com.shtazzz

import com.shtazzz.com.shtazzz.copier.deepCopy
import com.shtazzz.com.shtazzz.model.SimpleMan

fun main() {
    val john = SimpleMan(
        name = "John",
        age = 69,
        favoriteBooks = listOf("Book1", "Book2")
    )

    val johnCopy = deepCopy(john)

    println(john)
    println(johnCopy)
    println("johnCopy has the same address as john: ${johnCopy === john}")
}

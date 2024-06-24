package com.shtazzz.utils

import com.shtazzz.com.shtazzz.copier.deepCopy
import com.shtazzz.com.shtazzz.model.Book
import com.shtazzz.com.shtazzz.model.Man
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class DeepCopyTest {

    @Test
    fun `when deepCopy called for string then return string`() {
        //given
        val original = "test"

        //when
        val actual = deepCopy(original)

        //then
        assertThat(actual).isEqualTo(original)
    }

    @Test
    fun `when deepCopy called for collection then return collection`() {
        //given
        val original = listOf("test1", "foo2", "bar3")

        //when
        val actual = deepCopy(original)

        //then
        assertThat(actual).isEqualTo(original)
        assertThat(actual !== original).isTrue()
    }

    @Test
    fun `when deepCopy called for set then return set`() {
        //given
        val original = setOf("test1", "foo2", "bar3")

        //when
        val actual = deepCopy(original)

        //then
        assertThat(actual).isEqualTo(original)
        assertThat(actual !== original).isTrue()
    }

    @Test
    fun `when deepCopy called for map then return map`() {
        //given
        val original = mapOf("foo1" to "bar1", "foo2" to "bar2", "foo3" to "bar3")

        //when
        val actual = deepCopy(original)

        //then
        assertThat(actual).isEqualTo(original)
        assertThat(actual !== original).isTrue()
    }

    @ParameterizedTest
    @MethodSource("primitives")
    fun `when deepCopy called with primitives then return primitives`(input: Any) {
        //given

        //when
        val actual = deepCopy(input)

        //then
        assertThat(actual).isEqualTo(input)
    }

    @Test
    fun `when deepCopy called with object that contains primitives, collections, maps and recursive reference then return deep copy`() {
        //given
        val bob = Man(
            name = "Bob",
            age = 77,
            favoriteBooks = mutableListOf()
        )
        val john = Man(
            name = "John",
            age = 69,
            favoriteBooks = mutableListOf()
        )

        val book1 = Book(
            name = "Book1",
            author = "author1",
            owner = bob,
        )
        val book2 = Book(
            name = "Book2",
            author = "author2",
            owner = john,
        )
        val book3 = Book(
            name = "Book3",
            author = "author3",
            owner = null,
        )

        bob.favoriteBooks.addAll(listOf(book1, book3))
        john.favoriteBooks.addAll(listOf(book1, book2))

        //when
        val actual = deepCopy(john)

        //then
        assertThat(actual).isEqualTo(john)
        assertThat(actual !== john).isTrue()

        assertThat(actual!!.favoriteBooks).isEqualTo(john.favoriteBooks)
        assertThat(actual.favoriteBooks[0] !== john.favoriteBooks[0]).isTrue()
        assertThat(actual.favoriteBooks[1] !== john.favoriteBooks[1]).isTrue()
    }

    private companion object {
        @JvmStatic
        fun primitives() = listOf(
            Arguments.of(true),
            Arguments.of(1.toByte()),
            Arguments.of('a'),
            Arguments.of(11.toShort()),
            Arguments.of(111),
            Arguments.of(111.111f),
            Arguments.of(111111L),
            Arguments.of(111111.111111),
        )
    }
}

package com.shtazzz.com.shtazzz.copier

import sun.reflect.ReflectionFactory
import java.lang.reflect.Constructor
import java.util.IdentityHashMap
import java.util.LinkedList
import java.util.Queue
import java.util.TreeSet
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberProperties

fun <T : Any> deepCopy(obj: T?): T? {
    return deepCopyRecursive(obj, IdentityHashMap<Any, Any>())
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any> deepCopyRecursive(obj: T?, references: IdentityHashMap<Any, Any>): T? {
    if (obj == null) return null

    if (references.contains(obj)) return references[obj] as T

    val klass = obj::class
    return when {
        klass.isPrimitive() || klass.isWrapper() || klass.isString() -> obj

        klass.isSubclassOf(Collection::class) -> cloneCollectionWithValues(obj as Collection<*>, references) as T

        klass.isSubclassOf(Map::class) -> cloneMapWithValues(obj as Map<*, *>, references) as T

        else -> deepCopyObject(obj, references)
    }
}

private fun cloneCollectionWithValues(
    collection: Collection<*>,
    references: IdentityHashMap<Any, Any>,
): Collection<*> {
    val newCollection = when (collection) {
        is List -> ArrayList<Any>()
        is Queue -> LinkedList()
        is TreeSet -> TreeSet()
        is Set -> HashSet<Any>()
        else -> throw IllegalArgumentException("Unsupported collection type: ${collection::class.java}")
    }
    collection.forEach { element -> deepCopyRecursive(element, references)?.let { newCollection.add(it) } }
    return newCollection
}

private fun cloneMapWithValues(map: Map<*, *>, references: IdentityHashMap<Any, Any>) = map
    .mapKeys { deepCopyRecursive(it.key, references) }
    .mapValues { deepCopyRecursive(it.value, references) }

@Suppress("UNCHECKED_CAST")
private fun <T : Any> deepCopyObject(obj: T, references: IdentityHashMap<Any, Any>): T {
    val klass = obj::class
    val newInstance = createNewInstance(klass).also { references[obj] = it }

    klass.memberProperties.forEach { property ->
        val value = (property as KProperty<*>).getter.call(obj)
        if (property is KMutableProperty<*>) {
            val copiedValue = deepCopyRecursive(value as T?, references)
            property.setter.call(newInstance, copiedValue)
        }
    }
    return newInstance
}

@Suppress("UNCHECKED_CAST")
private fun <T : Any> createNewInstance(klass: KClass<T>): T {
    val reflectionFactory = ReflectionFactory.getReflectionFactory()

    val constructor: Constructor<*> =
        reflectionFactory.newConstructorForSerialization(klass.java, Any::class.java.getDeclaredConstructor())
    constructor.isAccessible = true

    return constructor.newInstance() as T
}

private inline fun <reified T : Any> KClass<*>.isSubclassOf(base: KClass<T>): Boolean {
    return base.java.isAssignableFrom(this.java)
}

private fun KClass<*>.isString(): Boolean {
    return this.java == String::class.java
}

private fun KClass<*>.isPrimitive(): Boolean {
    return this.java.isPrimitive
}

private fun KClass<*>.isWrapper(): Boolean {
    return this in setOf(
        Boolean::class,
        Byte::class,
        Character::class,
        Short::class,
        Integer::class,
        Float::class,
        Long::class,
        Double::class
    )
}

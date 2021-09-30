package io.madrona.njord.geo.symbols

import mil.nga.sf.geojson.Feature

typealias S57Prop = MutableMap<String, Any?>

inline fun <R : Any> S57Prop?.listFrom(key: String, transform: (Int) -> R?) : List<R> {
    return (this?.get(key) as? Array<*>)?.mapNotNull { it?.toString()?.toIntOrNull() }?.mapNotNull {
        transform(it)
    }?.toList() ?: emptyList()
}

fun Feature.s57Props(): S57Prop {
    return properties
}

fun S57Prop.stringValue(key: String) = get(key)?.toString()
fun S57Prop.intValue(key: String) = get(key)?.toString()?.toIntOrNull()
fun S57Prop.floatValue(key: String) = get(key)?.toString()?.toFloatOrNull()

package sidev.lib.universal.structure.data

import sidev.lib.universal.structure.collection.sequence.NestedSequence

/** Struktur data yg disertai dg level scr hirarki pada [NestedSequence]. */
data class LeveledValue<T>(val level: Int, val value: T)
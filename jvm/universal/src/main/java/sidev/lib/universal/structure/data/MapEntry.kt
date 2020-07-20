package sidev.lib.universal.structure.data

data class MapEntry<K, V>(override val key: K, override val value: V): Map.Entry<K, V>
data class MutableMapEntry<K, V>(override val key: K, override var value: V): MutableMap.MutableEntry<K, V>{
    override fun setValue(newValue: V): V {
        val oldVal= value
        value= newValue
        return oldVal
    }
}
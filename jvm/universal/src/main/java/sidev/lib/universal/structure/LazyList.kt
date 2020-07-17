package sidev.lib.universal.structure

/**
 * List yg bersifat lazy, yaitu berukuran kecil di awal dan akan berkembang
 * seiring penggunaannya yg melibatkan pemanggilan [getBuilderIterator].
 */
interface LazyList<T> : Collection<T>{
    /**
     * Digunakan untuk mengambil iterator yg berfungsi sbg pengisi [LazyList] ini.
     */
    val builderIterator: Iterator<T>
}
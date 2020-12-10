package sidev.lib.android.siframe.intfc

/*
/**
 * Interface yg menandakan bahwa turunan ini dapat diurutkan maupun difilter.
 *
 * @param T merupakan tipe data dari isi interface ini.
 */
interface ReArrangeable<T> {
    /**
     * @param pos1 harus lebih kecil dari [pos2]
     */
    fun sort(func: (pos1: Int, data1: T, pos2: Int, data2: T) -> Boolean)
    fun filter(func: (pos: Int, data: T) -> Boolean)
}
 */
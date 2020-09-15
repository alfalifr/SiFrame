package sidev.lib.android.siframe.intfc.listener

import sidev.lib.structure.prop.TagProp

/**
 * Interface dasar dari semua listener yg ada pada framework ini.
 * Interface ini meng-extend [TagProp] yg berguna saat operasi penambahan (addListener)
 * di mana parameternya berupa lambda, bkn object listener itu sendiri,
 * sehingga lambda tersebut harus dibungkus oleh object baru di mana pada kebanyakan kasus
 * programmer tidak akan mendapat object listener tersebut. Oleh karena itu, listener
 * pada framework ini memiliki [TagProp.tag] agar dapat menghapus (removeListener)
 * lambda menggunakan tag tersebut.
 */
interface Listener: TagProp<Any>
package sidev.lib.android.siframe.model.intfc

/**
 * Sebagai penanda suatu variabel jika merupakan AUTO_INCREMENT.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
//@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class ModelUnique(val kind: StorageKind= StorageKind.SQLITE)
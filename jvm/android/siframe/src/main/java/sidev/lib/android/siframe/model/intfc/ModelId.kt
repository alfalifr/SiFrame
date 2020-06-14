package sidev.lib.android.siframe.model.intfc

/**
 * Sebagai penanda suatu variabel jika merupakan primaryKey.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
annotation class ModelId(val kind: StorageKind= StorageKind.SQLITE)
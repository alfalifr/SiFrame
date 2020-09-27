package sidev.lib.android.siframe.model.intfc


/**
 * Sebagai penanda suatu attribut tidak disimpan dalam db.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class Exclude(val kind: StorageKind= StorageKind.SQLITE)
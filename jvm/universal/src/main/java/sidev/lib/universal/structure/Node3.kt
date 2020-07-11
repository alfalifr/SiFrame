package sidev.lib.universal.structure

/**
 * @param child hanya berjumlah 1 bkn brarti data ini hanya bisa memiliki 1 child.
 *   Data ini dapat memiliki banyak child, yaitu dg menggunakan child.next.
 *   Dalam konteks ini, param [child] merupakan child indeks ke-0.
 */
class Node3<T>(var value: T?, next: Node3<T>?, child: Node3<T>?) : DataStructure{
    var next: Node3<T>?= next
        set(v){
            if(v != null){
                v.prev= this
                v.parent= parent
            } else{
                field?.prev= null
                field?.parent= null
            }
            field= v
        }
    var child: Node3<T>?= child
        set(v){
            if(v != null)
                v.parent= this
            else
                field?.parent= null
            field= v
        }

    var prev: Node3<T>?= null
        private set
    var parent: Node3<T>?= null
        private set

    var hasBeenIterated= false
}
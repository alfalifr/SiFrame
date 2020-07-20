package sidev.lib.universal.structure.collection.iterator

import sidev.lib.universal.annotation.Interface


@Interface
interface MutableNestedIteratorSimple<I>: MutableNestedIterator<I, I>, NestedIteratorSimple<I>
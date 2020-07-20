package sidev.lib.universal.structure.collection.iterator

import sidev.lib.universal.annotation.Interface


@Interface
interface MutableNestedIterator<I, O>: NestedIterator<I, O>, MutableIterator<O>
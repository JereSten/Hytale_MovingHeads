package de.jerst.plugin.movingheads.utils

import java.util.Collections

object CollectionHelpers {
    fun <T> MutableList<T>.moveUpCircular(index: Int) {
        if (isEmpty()) return

        if (index > 0) {
            Collections.swap(this, index, index - 1)
        } else {
            val element = removeAt(0)
            add(element)
        }
    }

    fun <T> MutableList<T>.moveDownCircular(index: Int) {
        if (isEmpty()) return

        if (index < size - 1) {
            Collections.swap(this, index, index + 1)
        } else {
            val element = removeAt(size - 1)
            add(0, element)
        }
    }
}
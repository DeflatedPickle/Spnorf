package com.deflatedpickle.spnorf

import java.awt.Dimension
import java.awt.Point
import javax.swing.JButton

class DockButton(text: String) : JButton(text) {
    var row = 0
    var column = 0

    val maxSize = Dimension(120, 40)
    val minSize = Dimension(60, 20)

    var weight = 0

    init {
        minimumSize = minSize
        maximumSize = maxSize

        this.preferredSize = maxSize
    }

    fun update(mouseDistance: Point) {
        // println(mouseDistance)

        // Calculate the weight based on mouse distance
        weight = ((mouseDistance.x * mouseDistance.x) + (mouseDistance.y * mouseDistance.y)) / 100
        // println("$text, $weight")
    }
}
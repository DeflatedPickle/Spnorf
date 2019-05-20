package com.deflatedpickle.spnorf

import java.awt.Component
import java.awt.Rectangle
import java.awt.Toolkit
import javax.swing.JFrame

object GlobalValues {
    var frame: JFrame? = null

    var orientation: Orientation = Orientation.HORIZONTAL
    var side: Side = Side.BOTTOM

    // https://stackoverflow.com/a/29177069
    fun getEffectiveScreenSize(component: Component): Rectangle {
        val rectangle = Rectangle()

        val screenSize = Toolkit.getDefaultToolkit().screenSize
        val bounds = Toolkit.getDefaultToolkit().getScreenInsets(component.graphicsConfiguration)

        rectangle.width =
            (screenSize.getWidth() - bounds.left.toDouble() - bounds.right.toDouble()).toInt() - frame!!.width
        rectangle.height =
            (screenSize.getHeight() - bounds.top.toDouble() - bounds.bottom.toDouble()).toInt() - frame!!.height

        rectangle.x = ((screenSize.getHeight() - component.height) / 2.0).toInt()
        rectangle.y = ((screenSize.getWidth() - component.width) / 2.0).toInt()

        return rectangle
    }
}
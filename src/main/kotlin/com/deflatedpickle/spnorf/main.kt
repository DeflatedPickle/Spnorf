package com.deflatedpickle.spnorf

import java.awt.Color
import java.awt.Insets
import java.awt.Window
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager


@Suppress("KDocMissingDocumentation")
fun main() {
    val frame = JFrame("Spnorf")
    GlobalValues.frame = frame
    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
    SwingUtilities.updateComponentTreeUI(frame)

    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.isAlwaysOnTop = true
    frame.isUndecorated = true
    frame.background = Color(0, 0, 0, 0)
    frame.type = Window.Type.UTILITY

    val paddingInsets = Insets(4, 4, 4, 4)
    val orientation = Orientation.HORIZONTAL
    val panel = DockPanel(orientation, paddingInsets)
    frame.add(panel)

    frame.pack()
    frame.isVisible = true

    when (orientation) {
        Orientation.HORIZONTAL -> frame.setSize(600, 100)
        Orientation.VERTICAL -> frame.setSize(100, 600)
    }

    frame.setLocationRelativeTo(null)
}
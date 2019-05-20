package com.deflatedpickle.spnorf

import java.awt.*
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.UIManager
import java.awt.Toolkit.getDefaultToolkit


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
    val panel = DockPanel(paddingInsets)
    frame.add(panel)

    frame.pack()
    frame.isVisible = true

    when (GlobalValues.orientation) {
        Orientation.HORIZONTAL -> frame.setSize(600, 100)
        Orientation.VERTICAL -> frame.setSize(100, 600)
    }

    val edgePadding = 8

    val screenSize = GlobalValues.getEffectiveScreenSize(panel)
    when (GlobalValues.side) {
        Side.LEFT -> frame.setLocation(edgePadding, (screenSize.height / 2))
        Side.RIGHT -> frame.setLocation(screenSize.width - edgePadding, (screenSize.height / 2))
        Side.TOP -> frame.setLocation((screenSize.width / 2), edgePadding)
        Side.BOTTOM -> frame.setLocation((screenSize.width / 2), screenSize.height - edgePadding)
    }

    // frame.setLocationRelativeTo(null)
}
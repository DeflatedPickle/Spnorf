package com.deflatedpickle.spnorf

import java.awt.*
import java.awt.event.ActionListener
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.Timer
import javax.swing.UIManager


@Suppress("KDocMissingDocumentation")
fun main() {
    val frame = JFrame("Spnorf")
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

    val highlightType = HighlightType.POWER

    val weightMax = 1400
    val weightMultiplier = when (highlightType) {
        HighlightType.MULTIPLIER -> 7.0 // 3.0
        HighlightType.POWER -> 1.3 // 1.006
    }

    val timer = Timer(120, ActionListener {
        var totalWeights = 0
        for (i in panel.barButtons) {
            // TODO: Get the mouse pointer position
            val mouseLocation = MouseInfo.getPointerInfo().location
            SwingUtilities.convertPointFromScreen(mouseLocation, panel)
            val buttonLocation = i.location
            val buttonSize = i.size
            // TODO: Get the distance from the pointer to this button
            val distance = Point(mouseLocation.x - buttonLocation.x - (buttonSize.width / 2), mouseLocation.y - buttonLocation.y - (buttonSize.height / 2))
            i.update(distance)

            totalWeights += i.weight
        }

        for (i in panel.barButtons) {
            // TODO: Resize and move based on weight, using the width of the bar to get percentages
            val percentage = weightMax - when (highlightType) {
                HighlightType.MULTIPLIER -> (i.weight * weightMultiplier)
                HighlightType.POWER -> (Math.pow(i.weight.toDouble(), weightMultiplier))
            }
            // println("${i.text}, $percentage")

            panel.gridBagLayout.setConstraints(i, GridBagConstraints(i.column, i.row, 1, 1, percentage, percentage, GridBagConstraints.WEST, GridBagConstraints.BOTH, paddingInsets, 1, 1))
            panel.revalidate()

            // i.setLocation(i.location.x * percentage, i.location.y * percentage)
            // i.setSize(percentage, percentage)
        }
    })
    timer.start()

    frame.pack()
    frame.isVisible = true

    when (orientation) {
        Orientation.HORIZONTAL -> frame.setSize(600, 100)
        Orientation.VERTICAL -> frame.setSize(100, 600)
    }

    frame.setLocationRelativeTo(null)
}
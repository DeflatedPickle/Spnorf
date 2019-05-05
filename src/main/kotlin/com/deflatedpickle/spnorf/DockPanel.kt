package com.deflatedpickle.spnorf

import java.awt.*
import javax.swing.JPanel

class DockPanel(val orientation: Orientation, val paddingInsets: Insets) : JPanel() {
    val gridBagLayout = GridBagLayout()

    val barButtons = mutableListOf<DockButton>()

    // Config Options
    val barIcons = mutableListOf<String>(
    )
    val barWidth = 20
    val barHeight = 20

    init {
        isOpaque = false

        var row = 0
        var column = 0
        for (i in barIcons) {
            // val shellFolder = ShellFolder.getShellFolder(File(i))
            // val icon = ImageIcon(shellFolder.getIcon(true))
            barButtons.add(
                DockButton(i.substringAfterLast("\\").substringBefore(".").capitalize()).also {
                    it.row = row
                    it.column = column
                    this.gridBagLayout.setConstraints(it, GridBagConstraints(column, row, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, paddingInsets, 1, 1))
                    this.add(it)
                }
            )
            when (this.orientation) {
                Orientation.HORIZONTAL -> column++
                Orientation.VERTICAL -> row++
            }
        }

        this.layout = gridBagLayout
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        g.color = Color.WHITE

        when (this.orientation) {
            Orientation.HORIZONTAL -> g.fillRect(0, this.height - barHeight, this.width, this.height)
            Orientation.VERTICAL -> g.fillRect(0, 0, barWidth, this.height)
        }
    }
}
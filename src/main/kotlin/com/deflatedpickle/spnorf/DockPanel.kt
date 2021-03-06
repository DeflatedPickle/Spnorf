package com.deflatedpickle.spnorf

import java.awt.*
import java.awt.datatransfer.DataFlavor
import java.awt.dnd.DnDConstants
import java.awt.dnd.DropTarget
import java.awt.dnd.DropTargetDragEvent
import java.awt.dnd.DropTargetDropEvent
import java.awt.event.ActionListener
import java.io.File
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.Timer

class DockPanel(val paddingInsets: Insets) : JPanel() {
    val gridBagLayout = GridBagLayout()

    // Config Options
    val barIconFiles = mutableListOf<File>()
    val barIconButtons = mutableListOf<DockButton>()
    val barWidth = 20
    val barHeight = 20

    val distanceLimit = Rectangle(60, 10, 60, 10)

    var row = 0
    var column = 0

    init {
        isOpaque = false

        this.dropTarget = object : DropTarget() {
            override fun dragOver(dtde: DropTargetDragEvent) {
                super.dragOver(dtde)

                // TODO: Add a temporary button, in the column closest to the mouse
            }

            override fun drop(dtde: DropTargetDropEvent) {
                // super.drop(dtde)
                // TODO: Add DnD for dock buttons, so they can be re-organised

                // TODO: Reject (or resolve) if it's a shortcut or not an executable
                dtde.acceptDrop(DnDConstants.ACTION_COPY)
                val droppedFiles = dtde.transferable.getTransferData(DataFlavor.javaFileListFlavor) as List<File>

                for (file in droppedFiles) {
                    // TODO: Check if the file is a shortcut then reject it or resolve the linked file
                    barIconFiles.add(file)
                    addButton(file)
                }
            }
        }

        val highlightType = HighlightType.POWER

        val weightMax = 1400
        val weightMultiplier = when (highlightType) {
            HighlightType.MULTIPLIER -> 7.0 // 3.0
            HighlightType.POWER -> 1.3 // 1.006
        }

        val timer = Timer(1000 / 60, ActionListener {
            var totalWeights = 0
            for (i in this.barIconButtons) {
                val mouseLocation = MouseInfo.getPointerInfo().location

                val frameLocation = GlobalValues.frame!!.location
                val frameSize = GlobalValues.frame!!.size

                // If the mouse is outside the range border
                val rectangle = Rectangle(
                    frameLocation.x - distanceLimit.x,
                    frameLocation.y - distanceLimit.y,
                    frameSize.width + (distanceLimit.width * 2),
                    frameSize.height + (distanceLimit.height * 2)
                )
                if (!rectangle.contains(mouseLocation)) {
                    i.weight = 100 / this.barIconButtons.size
                    continue
                }

                // Get the mouse pointer position
                val relativeMouseLocation = MouseInfo.getPointerInfo().location
                SwingUtilities.convertPointFromScreen(relativeMouseLocation, this)
                val buttonLocation = i.location
                val buttonSize = i.size
                // Get the distance from the pointer to this button
                val distance = Point(
                    relativeMouseLocation.x - buttonLocation.x - (buttonSize.width / 2),
                    relativeMouseLocation.y - buttonLocation.y - (buttonSize.height / 2)
                )
                i.update(distance)

                totalWeights += i.weight
            }

            for (i in this.barIconButtons) {
                val percentage = weightMax - when (highlightType) {
                    HighlightType.MULTIPLIER -> (i.weight * weightMultiplier)
                    HighlightType.POWER -> (Math.pow(i.weight.toDouble(), weightMultiplier))
                }
                // println("${i.text}, $percentage")

                // Update the constraints based on weight
                this.gridBagLayout.setConstraints(
                    i,
                    GridBagConstraints(
                        i.column,
                        i.row,
                        1,
                        1,
                        percentage,
                        percentage,
                        GridBagConstraints.WEST,
                        GridBagConstraints.BOTH,
                        paddingInsets,
                        1,
                        1
                    )
                )
                this.revalidate()
            }
        })
        timer.start()

        this.layout = gridBagLayout
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)

        // TODO: Add an embedded scripting language, so this can be completely customizable
        g.color = Color.WHITE

        when (GlobalValues.orientation) {
            Orientation.HORIZONTAL -> {
                when(GlobalValues.side) {
                    Side.TOP -> g.fillRect(0, 0, this.width, barHeight)
                    Side.BOTTOM -> g.fillRect(0, this.height - barHeight, this.width, this.height)
                    else -> {}
                }
            }
            Orientation.VERTICAL -> {
                when(GlobalValues.side) {
                    Side.LEFT -> g.fillRect(0, 0, barWidth, this.height)
                    Side.RIGHT -> g.fillRect(this.width - barWidth, 0, barWidth, this.height)
                    else -> {}
                }
            }
        }
    }

    fun addButton(file: File) {
        // val shellFolder = ShellFolder.getShellFolder(File(i))
        // val icon = ImageIcon(shellFolder.getIcon(true))
        barIconButtons.add(
            DockButton(file.name.substringAfterLast("\\").substringBefore(".").capitalize()).also {
                it.row = row
                it.column = column
                this.gridBagLayout.setConstraints(
                    it,
                    GridBagConstraints(
                        column,
                        row,
                        1,
                        1,
                        1.0,
                        1.0,
                        GridBagConstraints.WEST,
                        GridBagConstraints.BOTH,
                        paddingInsets,
                        1,
                        1
                    )
                )
                this.add(it)
            }.apply {
                addActionListener {
                    Runtime.getRuntime().exec(file.absolutePath)
                }
            }
        )
        when (GlobalValues.orientation) {
            Orientation.HORIZONTAL -> column++
            Orientation.VERTICAL -> row++
        }
    }
}
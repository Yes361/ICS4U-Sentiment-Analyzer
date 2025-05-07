/*
* FileCellRenderer.java defines a custom ListCellRenderer for JList to Render Files
*
* Followed this tutorial:
* https://www.codejava.net/java-se/swing/jlist-custom-renderer-example
*  */

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FileCellRenderer extends JLabel implements ListCellRenderer<File> {
    // Boilerplate Constructor
    public FileCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends File> list, File file, int index, boolean isSelected, boolean cellHasFocus) {
        // Set the contents of the Cell in the JList to the file name
        setText(file.getName());

        // Handle Foreground and Background color for the cell based on whether or not its selected
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }

        return this;
    }
}

package model.theme;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class UITheme {

    // Color Palette
    public static final Color PRIMARY_COLOR = new Color(41, 128, 185); // Belize Hole
    public static final Color SECONDARY_COLOR = new Color(52, 152, 219); // Peter River
    public static final Color BACKGROUND_COLOR = new Color(236, 240, 241); // Clouds
    public static final Color FONT_COLOR = new Color(44, 62, 80); // Wet Asphalt
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color TABLE_HEADER_COLOR = new Color(52, 73, 94); // Asphalt
    public static final Color TABLE_GRID_COLOR = new Color(220, 220, 220);
    public static final Color TABLE_EVEN_ROW_COLOR = new Color(245, 245, 245);
    public static final Color TABLE_ODD_ROW_COLOR = WHITE;


    // Fonts
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 18);

    // General Styling Method
    public static void applyTheme(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND_COLOR);
    }

    // Component Styling Methods
    public static void styleButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(WHITE);
        button.setFont(BOLD_FONT);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void styleBigButton(JButton button) {
        styleButton(button);
        button.setFont(HEADER_FONT);
        button.setBackground(SECONDARY_COLOR);
        button.setPreferredSize(new Dimension(200, 80));
    }

    public static void styleTable(JTable table, JScrollPane scrollPane) {
        table.setFont(MAIN_FONT);
        table.setForeground(FONT_COLOR);
        table.setGridColor(TABLE_GRID_COLOR);
        table.setRowHeight(25);
        table.setSelectionBackground(PRIMARY_COLOR);
        table.setSelectionForeground(WHITE);
        table.getTableHeader().setFont(BOLD_FONT);

        // Custom renderer for the table header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(TABLE_HEADER_COLOR);
        headerRenderer.setForeground(WHITE);
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        headerRenderer.setFont(BOLD_FONT);
        headerRenderer.setOpaque(true);

        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? TABLE_EVEN_ROW_COLOR : TABLE_ODD_ROW_COLOR);
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                } else {
                    c.setForeground(table.getForeground());
                }
                return c;
            }
        });

        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        scrollPane.setBorder(BorderFactory.createLineBorder(TABLE_GRID_COLOR));
    }

    public static JPanel createHeaderPanel(String title) {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(TABLE_HEADER_COLOR);
        JLabel headerLabel = new JLabel(title);
        headerLabel.setFont(TITLE_FONT);
        headerLabel.setForeground(WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        headerPanel.add(headerLabel);
        return headerPanel;
    }
}

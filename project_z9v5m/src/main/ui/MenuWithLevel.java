package ui;

import model.*;
import javax.swing.*;
import java.awt.*;

// Represents an abstract class that requires a level grid.
public abstract class MenuWithLevel {
    protected final JPanel panel;
    protected final PipeGameApp app;

    protected JPanel gridPanel;
    protected Level level;

    // MODIFIES: PipeGameApp (as well as this)
    // EFFECTS: sets given app and panel as borderLayout.
    public MenuWithLevel(PipeGameApp app) {
        panel = new JPanel(new BorderLayout());
        this.app = app;
    }

    // MODIFIES: this, PipeGameApp
    // EFFECTS: paints the level onto grid of buttons, and adds connector pipes as JLabels on the edges
    public void connectToLevel(Level level, char boardType) {
        this.level = level;
        panel.removeAll();
        gridPanel = new JPanel(new GridLayout(11, 11));
        addColumnLabels();
        for (int y = 0; y < 10; y++) {
            JLabel label;
            if (level.getConnectorY() == y) {
                ImageIcon westConnectingPipe = new ImageIcon("data/1STRAIGHT.png");
                label = new JLabel(westConnectingPipe, JLabel.CENTER);
            } else {
                label = new JLabel(String.valueOf(y), JLabel.CENTER);
            }
            gridPanel.add(label);

            for (int x = 0; x < 10; x++) {
                int pos = y * 10 + x;
                JButton button = new JButton();
                updateIcon(button, pos, boardType);
                addActionListenerForGridButton(button, pos);
                gridPanel.add(button);
            }
        }
        panel.add(gridPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this, PipeGameApp
    // EFFECTS: helper function that adds column labels to button gridLayout
    private void addColumnLabels() {
        gridPanel.add(new JLabel("*", JLabel.CENTER));
        for (int x = 0; x < 10; x++) {
            JLabel label;
            if (level.getConnectorX() == x) {
                ImageIcon northConnectingPipe = new ImageIcon("data/0STRAIGHT.png");
                label = new JLabel(northConnectingPipe, JLabel.CENTER);
            } else {
                label = new JLabel(String.valueOf(x), JLabel.CENTER);
            }
            gridPanel.add(label);
        }
    }

    protected abstract void addActionListenerForGridButton(JButton button, int pos);

    // MODIFIES: this, PipeGameApp
    // EFFECTS: adds/changes the icon on a gridButton
    protected void updateIcon(JButton button, int pos, char boardType) {
        Pipe p = level.getPipe(pos);
        String iconDirectory = p.getPipeSymbol(boardType);

        ImageIcon icon = new ImageIcon("data/" + iconDirectory + ".png");
        button.setIcon(icon);

    }

    public JPanel getPanel() {
        return panel;
    }
}

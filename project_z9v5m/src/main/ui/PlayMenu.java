package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents Play Menu where the user can rotate the pipes until the pipes match the rotations of the solution.
// Opens/Creates the WinBox when the solution is reached
public class PlayMenu extends MenuWithLevel {
    protected long startTime;

    public PlayMenu(PipeGameApp app) {
        super(app);
    }

    // MODIFIES: this, Level, Pipe, PipeGameApp
    // EFFECTS: adds level pipes to the grid, adds the startTime, and adds instructions tho the panel
    public void connectToLevel(Level level, long startTime) {
        super.connectToLevel(level, 'u');
        this.startTime = startTime;
        JLabel instructions = new JLabel("Press pipes to rotate 90 degrees Clockwise until pipes connect");
        panel.add(instructions, BorderLayout.PAGE_END);
    }

    // MODIFIES: this, PipeGameApp, Level, Pipe
    // EFFECTS: rotates the pipe of button that was pressed. If solution is reached, instantiates WinBox and score
    @Override
    protected void addActionListenerForGridButton(JButton button, int pos) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pipe rotPipe = level.getPipe(pos);
                rotPipe.rotate('u');
                updateIcon(button, pos, 'u');
                if (level.isSolution()) {
                    long endTime = System.currentTimeMillis();
                    long score = ((endTime - startTime) / 1000);
                    new WinBox(app, level, score);
                }
            }
        });
    }
}

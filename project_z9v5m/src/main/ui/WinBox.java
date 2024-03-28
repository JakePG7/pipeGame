package ui;

import model.Level;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents JDialog box that pops up when the level is complete. Shows score and high score and
// disables access to level. User can return to the MainMenu by pressing button.
public class WinBox {
    private final PipeGameApp app;
    private final JDialog creatorBox;
    private final JButton returnToMainMenuButton;

    // MODIFIES: PlayMenu, PipeGameApp, Level (as well as this)
    // EFFECTS: adds GUI to new JDialog Box and edits the high score if necessary
    public WinBox(PipeGameApp app, Level level, long score) {
        this.app = app;
        JFrame frame = app.getFrame();
        creatorBox = new JDialog(frame, "Congratulations!", true);
        creatorBox.setLayout(new BoxLayout(creatorBox.getContentPane(), BoxLayout.Y_AXIS));
        creatorBox.setSize(400,360);
        creatorBox.setLocation(frame.getLocation());

        level.checkBestTime(score);

        returnToMainMenuButton = new JButton("Return to Main Menu");
        addButtonActionListener();
        creatorBox.add(new JLabel("Congratulations! You've completed the puzzle!"));
        creatorBox.add(new JLabel("Your time was " + score + "s."));
        creatorBox.add(new JLabel("Your best time is " + level.getBestTime() + "s."));
        creatorBox.add(returnToMainMenuButton);
        creatorBox.setVisible(true);
    }

    // MODIFIES: this, MainMenu, PipeGameApp
    // EFFECTS: removes the WinBox and returns to the main menu
    private void addButtonActionListener() {
        returnToMainMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                creatorBox.setVisible(false);
                app.returnToMainMenu();
            }
        });
    }
}

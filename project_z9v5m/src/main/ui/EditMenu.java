package ui;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static model.PipeType.*;

// Represents the Edit Level Menu where the user can change the solution of the level using various functions
public class EditMenu extends MenuWithLevel {

    private static final String[] FUNCTIONS = {"Rotate Clockwise", "Make Straight Pipe", "Make L-Shaped Pipe",
            "Make 3-Way Pipe", "Make 4-Way Pipe", "Remove Pipe"};
    private JComboBox<String> functionSelector;
    private JButton finishEditingButton;

    public EditMenu(PipeGameApp app) {
        super(app);
    }

    // MODIFIES: this, PipeGameApp
    // EFFECTS: Adds a level grid to the editor, as well as it's functions and finish button
    public void connectToLevel(Level level) {
        super.connectToLevel(level, 's');
        JPanel lowerBoxPanel = new JPanel();
        lowerBoxPanel.setLayout(new BoxLayout(lowerBoxPanel, BoxLayout.Y_AXIS));
        functionSelector = new JComboBox<>(FUNCTIONS);
        finishEditingButton = new JButton("Finish Editing");
        addActionListenerForOther();
        lowerBoxPanel.add(new JLabel("Select from the following functions:"));
        lowerBoxPanel.add(functionSelector);
        lowerBoxPanel.add(finishEditingButton);
        panel.add(lowerBoxPanel, BorderLayout.PAGE_END);
    }

    // REQUIRES: 0 < pos < 99
    // MODIFIES: this, PipeGameApp, Level, Pipe
    // EFFECTS: edits the solution of the selected pipe to given specification
    @Override
    protected void addActionListenerForGridButton(JButton button, int pos) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFunction = (String)functionSelector.getSelectedItem();
                Pipe pipe = level.getPipe(pos);
                processEditChoice(selectedFunction, pipe); // selectedFunction is impossible to be null in this case.

                updateIcon(button, pos, 's');
            }
        });
    }

    // MODIFIES: this, PipeGameApp, Level, Pipe
    // EFFECTS: helper function that determines the type of function
    private void processEditChoice(String selectedFunction, Pipe pipe) {
        switch (selectedFunction) {
            case "Rotate Clockwise":
                pipe.rotate('s');
                break;
            case "Make Straight Pipe":
                pipe.setType(STRAIGHT);
                break;
            case "Make L-Shaped Pipe":
                pipe.setType(L_SHAPE);
                break;
            case "Make 3-Way Pipe":
                pipe.setType(T_SHAPE);
                break;
            case "Make 4-Way Pipe":
                pipe.setType(CROSS);
                break;
            default:
                pipe.setType(NULL);
                break;
        }
        chooseLogToEdit(pipe, selectedFunction);
    }

    // MODIFIES: this, MainMenu, PipeGameApp
    // EFFECTS: ends editMenu functions temporarily and returns to MainMenu
    private void addActionListenerForOther() {
        finishEditingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.returnToMainMenu();
            }
        });
    }

    // EFFECTS: chooses which type of log to perform based on selectedFunction
    private void chooseLogToEdit(Pipe pipe, String selectedFunction) {
        if (selectedFunction.equals("Rotate Clockwise")) {
            Pipe.logEditChoice("rotated");
        } else {
            Pipe.logEditChoice("converted to " + pipe.getType().name());
        }
    }
}
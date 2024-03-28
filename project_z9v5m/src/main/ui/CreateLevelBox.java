package ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents a JDialogBox that allows the user to select features of the level they're creating
public class CreateLevelBox {
    private final PipeGameApp app;
    private final JDialog creatorBox;
    private final JTextField titleEntry;
    private JSlider chooseXConnector;
    private JSlider chooseYConnector;
    private final JButton createButton;


    // MODIFIES: PipeGameApp (as well as this)
    // EFFECTS: produces dialogbox with components in the frame. Also sets it to be visible
    public CreateLevelBox(PipeGameApp app) {
        this.app = app;
        JFrame frame = app.getFrame();
        creatorBox = new JDialog(frame, "Level Creator", true);
        creatorBox.setLayout(new BoxLayout(creatorBox.getContentPane(), BoxLayout.Y_AXIS));
        creatorBox.setSize(400,360);
        creatorBox.setLocation(frame.getLocation());
        JLabel titleLabel = new JLabel("Enter your level's title:");
        titleEntry = new JTextField();
        JLabel sliderLabel = new JLabel("Slide to pos. that pipes should start and end on the edges:");
        instantiateSliders();
        createButton = new JButton("Create!");
        addButtonActionListener();
        creatorBox.add(titleLabel);
        creatorBox.add(titleEntry);
        creatorBox.add(sliderLabel);
        creatorBox.add(chooseXConnector);
        creatorBox.add(chooseYConnector);
        creatorBox.add(createButton);
        creatorBox.setVisible(false);
    }

    // MODIFIES: this, PipeGameApp
    // EFFECTS: helper function that instantiates the sliders
    private void instantiateSliders() {
        chooseXConnector = new JSlider(JSlider.HORIZONTAL, 0, 9, 0);
        chooseXConnector.setMajorTickSpacing(1);
        chooseXConnector.setPaintLabels(true);
        chooseXConnector.setPaintTicks(true);
        chooseYConnector = new JSlider(JSlider.VERTICAL, 0, 9, 0);
        chooseYConnector.setMajorTickSpacing(1);
        chooseYConnector.setPaintLabels(true);
        chooseYConnector.setPaintTicks(true);
        chooseYConnector.setInverted(true);
    }

    // MODIFIES: this
    // EFFECTS: allows the box to be seen again and resets the textField
    public void reOpen() {
        creatorBox.setVisible(true);
        titleEntry.setText("");
    }

    // MODIFIES: this, PipeGameApp, Level, Pipe, MainMenu
    // EFFECTS: adds the level with given specifications to the list of levels and removes the creatorBox
    private void addButtonActionListener() {
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = titleEntry.getText();
                int x = chooseXConnector.getValue();
                int y = chooseYConnector.getValue();
                app.makeNewLevel(name, x, y);
                creatorBox.setVisible(false);
            }
        });
    }
}

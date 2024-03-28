package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the starting screen, where the user can select from a variety of options through buttons, and select a
// level for which they would like to have the option operate on. Also has an informer textField for messages to user.
public class MainMenu {
    private final PipeGameApp app;
    private final JPanel panel;
    private final JLabel informer;
    private JComboBox<String> levelBox;
    private final JButton playButton;
    private final JButton editButton;
    private final JButton createButton;
    private final JButton saveButton;
    private final JButton loadButton;
    private final CreateLevelBox createLevelDialogBox;
    private JPanel introPanel;

    // MODIFIES: PipeGameApp (as well as this)
    // EFFECTS: creates the GUI for the Main Menu
    public MainMenu(PipeGameApp pipeGameApp) {
        app = pipeGameApp;
        panel = new JPanel(new BorderLayout());
        informer = new JLabel();
        createLevelDialogBox = new CreateLevelBox(app);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        playButton = new JButton("Play Level");
        editButton = new JButton("Edit Level");
        createButton = new JButton("Create Level");
        saveButton = new JButton("Save Levels");
        loadButton = new JButton("Load Levels");

        addGameActionListeners();
        addSaveActionListeners();
        addInstructionPanel();

        buttonPanel.add(playButton);
        buttonPanel.add(editButton);
        buttonPanel.add(createButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(loadButton);

        panel.add(introPanel, BorderLayout.PAGE_START);
        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.add(informer, BorderLayout.PAGE_END);
    }

    // MODIFIES: PipeGameApp, this
    // EFFECTS: helper function that adds the instructions to the menu with the level box on it.
    private void addInstructionPanel() {
        introPanel = new JPanel();
        introPanel.setLayout(new BoxLayout(introPanel, BoxLayout.PAGE_AXIS));
        introPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        levelBox = new JComboBox<>();
        introPanel.add(levelBox);
        updateLevelBox();

        // Adds to BoxLayout in Order:
        introPanel.add(new JLabel("Welcome to Jake PG's Pipe Game!"));
        introPanel.add(new JSeparator()); // purely for aesthetics
        introPanel.add(new JLabel("Instructions:"));
        introPanel.add(new JLabel("- Select a level and try to rotate the pipes until all pipes connect to"));
        introPanel.add(new JLabel("the connecting pipes on the top and left edges without any loose pipes."));
        introPanel.add(new JLabel("- Edit or create a new level to make a solution."));
        introPanel.add(new JLabel("Make sure your solution is valid!"));
        introPanel.add(new JLabel("- Create a level and set start and end pipes and give a name to your level"));
        introPanel.add(new JLabel("- Add, remove, and rotate pipes to your liking in the level editor."));
        introPanel.add(new JLabel("- Try and beat your times for each level!"));
        introPanel.add(levelBox);
    }

    // MODIFIES: this, PipeGameApp
    // EFFECTS: helper function that sets functions for when game buttons on the Main menu are pressed
    private void addGameActionListeners() {
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textToInform = app.checkLevelPlayable(levelBox.getSelectedIndex());
                // only writes something useful if automatic win
                informer.setText(textToInform);
            }

        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                app.editLevel(levelBox.getSelectedIndex());
            }
        });
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createLevelDialogBox.reOpen();
                updateLevelBox();
                app.editLevel(app.getLevelsSize() - 1);
            }
        });
    }

    // MODIFIES: this, PipeGameApp, JsonReader, JsonWriter
    // EFFECTS: helper function that sets functions for when save and load buttons on the Main menu are pressed
    private void addSaveActionListeners() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textToInform = app.saveGame();
                informer.setText(textToInform);
            }
        });
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String textToInform = app.loadGame();
                informer.setText(textToInform);
                updateLevelBox();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: adds/edits the new/changed level to the JComboBox
    public void updateLevelBox() {
        introPanel.remove(levelBox);
        String[] levelList = app.updateLevelList();
        levelBox = new JComboBox<>(levelList);
        introPanel.add(levelBox);
    }

    public JPanel getPanel() {
        return panel;
    }
}

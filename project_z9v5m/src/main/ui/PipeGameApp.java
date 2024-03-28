package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

// ASCII code source:https://theasciicode.com.ar/extended-ascii-code/box-drawing-character-double
// -line-lower-left-corner-ascii-code-200.html

// Represents the ui for the pipe game app. Begins with the main menu, where the user can be directed
// to the level editing menu, play a level, create a level, or save and load levels from a file/
// Level is played by rotating the pipes until each is rotated as given in the solution.
//  The solution is set in the editing menu. There is no verifying that the pipes actually go together,
//  relies on user's trust that it is correct.
public class PipeGameApp {

    private static final String JSON_STORE = "./data/levels.json";
    private ArrayList<Level> levels = new ArrayList<>();
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    private JFrame frame;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private MainMenu mainMenu;
    private EditMenu editMenu;
    private PlayMenu playMenu;

    // EFFECTS: initializes accounts, adds default empty levels to levels list
    public PipeGameApp() {
        Level level0 = new Level("Level 0", 2, 3);
        Level level1 = new Level("Level 1", 5, 0);
        levels.add(level0);
        levels.add(level1);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // MODIFIES: this, MainMenu, EditMenu, PlayMenu
    // EFFECTS: Starts application frame, and instantiates 3 menu panes.
    // automatically starts with the main menu.
    public void createAndShowGUI() {
        frame = new JFrame("Jake PG's Pipe Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addWindowListener();

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        mainMenu = new MainMenu(this);
        editMenu = new EditMenu(this);
        playMenu = new PlayMenu(this);

        cardPanel.add(mainMenu.getPanel(), "Main Menu");
        cardPanel.add(editMenu.getPanel(), "Edit Menu");
        cardPanel.add(playMenu.getPanel(), "Play Menu");

        frame.add(cardPanel);

        frame.setSize(600, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true); // Will automatically show the mainMenu panel
    }

    // EFFECTS: Adds listener that allows for the printing of the log on the console once the application is closed.
    private void addWindowListener() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                for (Event e : EventLog.getInstance()) {
                    System.out.println(e.toString());
                }
            }
        });
    }

    // EFFECTS: calls choose level sequence and plays level unless level is an automatic win, which
    //          also returns text to informer if necessary
    public String checkLevelPlayable(int levelIndex) {
        Level chosenLevel = levels.get(levelIndex);
        if (chosenLevel.isAutomaticWin()) {
            return "This level is an Automatic win! Try editing it first.";
        } else {
            playLevel(chosenLevel);
            return "";
        }
    }

    // REQUIRES: , 0 <= x <= 9, 0 <= y <= 9
    // MODIFIES: this (initializes level and pipe as well)
    // EFFECTS: appends a new level to the levels list starting with all null pipes
    //          new level has name and unchangeable start and end pipes
    public void makeNewLevel(String name, int x, int y) {
        Level newLevel = new Level(name, x, y);
        levels.add(newLevel);
    }

    // MODIFIES: this, level, pipe
    // EFFECTS: allows the user to rotate their pipes until the solution matches the board.
    public void playLevel(Level level) {
        level.shuffle(); // makes sure it has been shuffled at least once
        while (level.isSolution()) {
            level.shuffle();
        }
        long startTime = System.currentTimeMillis();
        playMenu.connectToLevel(level, startTime);
        cardLayout.show(cardPanel, "Play Menu");
    }

    // MODIFIES: this, pipe, level
    // EFFECTS: switches to Edit Menu Screen
    public void editLevel(int levelIndex) {
        Level level = levels.get(levelIndex);
        editMenu.connectToLevel(level);
        cardLayout.show(cardPanel, "Edit Menu");
    }

    // EFFECTS: Prints list of levels in level in form: "(index) - (name) | Best Time: (bestTime)s
    public String[] updateLevelList() {
        ArrayList<String> levelsWithDetails = new ArrayList<>();
        for (Level l : levels) {
            String name = l.getName();
            String bestTime = Long.toString(l.getBestTime());
            levelsWithDetails.add(name + " | Best Time: " + bestTime + "s");
        }
        Level.logDisplay();
        String[] arrayToReturn = new String[levelsWithDetails.size()];
        arrayToReturn = levelsWithDetails.toArray(arrayToReturn);
        return arrayToReturn;
    }

    // MODIFIES: MainMenu
    // EFFECTS: saves the levels and scores to file, returns text to inform user
    public String saveGame() {
        try {
            jsonWriter.open();
            jsonWriter.write(this.levels);
            jsonWriter.close();
            return "Your game has been saved to " + JSON_STORE;
        } catch (FileNotFoundException e) {
            return "Unable to write to file: " + JSON_STORE;
        }
    }

    // MODIFIES: this, MainMenu
    // EFFECTS: loads levels and scores from file, returns text to inform user
    public String loadGame() {
        try {
            this.levels = jsonReader.read();
            return "Loaded your game from " + JSON_STORE;
        } catch (IOException e) {
            return "Unable to read from file: " + JSON_STORE;
        }
    }

    // MODIFIES: this, MainMenu, MenuWithLevel
    // EFFECTS: adds/changes levels in the levelBox and returns to the main menu panel
    public void returnToMainMenu() {
        mainMenu.updateLevelBox();
        cardLayout.show(cardPanel, "Main Menu");
    }

    public JFrame getFrame() {
        return frame; // Used in CreateLevelBox
    }

    // EFFECTS: returns the amount of levels in the levels list
    public int getLevelsSize() {
        return levels.size();
    }

}
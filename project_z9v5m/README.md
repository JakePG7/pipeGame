# Jake PG's Pipe Game

## An accessible game for all ages

This project will use java to create a game where the player attempts to rotate plumbing pipes laid out in a grid to
connect the start and end pipes. The game is a race against time to connect the pipes without any disconnections. 
Since this is a simple project, the game will not check if the pipe ends connect to each other, only will compare it to 
the solution the user provides. Scores will be recorded through times to complete the level. 

## User Stories

- I want the game to have a title screen with buttons to the different screens
- I want the game to have a few standard levels that appear in a level screen (a list) 
- I want the pipes to rotate 90 degrees clockwise when clicked agan 
- I want the game to have a level editor screen, where users can freely place pipes in a distinct grid
- I want the user to append their created levels to the level screen
- I want the levels to be editable at any time
- I want the level editor to have a selection bar for the different types of pipes that the user can add
- I want the user to choose where the start and end pipes are located
- I want each level to have an end screen displaying your time
- I want the game to have a high score screen of fastest times for each level
- I want times and levels to be savable to a user's name 
- I want the times and levels to be loadable from a file

## Phase 4: Task 2 (EventLog Example)

The following events are logged when the game is opened, an initial level is edited (to Straight pipe type and rotated),
a level named "TestLevel" is created and edited (to t-shape pipe type), then quit in the main menu.

- Fri Dec 01 08:19:16 PST 2023 
- Level 0 was created and added to list of levels
- Fri Dec 01 08:19:16 PST 2023
- Level 1 was created and added to list of levels
- Fri Dec 01 08:19:16 PST 2023
- Levels were displayed in a JComboBox
- Fri Dec 01 08:19:21 PST 2023
- Pipe was converted to STRAIGHT
- Fri Dec 01 08:19:26 PST 2023
- Pipe was rotated
- Fri Dec 01 08:19:27 PST 2023
- Pipe was rotated
- Fri Dec 01 08:19:28 PST 2023
- Levels were displayed in a JComboBox
- Fri Dec 01 08:19:36 PST 2023
- Test Level was created and added to list of levels
- Fri Dec 01 08:19:36 PST 2023
- Levels were displayed in a JComboBox
- Fri Dec 01 08:19:41 PST 2023
- Pipe was converted to T_SHAPE
- Fri Dec 01 08:19:45 PST 2023
- Levels were displayed in a JComboBox

## Phase 4: Task 3 (Design Review)

In phase 3,
I had tried to cohesively separate the Menus, which did prove to be more effective than the alternative. There is
still, however, quite a bit of design flaws still present in this code.
The UML truly reveals the tight coupling within PipeGameApp, which acts as the general manager for the game. 
Some of the GUI Components don't need direct access to the Gameplay features and vice versa. Therefore PipeGameApp 
can be split into a GUIManager and a GameplayManager to loosen the coupling and consequently allows the
class to be more cohesive. Classes with a lot of unrelated buttons like MainMenu should be offshot into a buttonManager 
to increase cohesion. Creating a seperate LevelList model class could also relieve PipeGameApp's coupling, since
most functions that change something in the level list do not use other properties of PipeGameApp, and would again 
increase cohesion. Additionally, Level could be divided into LevelSolution & UserLevel, since they are almost never 
used together other than in construction, and could extend a Level class that contains some of their commonalities. 
As you can imagine, this would make the code more cohesive and loosen the coupling on the classes that only affect
solutions, such as EditMenu.

Most of the GUI classes can only use 1 instance at all times. Therefore, PipeGameApp (or equiv.), 
PlayMenu, MainMenu, EditMenu, and CreateLevelBox should be edited to conform to the Singleton pattern. this would also
allow the classes to get the app without requiring the app as a parameter in the constructor.

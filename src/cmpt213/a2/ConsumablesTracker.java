package cmpt213.a2;

import cmpt213.a2.textui.MainMenu;

/**
 * This is the entry point of the system.
 * Its only responsibility is to create and call the Main Menu.
 */
public class ConsumablesTracker {
    /**
     * Calls the main menu and terminates when complete.
     * @param args default argument given by Java
     */
    public static void main(String[] args) {
        MainMenu mainMenu = new MainMenu();
        mainMenu.mainMenu();
    }
}
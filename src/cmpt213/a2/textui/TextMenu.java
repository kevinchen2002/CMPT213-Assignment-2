package cmpt213.a2.textui;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * interface that the user interacts with
 * called by Main when an option select is needed
 */
public class TextMenu {

    /**
     * stores the title of the menu; displayed once at the start
     */
    private final String title = "\n" +
            "   ______                                       __    __             ______                __            \n" +
            "  / ____/___  ____  _______  ______ ___  ____ _/ /_  / /__  _____   /_  __/________ ______/ /_____  _____\n" +
            " / /   / __ \\/ __ \\/ ___/ / / / __ `__ \\/ __ `/ __ \\/ / _ \\/ ___/    / / / ___/ __ `/ ___/ //_/ _ \\/ ___/\n" +
            "/ /___/ /_/ / / / (__  ) /_/ / / / / / / /_/ / /_/ / /  __(__  )    / / / /  / /_/ / /__/ ,< /  __/ /    \n" +
            "\\____/\\____/_/ /_/____/\\__,_/_/ /_/ /_/\\__,_/_.___/_/\\___/____/    /_/ /_/   \\__,_/\\___/_/|_|\\___/_/     \n" +
            "                                                                                                         \n";

    private final String titleText = "Consumables Tracker";

    /**
     * stores menu options
     */
    private final String[] menuOptions = new String[]{"List food items", "Add new food item", "Remove food item",
            "List expired food items", "List non-expired food items", "List food items expiring within 7 days", "Exit"};

    private final int NUM_MENU_OPTIONS = 7;

    /**
     * constructor which does nothing
     */
    public TextMenu() {
    }

    /**
     * prints the title; should only be called once upon startup
     */
    public void printTitle() {
        System.out.println(title);
    }

    /**
     * prints the smaller title with the hashtags; called every time the menu is shown
     */
    public void printSubTitle() {
        int numHashtags = titleText.length() + 4;
        String hashtags = "#";
        hashtags = hashtags.repeat(numHashtags);
        String fullTitle = "\n";
        fullTitle += hashtags;
        fullTitle += "\n# " + titleText + " #\n";
        fullTitle += hashtags;
        System.out.println(fullTitle);
    }

    /**
     * shows menu with current date and allows user to select an option
     *
     * @return the menu option that the user chose
     */
    public int displayMenu() {
        printSubTitle();
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("Today is: " + currentTime.format(formatter) + "\n");

        for (int i = 1; i <= NUM_MENU_OPTIONS; i++) {
            System.out.println(i + ": " + menuOptions[i - 1]);
        }

        int choice = -1;
        Scanner in = new Scanner(System.in);
        while (choice < 1 || choice > 7) {
            System.out.println("Choose an option by entering 1 - 7: ");
            try {
                choice = Integer.parseInt(in.nextLine());
            } catch (NumberFormatException nfe) {
                System.out.println("Not an integer");
            }
        }

        return choice;
    }
}

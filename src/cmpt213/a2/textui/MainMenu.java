package cmpt213.a2.textui;

import cmpt213.a2.model.Consumable;
import cmpt213.a2.model.ConsumableFactory;
import cmpt213.a2.model.FoodItem;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Main class
 */
public class MainMenu {

    /**
     * ArrayList of FoodItems
     */
    static ArrayList<Consumable> consumableList = new ArrayList<>();

    static String fileName = "data.json";

    /**
     * helper function that ensures numerical input
     * @return a valid integer
     */
    static int getInt() {
        int choice;
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                choice = Integer.parseInt(in.nextLine());
                return choice;
            } catch (NumberFormatException nfe) {
                System.out.println("Not an integer");
            }
        }
    }

    /**
     * helper function that ensures numerical input
     * @return a valid double
     */
    static double getDouble() {
        double choice;
        Scanner in = new Scanner(System.in);
        while (true) {
            try {
                choice = Double.parseDouble(in.nextLine());
                return choice;
            } catch (NumberFormatException nfe) {
                System.out.println("Not a double!");
            }
        }
    }

    static LocalDateTime getLocalDateTime() {
        while (true) {
            int year = -1;
            int month = -1;
            int day = -1;
            LocalDateTime expiry;
            try {
                final int MIN_YEAR = 2000;
                while (year < MIN_YEAR) {
                    System.out.println("Enter the year of the expiry date: ");
                    year = getInt();
                }
                final int MAX_MONTH = 12;
                while (month < 1 || month > MAX_MONTH) {
                    System.out.println("Enter the month of the expiry date: ");
                    month = getInt();
                }
                final int MAX_DAY = 31;
                while (day < 1 || day > MAX_DAY) {
                    System.out.println("Enter the day of the expiry date: ");
                    day = getInt();
                }
                expiry = LocalDateTime.of(year, month, day, 23, 59);
                return expiry;
            } catch(DateTimeException e) {
                System.out.println("Invalid date!\n");
            }
        }
    }

    /**
     * menu option 1; lists all food items
     */
    static void listFood() {
        if (consumableList.isEmpty()) {
            System.out.println("There are no consumable items!");
        }
        int itemNum = 1;
        for (Consumable item : consumableList) {
            System.out.println("\nConsumable Item #" + itemNum);
            System.out.println(item);
            itemNum++;
        }
    }

    /**
     * menu option 2; takes in fields and adds a food item
     * looks at the list to ensure that it is inserted in order by expiration date
     * this ensures that sorting will not be needed in the future
     */
    static void addFood() {
        Scanner in = new Scanner(System.in);

        FoodItem dummy = new FoodItem("dummy", "dummy", 1, 1, LocalDateTime.now());
        consumableList.add(dummy);

        int itemType = 0;
        while (itemType < 1 || itemType > 2) {
            System.out.println("Is this a [1] Food item or [2] Drink item?");
            itemType = getInt();
        }

        boolean isFood;
        isFood = itemType == 1;

        String itemName = "";
        while (itemName.equals("")) {
            System.out.println("Enter the name of the new consumable item: ");
            itemName = in.nextLine();
        }

        String itemNotes;
        System.out.println("Enter any notes for the new consumable item: ");
        itemNotes = in.nextLine();

        //get price
        double price = -1.0;
        while (price < 0) {
            System.out.println("Enter the price of this consumable item: ");
            price = getDouble();
        }

        //get weight
        double weightOrVolume = -1;
        while (weightOrVolume < 0) {
            if (isFood) {
                System.out.println("Enter the weight of this consumable item: ");
            } else {
                System.out.println("Enter the volume of this consumable item: ");
            }
            weightOrVolume = getDouble();
        }

        LocalDateTime expiry = getLocalDateTime();
        Consumable newConsumableItem;
        if (isFood) {
            newConsumableItem = ConsumableFactory.getConsumable(true, itemName, itemNotes, price, weightOrVolume, expiry);
        } else {
            newConsumableItem = ConsumableFactory.getConsumable(false, itemName, itemNotes, price, weightOrVolume, expiry);
        }
        //insert in the correct spot to ensure ascending order of dates
        int maxSize = consumableList.size();
        for (int i = 0; i < maxSize; i++) {
            if (expiry.isBefore(consumableList.get(i).getExpDate())) {
                consumableList.add(i, newConsumableItem);
                break;
            }
            else if (i == maxSize-1) {
                consumableList.add(newConsumableItem);
                break;
            }
        }
        consumableList.remove(dummy);
        System.out.println("Item " + itemName + " has been added!");
    }

    /**
     * menu option 3; removes a food
     */
    static void removeFood() {
        listFood();
        //get the item that the user will delete
        int toDelete = -1;
        while (toDelete < 1 || toDelete > consumableList.size()) {
            System.out.println("Which item would you like to delete? 0 to cancel.");
            toDelete = getInt();
            if (toDelete == 0) {
                return;
            }
        }
        Consumable removed = consumableList.get(toDelete-1);
        consumableList.remove(toDelete-1);
        System.out.println(removed.getName() + " has been removed!");
    }

    /**
     * menu option 4; lists expired foods
     */
    static void listExpired() {
        if (consumableList.isEmpty()) {
            System.out.println("There are no food items!");
        }
        int itemNum = 1;
        boolean noExpired = true;
        for (Consumable item : consumableList) {
            if (item.isExpired()) {
                System.out.println("\nFood Item #" + itemNum);
                System.out.println(item);
                noExpired = false;
                itemNum++;
            }
        }
        if (noExpired && consumableList.size() != 0) {
            System.out.println("There are no expired items!");
        }
    }

    /**
     * menu option 5; lists non-expired foods
     */
    static void listNotExpired() {
        if (consumableList.isEmpty()) {
            System.out.println("There are no consumable items!");
        }
        int itemNum = 1;
        boolean allExpired = true;
        for (Consumable item : consumableList) {
            if (!item.isExpired()) {
                System.out.println("\nConsumable Item #" + itemNum);
                System.out.println(item);
                allExpired = false;
                itemNum++;
            }
        }
        if (allExpired && consumableList.size() != 0) {
            System.out.println("All consumable items are expired!");
        }
    }

    /**
     * menu option 6; lists foods expiring within seven days
     */
    static void expiringSevenDays() {
        if (consumableList.isEmpty()) {
            System.out.println("There are no consumable items!");
        }
        int itemNum = 1;
        boolean noneWithinSevenDays = true;
        for (Consumable item : consumableList) {
            if (item.getDaysUntilExp() <= 7 && !item.isExpired()) {
                System.out.println("\nConsumable Item #" + itemNum);
                System.out.println(item);
                noneWithinSevenDays = false;
                itemNum++;
            }
        }
        if (noneWithinSevenDays && consumableList.size() != 0) {
            System.out.println("There are no consumables expiring within 7 days!");
        }
    }

    /**
     * calls the menu from TextMenu object and performs the corresponding operation
     */
    static void mainMenu() {
        TextMenu menu = new TextMenu();
        menu.printTitle();
        int choice = 0;
        while (choice != 7) {
            choice = menu.displayMenu();
            switch (choice) {
                case 1 -> listFood();
                case 2 -> addFood();
                case 3 -> removeFood();
                case 4 -> listExpired();
                case 5 -> listNotExpired();
                case 6 -> expiringSevenDays();
                case 7 -> writeFile();
                default -> System.out.println("Pick one of the above options");
            }
        }

    }

    /**
     * Creates a new data.json file if needed; derived from https://www.w3schools.com/java/java_files_create.asp
     */
    static void createFile() {
        try {
            File foodStorage = new File(fileName);
            if (foodStorage.createNewFile()) {
                System.out.println("File data.json created!");
            }
        } catch (IOException e) {
            System.out.println("Error while creating file");
            e.printStackTrace();
        }
    }

    /**
     * loads data.json file if it exists; derived from https://attacomsian.com/blog/gson-read-json-file
     */
    static void loadFile() {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            consumableList = myGson.fromJson(reader, new TypeToken<List<FoodItem>>() {}.getType());
            reader.close();
        } catch (NoSuchFileException e) {
            //if the file is not there, create it
            createFile();
            consumableList.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * writes to data.json upon shutdown; derived from https://attacomsian.com/blog/gson-write-json-file
     */
    static void writeFile() {
        Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter,
                                      LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }
                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                }).create();
        try {
            Writer writer = Files.newBufferedWriter(Paths.get(fileName));
            myGson.toJson(consumableList, writer);
            writer.close();

        } catch (NoSuchFileException e) {
            System.out.println("File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * main method
     * @param args command line argument
     */
    public static void main(String[] args) {
        loadFile();
        mainMenu();
    }
}

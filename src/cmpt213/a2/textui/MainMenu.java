package cmpt213.a2.textui;

import cmpt213.a2.gson.extras.RuntimeTypeAdapterFactory;
import cmpt213.a2.model.Consumable;
import cmpt213.a2.model.ConsumableFactory;
import cmpt213.a2.model.DrinkItem;
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
 * The MainMenu handles all operations of the system.
 * It is instantiated and called by ConsumablesTracker, the entry point.
 * data.json is loaded (or created if it does not exist) upon startup and saved when terminating.
 */
public class MainMenu {

    /**
     * ArrayList of FoodItems
     */
    private static ArrayList<Consumable> consumableList = new ArrayList<>();

    private static final String fileName = "data.json";

    /**
     * helper function that ensures numerical input
     *
     * @return a valid integer
     */
    private static int getInt() {
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
     *
     * @return a valid double
     */
    private static double getDouble() {
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

    /**
     * helper function that ensures valid date
     *
     * @return a valid date
     */
    private static LocalDateTime getLocalDateTime() {
        while (true) {
            int year;
            int month;
            int day;
            LocalDateTime expiry;
            try {
                final int MIN_YEAR = 2000;
                System.out.println("Enter the year of the expiry date: ");
                year = getInt();
                while (year < MIN_YEAR) {
                    System.out.println("The year must be at least 2000.");
                    year = getInt();
                }

                final int MAX_MONTH = 12;
                System.out.println("Enter the month of the expiry date: ");
                month = getInt();
                while (month < 1 || month > MAX_MONTH) {
                    System.out.println("The month must be between 1 and 12.");
                    month = getInt();
                }

                final int MAX_DAY = 31;
                System.out.println("Enter the day of the expiry date: ");
                day = getInt();
                while (day < 1 || day > MAX_DAY) {
                    System.out.println("The day must between 1 and 31.");
                    day = getInt();
                }
                expiry = LocalDateTime.of(year, month, day, 23, 59);
                return expiry;
            } catch (DateTimeException e) {
                System.out.println("Invalid date. Please enter a valid date.\n");
            }
        }
    }

    /**
     * menu option 1; lists all consumable items
     */
    private static void listConsumables() {
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
     * menu option 2; takes in fields and adds a consumable item
     * looks at the list to ensure that it is inserted in order by expiration date
     * this ensures that sorting will not be needed in the future
     */
    private static void addConsumable() {
        Scanner in = new Scanner(System.in);

        FoodItem dummy = new FoodItem("dummy", "dummy", 1, 1, LocalDateTime.now());
        consumableList.add(dummy);

        System.out.println("Is this a [1] Food item or [2] Drink item?");
        int itemType = getInt();
        while (itemType < 1 || itemType > 2) {
            System.out.println("Please select [1] Food item or [2] Drink item.");
            itemType = getInt();
        }

        boolean isFood;
        String consumableType;
        String dataType;
        if (itemType == 1) {
            isFood = true;
            consumableType = "food";
            dataType = "weight";
        } else {
            isFood = false;
            consumableType = "drink";
            dataType = "volume";
        }

        System.out.println("Enter the name of the new " + consumableType + " item: ");
        String itemName = in.nextLine();
        while (itemName.equals("")) {
            System.out.println("The name cannot be empty.");
            itemName = in.nextLine();
        }

        System.out.println("Enter any notes for the new " + consumableType + " item: ");
        String itemNotes = in.nextLine();

        //get price
        System.out.println("Enter the price of this " + consumableType + " item: ");
        double price = getDouble();
        while (price < 0) {
            System.out.println("The price cannot be negative.");
            price = getDouble();
        }

        //get weight or volume
        System.out.println("Enter the " + dataType + " of this " + consumableType + " item: ");
        double weightOrVolume = getDouble();
        while (weightOrVolume < 0) {
            System.out.println("The " + consumableType + " cannot be negative.");
            weightOrVolume = getDouble();
        }

        LocalDateTime expiry = getLocalDateTime();
        Consumable newConsumableItem;
        if (isFood) {
            newConsumableItem = ConsumableFactory.getInstance(true, itemName, itemNotes, price, weightOrVolume, expiry);
        } else {
            newConsumableItem = ConsumableFactory.getInstance(false, itemName, itemNotes, price, weightOrVolume, expiry);
        }
        //insert in the correct spot to ensure ascending order of dates
        int maxSize = consumableList.size();
        for (int i = 0; i < maxSize; i++) {
            if (expiry.isBefore(consumableList.get(i).getExpDate())) {
                consumableList.add(i, newConsumableItem);
                break;
            } else if (i == maxSize - 1) {
                consumableList.add(newConsumableItem);
                break;
            }
        }
        consumableList.remove(dummy);
        System.out.println("Item " + itemName + " has been added!");
    }

    /**
     * menu option 3; removes a consumable
     */
    private static void removeConsumable() {
        listConsumables();
        //get the item that the user will delete
        int toDelete = -1;
        while (toDelete < 1 || toDelete > consumableList.size()) {
            System.out.println("Which item would you like to delete? 0 to cancel.");
            toDelete = getInt();
            if (toDelete == 0) {
                return;
            }
        }
        Consumable removed = consumableList.get(toDelete - 1);
        consumableList.remove(toDelete - 1);
        System.out.println(removed.getName() + " has been removed!");
    }

    /**
     * menu option 4; lists expired consumables
     */
    private static void listExpired() {
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
     * menu option 5; lists non-expired consumables
     */
    private static void listNotExpired() {
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
     * menu option 6; lists consumables expiring within seven days
     */
    private static void listExpiringSevenDays() {
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
     * Creates a new data.json file if needed; derived from https://www.w3schools.com/java/java_files_create.asp
     */
    private static void createFile() {
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
     * Learned about RuntimeTypeAdapterFactory class from:
     * https://jansipke.nl/serialize-and-deserialize-a-list-of-polymorphic-objects-with-gson/
     * Downloaded RuntimeTypeAdapterFactory class from:
     * https://github.com/google/gson/blob/master/extras/src/main/java/com/google/gson/typeadapters/RuntimeTypeAdapterFactory.java
     * This is an extra feature of Gson used for deserializing polymorphic objects.
     * This class is provided by Google on the Gson GitHub page, with the link shown above.
     */
    private static final RuntimeTypeAdapterFactory<Consumable> runTimeTypeAdapterFactory = RuntimeTypeAdapterFactory
            .of(Consumable.class, "type")
            .registerSubtype(FoodItem.class, "food")
            .registerSubtype(DrinkItem.class, "drink");

    private static final Gson myGson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
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
            }).registerTypeAdapterFactory(runTimeTypeAdapterFactory).create();

    /**
     * loads data.json file if it exists; derived from https://attacomsian.com/blog/gson-read-json-file
     */
    private static void loadFile() {

        try {
            Reader reader = Files.newBufferedReader(Paths.get(fileName));
            consumableList = myGson.fromJson(reader, new TypeToken<List<Consumable>>() {
            }.getType());
            for (Consumable consumable : consumableList) {
                if (consumable instanceof FoodItem) {
                    consumable.setType("food");
                } else if (consumable instanceof DrinkItem) {
                    consumable.setType("drink");
                }
            }
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
    private static void writeFile() {

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
     * calls the menu from TextMenu object and performs the corresponding operation
     */
    public void mainMenu() {
        loadFile();
        TextMenu menu = new TextMenu();
        menu.printTitle();
        int choice = 0;
        while (choice != 7) {
            choice = menu.displayMenu();
            switch (choice) {
                case 1 -> listConsumables();
                case 2 -> addConsumable();
                case 3 -> removeConsumable();
                case 4 -> listExpired();
                case 5 -> listNotExpired();
                case 6 -> listExpiringSevenDays();
                case 7 -> writeFile();
                default -> System.out.println("Pick one of the above options");
            }
        }
    }
}
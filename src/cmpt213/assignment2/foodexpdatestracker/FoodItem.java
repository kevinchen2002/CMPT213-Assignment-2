package cmpt213.assignment2.foodexpdatestracker;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * FoodItem class
 * Stores fields name, notes, price, expiration date, days until expiry, and expiration status
 * FoodItem objects are stored in an ArrayList in Main
 */
public class FoodItem {

    private final String name;
    private final String notes;
    private final double price;
    private final LocalDateTime expDate;
    private int daysUntilExp;
    private boolean isExpired;

    /**
     * FoodItem constructor; requires information about the object
     *
     * @param name the name of the food item; cannot be empty
     * @param notes any other information; can be empty
     * @param price the price of this item
     * @param expDate the expiration date of this item
     */
    public FoodItem(String name, String notes, double price, LocalDateTime expDate) {

        //name cannot be empty; enforce with exception
        if (name.equals("")) {
            throw new IllegalArgumentException("Name of food cannot be empty.");
        }

        this.name = name;
        this.notes = notes;
        this.price = price;
        this.expDate = expDate;

        //update time until expiry upon construction; derived from https://mkyong.com/java8/java-8-difference-between-two-localdate-or-localdatetime/
        LocalDateTime currentTime = LocalDateTime.now();
        isExpired = !currentTime.isBefore(expDate);
        this.daysUntilExp = (int) ChronoUnit.DAYS.between(currentTime, expDate);
    }

    /**
     * getter for expiration date; used for date comparison
     * @return the expiration date
     */
    public LocalDateTime getExpDate() {
        return expDate;
    }

    /**
     * getter for the name of the item
     * @return the name of the item
     */
    public String getName() {return name;}

    /**
     * determines if a particular item is expired
     * @return the expiration status
     */
    public boolean isExpired() {return isExpired;}

    /**
     * getter for the days until expiration
     * @return the number of days until expiration
     */
    public int getDaysUntilExp() {return daysUntilExp;}

    /**
     * constructs a string with all information about this FoodItem
     * @return the string containing all relevant information
     */
    @Override
    public String toString() {
        String foodString = "";
        foodString += "Food: " + name;
        foodString += "\nNotes: " + notes;
        foodString += "\nPrice: " + price;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        foodString += "\nExpiry date: " + expDate.format(formatter);

        //update time until expiry every time the food is displayed; derived from https://mkyong.com/java8/java-8-difference-between-two-localdate-or-localdatetime/
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(expDate)) {
            isExpired = false;
            int daysUntilExpiry = (int) ChronoUnit.DAYS.between(currentTime, expDate);
            this.daysUntilExp = daysUntilExpiry;
            if (daysUntilExpiry <= 0) {
                foodString += "\nThis food item will expire today.";
            }
            else {
                foodString += "\nThis food will expire in " + daysUntilExpiry + " day(s).";
            }
        }
        else {
            this.isExpired = true;
            int daysUntilExpiry = (int) ChronoUnit.DAYS.between(currentTime, expDate);
            this.daysUntilExp = daysUntilExpiry;
            daysUntilExpiry = -daysUntilExpiry;
            foodString += "\nThis food has been expired for " + daysUntilExpiry + " days!";
        }

        return foodString;
    }

}

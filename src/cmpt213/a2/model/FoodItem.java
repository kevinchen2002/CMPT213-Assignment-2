package cmpt213.a2.model;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * FoodItem class
 * Stores fields name, notes, price, expiration date, days until expiry, and expiration status
 * FoodItem objects are stored in an ArrayList in Main
 */
public class FoodItem extends Consumable {
    double weight;

    public FoodItem(String name, String notes, double price, double weight, LocalDateTime expDate) {

        //name cannot be empty; enforce with exception
        if (name.equals("")) {
            throw new IllegalArgumentException("Name of food cannot be empty.");
        }

        this.name = name;
        this.notes = notes;
        this.price = price;
        this.weight = weight;
        this.expDate = expDate;

        //update time until expiry upon construction; derived from https://mkyong.com/java8/java-8-difference-between-two-localdate-or-localdatetime/
        LocalDateTime currentTime = LocalDateTime.now();
        isExpired = !currentTime.isBefore(expDate);
        this.daysUntilExp = (int) ChronoUnit.DAYS.between(currentTime, expDate);
    }

    @Override
    public String toString() {
        String foodString = "This is a food item.";
        foodString += "\nFood: " + name;
        foodString += "\nNotes: " + notes;
        foodString += "\nPrice: $" + price;
        foodString += "\nWeight: " + weight + "g";
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

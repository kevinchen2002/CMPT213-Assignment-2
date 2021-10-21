package cmpt213.a2.model;

import java.time.LocalDateTime;

/**
 * The Consumable class is the basis for FoodItem and DrinkItem.
 * It has shared fields and methods of the two, as well as implementing Comparable.
 */
public class Consumable implements Comparable {
    protected String name;
    protected String notes;
    protected double price;
    LocalDateTime expDate;
    protected int daysUntilExp;
    protected boolean isExpired;

    public Consumable() {
    }

    /**
     * Getter for the name
     * @return the name
     */
    public String getName() {return name;}

    /**
     * Getter for the expiry date
     * @return the expiry date
     */
    public LocalDateTime getExpDate() {
        return expDate;
    }

    /**
     * Getter for expiration status
     * @return the expiration status
     */
    public boolean isExpired() {return isExpired;}

    /**
     * Getter for the days until expiry
     * @return the days until expiry
     */
    public int getDaysUntilExp() {return daysUntilExp;}

    @Override
    public int compareTo(Object o) {
        return 0;
        //TODO: implement this comparable
    }
}

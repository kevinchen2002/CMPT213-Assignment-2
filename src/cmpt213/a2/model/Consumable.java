package cmpt213.a2.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Consumable {
    protected String name;
    protected String notes;
    protected double price;
    protected LocalDateTime expDate;
    protected int daysUntilExp;
    protected boolean isExpired;

    public Consumable() {
    }

    public String getName() {return name;}

    public LocalDateTime getExpDate() {
        return expDate;
    }

    public boolean isExpired() {return isExpired;}

    public int getDaysUntilExp() {return daysUntilExp;}
}

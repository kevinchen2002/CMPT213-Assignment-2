package cmpt213.a2.model;

import java.time.LocalDateTime;

public class ConsumableFactory {
    public static Consumable getConsumable(boolean isFood, String name, String notes, double price,
                                    double wrightOrVolume, LocalDateTime expDate) {
        if (isFood) {
            return new FoodItem(name, notes, price, wrightOrVolume, expDate);
        } else {
            return new DrinkItem(name, notes, price, wrightOrVolume, expDate);
        }
    }
}

import java.util.ArrayList;

public class MealPacket extends MenuItem {
    private ArrayList<MenuItem> bundledItems;
    private double price;

    public MealPacket(String name) {
        super(name, 0, "Combo");
        this.bundledItems = new ArrayList<>();
        this.price = 0;
    }

    public void addBundledItem(MenuItem item) {
        bundledItems.add(item);
        double totalPrice = 0;
        for (MenuItem bundledItem : bundledItems) {
            totalPrice += bundledItem.getPrice();
        }
        this.price = totalPrice * 0.9;
    }

    @Override
    public double getPrice() {
        return this.price;
    }

    public ArrayList<MenuItem> getBundledItems() {
        return bundledItems;
    }

    @Override
    public String toString() {
        return getName() + " (Combo)";
    }
}

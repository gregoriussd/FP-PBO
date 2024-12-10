import java.util.ArrayList;

public class Order {
    private ArrayList<OrderItem> items;

    public Order() {
        items = new ArrayList<>();
    }

    public ArrayList<OrderItem> getItems() {
        return items;
    }

    public void addItem(MenuItem menuItem, int quantity) {
        for (OrderItem orderItem : items) {
            if (orderItem.getMenuItem().equals(menuItem)) {
                orderItem.setQuantity(orderItem.getQuantity() + quantity);
                return;
            }
        }
        items.add(new OrderItem(menuItem, quantity));
    }

    public void removeItem(MenuItem menuItem) {
        items.removeIf(orderItem -> orderItem.getMenuItem().equals(menuItem));
    }

    public void clearOrder() {
        items.clear();
    }
}
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class App {
    private JFrame frame;
    private JTabbedPane tabbedPane;
    private JTable orderTable;
    private DefaultTableModel tableModel;
    private Order currentOrder;
    private ArrayList<MenuItem> menuItems;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel welcomePanel;
    private JLabel totalPriceLabel;

    public App() {
        currentOrder = new Order();
        menuItems = new ArrayList<>();
        populateMenuItems();
        createGUI();
    }

    private void populateMenuItems() {
        menuItems.add(new MenuItem("Big Mac", 45000, "Food"));
        menuItems.add(new MenuItem("McChicken", 40000, "Food"));
        menuItems.add(new MenuItem("Cheeseburger", 35000, "Food"));
        menuItems.add(new MenuItem("Ayam Goreng McD", 36000, "Food"));
        menuItems.add(new MenuItem("McSpaghetti", 32000, "Food"));

        menuItems.add(new MenuItem("Coca-Cola", 12000, "Drinks"));
        menuItems.add(new MenuItem("Fanta", 12000, "Drinks"));
        menuItems.add(new MenuItem("Iced Lemon Tea", 15000, "Drinks"));
        menuItems.add(new MenuItem("McCafe Coffee", 20000, "Drinks"));

        menuItems.add(new MenuItem("McFlurry", 25000, "Dessert"));
        menuItems.add(new MenuItem("Ice Cream Cone", 8000, "Dessert"));
        menuItems.add(new MenuItem("Apple Pie", 14000, "Dessert"));

        MealPacket combo1 = new MealPacket("PaNas 1");
        combo1.addBundledItem(new MenuItem("Ayam Goreng McD", 36000, "Food"));
        combo1.addBundledItem(new MenuItem("Coca-Cola", 12000, "Drinks"));

        MealPacket combo2 = new MealPacket("McSpaghetti Set");
        combo2.addBundledItem(new MenuItem("McSpaghetti", 32000, "Food"));
        combo2.addBundledItem(new MenuItem("Iced Lemon Tea", 15000, "Drinks"));

        menuItems.add(combo1);
        menuItems.add(combo2);
    }

    private void createGUI() {
        frame = new JFrame("Food Ordering System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
    
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
    
        welcomePanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome to Food Ordering System", SwingConstants.CENTER);
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.PLAIN, 35));
    
        JPanel buttonsPanel = new JPanel();
        JButton takeoutButton = new JButton("Takeout");
        JButton dineinButton = new JButton("Dine In");
        takeoutButton.setPreferredSize(new Dimension(300, 200));
    
        takeoutButton.addActionListener(e -> showOrderPage());
        dineinButton.addActionListener(e -> showOrderPage());
        dineinButton.setPreferredSize(new Dimension(300, 200));
    
        buttonsPanel.add(takeoutButton);
        buttonsPanel.add(dineinButton);
        welcomePanel.add(buttonsPanel, BorderLayout.SOUTH);
    
        JPanel orderPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Item", "Amount", "Price"}, 0);
        orderTable = new JTable(tableModel);
    
        JScrollPane scrollPane = new JScrollPane(orderTable);
    
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton checkoutButton = new JButton("Checkout");
        JButton cancelOrderButton = new JButton("Cancel Order");
    
        cancelOrderButton.addActionListener(e -> {
            currentOrder = new Order();
            updateOrderSummary();
            updateTotalPrice();
        });
    
        checkoutButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Thank you for your order!");
            currentOrder = new Order();
            updateOrderSummary();
            updateTotalPrice();
            cardLayout.show(mainPanel, "Welcome");
        });
    
        totalPriceLabel = new JLabel("Total: Rp. 0");
        totalPriceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        totalPriceLabel.setHorizontalAlignment(SwingConstants.CENTER);
    
        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.add(totalPriceLabel, BorderLayout.CENTER);
    
        JPanel totalButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalButtonPanel.add(checkoutButton);
        totalButtonPanel.add(cancelOrderButton);
    
        totalPanel.add(totalButtonPanel, BorderLayout.SOUTH);
    
        orderPanel.add(scrollPane, BorderLayout.CENTER);
        orderPanel.add(totalPanel, BorderLayout.SOUTH);
    
        tabbedPane = new JTabbedPane();
        addCategoryTab("Food");
        addCategoryTab("Drinks");
        addCategoryTab("Dessert");
        addCategoryTab("Combo");
        addCategoryTab("All");
    
        JPanel orderContainer = new JPanel(new GridLayout(1, 2));
        orderContainer.add(tabbedPane);
        orderContainer.add(orderPanel);
    
        mainPanel.add(welcomePanel, "Welcome");
        mainPanel.add(orderContainer, "Order");
    
        frame.add(mainPanel);
        frame.setVisible(true);
    }
    
    private void updateTotalPrice() {
        int total = 0;
        for (OrderItem orderItem : currentOrder.getItems()) {
            total += orderItem.getMenuItem().getPrice() * orderItem.getQuantity();
        }
        totalPriceLabel.setText("Total: Rp. " + total);
    }

    private void addCategoryTab(String category) {
        JPanel categoryPanel = new JPanel();
        categoryPanel.setLayout(new GridLayout(0, 1));

        Dimension buttonSize = new Dimension(80, 30);
        Dimension labelSize = new Dimension(300, 30);

        for (MenuItem item : menuItems) {
            if (category.equals("All") || item.getCategory().equals(category)) {
                JPanel itemPanel = new JPanel(new BorderLayout());

                JLabel itemLabel = new JLabel(item.toString());
                itemLabel.setPreferredSize(labelSize);

                JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
                quantitySpinner.setPreferredSize(buttonSize);

                JButton addButton = new JButton("Add");
                addButton.setPreferredSize(buttonSize);
                
                addButton.addActionListener(e -> {
                    int quantity = (int) quantitySpinner.getValue();
                    currentOrder.addItem(item, quantity);
                    updateOrderSummary();
                    updateTotalPrice();
                });

                itemPanel.add(itemLabel, BorderLayout.WEST);
                itemPanel.add(quantitySpinner, BorderLayout.CENTER);
                itemPanel.add(addButton, BorderLayout.EAST);
                categoryPanel.add(itemPanel);
            }
        }

        tabbedPane.addTab(category, categoryPanel);
    }


    private void updateOrderSummary() {
        tableModel.setRowCount(0);
        for (OrderItem orderItem : currentOrder.getItems()) {
            tableModel.addRow(new Object[]{
                    orderItem.getMenuItem().getName(),
                    orderItem.getQuantity(),
                    orderItem.getMenuItem().getPrice() * orderItem.getQuantity()
            });
        }
    }

    private void showOrderPage() {
        cardLayout.show(mainPanel, "Order");
    }

    public static void main(String[] args) {
        new App();
    }
}

package gui;

import model.bill;
import model.product;
import service.BillingService;
import service.InventoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;

public class BillingGUI extends JFrame {

    private InventoryService inventoryService = new InventoryService();
    private BillingService billingService = new BillingService(inventoryService);

    private JTable productTable;
    private DefaultTableModel productModel;

    private JTable cartTable;
    private DefaultTableModel cartModel;

    private JTextField customerField;
    private JTextField qtyField;

    private JLabel totalLabel;

    private Map<Integer, Integer> cart = new LinkedHashMap<>();

    public BillingGUI() {

        setTitle("Electronic Shop Billing System");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadSampleData();

        add(buildProductPanel(), BorderLayout.CENTER);
        add(buildCartPanel(), BorderLayout.EAST);

        setVisible(true);
    }

    // ---------------- PRODUCT PANEL ----------------
    private JPanel buildProductPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Products"));

        productModel = new DefaultTableModel(
                new Object[]{"ID", "Name", "Category", "Price", "Qty"}, 0);

        productTable = new JTable(productModel);
        refreshProducts();

        JScrollPane scroll = new JScrollPane(productTable);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();

        JButton addBtn = new JButton("Add to Cart");

        addBtn.addActionListener(e -> addToCart());

        btnPanel.add(addBtn);

        panel.add(btnPanel, BorderLayout.SOUTH);

       

        return panel;
    }

    // ---------------- CART PANEL ----------------
    private JPanel buildCartPanel() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Cart"));

        JPanel top = new JPanel(new GridLayout(2, 2));

        top.add(new JLabel("Customer:"));
        customerField = new JTextField();
        top.add(customerField);

        top.add(new JLabel("Qty:"));
        qtyField = new JTextField("1");
        top.add(qtyField);

        panel.add(top, BorderLayout.NORTH);

        cartModel = new DefaultTableModel(
                new Object[]{"Product", "Qty", "Total"}, 0);

        cartTable = new JTable(cartModel);
        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());

        totalLabel = new JLabel("Total: 0.0");
        bottom.add(totalLabel, BorderLayout.NORTH);

        JButton billBtn = new JButton("Generate Bill");

        billBtn.addActionListener(e -> generateBill());

        bottom.add(billBtn, BorderLayout.SOUTH);

        panel.add(bottom, BorderLayout.SOUTH);

        

        return panel;
    }

    // ---------------- ADD TO CART ----------------
    private void addToCart() {

        int row = productTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product");
            return;
        }

        int id = (int) productModel.getValueAt(row, 0);
        product p = inventoryService.getProductById(id);

        int qty;

        try {
            qty = Integer.parseInt(qtyField.getText());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
            return;
        }

        int existing = cart.getOrDefault(id, 0);

        if (existing + qty > p.getQuantity()) {
            JOptionPane.showMessageDialog(this,
                    "Not enough stock. Available: " + p.getQuantity());
            return;
        }

        cart.put(id, existing + qty);

        refreshCart();
    }

    // ---------------- GENERATE BILL ----------------
    private void generateBill() {

        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty");
            return;
        }

        String name = customerField.getText();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter customer name");
            return;
        }

        bill bill = billingService.createBill(name, cart);

        JTextArea area = new JTextArea(bill.getReceiptText());
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JOptionPane.showMessageDialog(this, new JScrollPane(area));

        cart.clear();
        refreshCart();
        refreshProducts();
    }

    // ---------------- REFRESH CART ----------------
    private void refreshCart() {

        cartModel.setRowCount(0);

        double total = 0;

        for (Map.Entry<Integer, Integer> e : cart.entrySet()) {

            product p = inventoryService.getProductById(e.getKey());

            int qty = e.getValue();

            double line = p.getPrice() * qty;

            total += line;

            cartModel.addRow(new Object[]{
                    p.getName(), qty, line
            });
        }

        totalLabel.setText("Total: " + total);
    }

    // ---------------- REFRESH PRODUCTS ----------------
    private void refreshProducts() {

        productModel.setRowCount(0);

        for (product p : inventoryService.getAllProducts()) {

            productModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    p.getQuantity()
            });
        }
    }

    // ---------------- SAMPLE DATA ----------------
    private void loadSampleData() {

    inventoryService.addProduct(new product(1, "TV", "Electronics", 20000, 10));
    inventoryService.addProduct(new product(2, "Phone", "Mobile", 15000, 15));
    inventoryService.addProduct(new product(3, "Laptop", "Computer", 50000, 5));
    inventoryService.addProduct(new product(4, "Fan", "Home", 2000, 20));
    inventoryService.addProduct(new product(5, "AC", "Electronics", 35000, 8));
    inventoryService.addProduct(new product(6, "Tablet", "Mobile", 12000, 12));
    inventoryService.addProduct(new product(7, "Mouse", "Accessories", 500, 50));
    inventoryService.addProduct(new product(8, "Keyboard", "Accessories", 800, 40));
    inventoryService.addProduct(new product(9, "Headphones", "Audio", 1500, 30));
    inventoryService.addProduct(new product(10, "Speaker", "Audio", 3000, 25));
}
    

    public static void main(String[] args) {
        new BillingGUI();
    }

private void searchProduct() {

    String input = JOptionPane.showInputDialog(this, "Enter Product Name:");

    if (input == null || input.trim().isEmpty()) return;

    productModel.setRowCount(0);

    boolean found = false;

    for (product p : inventoryService.getAllProducts()) {

        if (p.getName().toLowerCase().contains(input.toLowerCase())) {

            productModel.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getCategory(),
                    p.getPrice(),
                    p.getQuantity()
            });

            found = true;
        }
    }

    if (!found) {
        JOptionPane.showMessageDialog(this, "No product found!");
        refreshProducts();
    }
}
}
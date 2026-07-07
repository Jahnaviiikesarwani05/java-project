package service;

import model.bill;
import model.product;

import java.util.Map;

public class BillingService {

    private InventoryService inventoryService;

    public BillingService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public bill createBill(String customerName, Map<Integer, Integer> cart) {

        double subtotal = 0;
        StringBuilder receipt = new StringBuilder();

        receipt.append("===== ELECTRONIC SHOP BILL =====\n");
        receipt.append("Customer: ").append(customerName).append("\n\n");
        receipt.append("---------------------------------\n");

        for (Map.Entry<Integer, Integer> entry : cart.entrySet()) {

            int productId = entry.getKey();
            int qty = entry.getValue();

            product p = inventoryService.getProductById(productId);

            if (p == null) continue;

            double lineTotal = p.getPrice() * qty;
            subtotal += lineTotal;

            receipt.append(p.getName())
                    .append(" x ").append(qty)
                    .append(" = ").append(String.format("%.2f", lineTotal))
                    .append("\n");

            // reduce stock
            inventoryService.reduceStock(productId, qty);
        }

        double tax = subtotal * 0.05;
        double total = subtotal + tax;

        receipt.append("---------------------------------\n");
        receipt.append("Subtotal: ").append(String.format("%.2f", subtotal)).append("\n");
        receipt.append("Tax (5%): ").append(String.format("%.2f", tax)).append("\n");
        receipt.append("TOTAL: ").append(String.format("%.2f", total)).append("\n");
        receipt.append("=================================\n");

        return new bill(customerName, subtotal, tax, total, receipt.toString());
    }
}
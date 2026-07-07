package service;

import model.product;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class InventoryService {

    private Map<Integer, product> products = new LinkedHashMap<>();

    // Add Product
    public void addProduct(product product) {
        products.put(product.getId(), product);
    }

    // Check if product exists
    public boolean productExists(int id) {
        return products.containsKey(id);
    }

    // Get Product by ID
    public product getProductById(int id) {
        return products.get(id);
    }

    // Get All Products
    public Collection<product> getAllProducts() {
        return products.values();
    }

    // Update Product
    public void updateProduct(int id, String name, String category, double price, int quantity) {
        product p = products.get(id);

        if (p != null) {
            p.setName(name);
            p.setCategory(category);
            p.setPrice(price);
            p.setQuantity(quantity);
        }
    }

    // Delete Product
    public void deleteProduct(int id) {
        products.remove(id);
    }

    // Reduce Stock
    public void reduceStock(int id, int qty) {
        product p = products.get(id);

        if (p != null) {
            p.setQuantity(p.getQuantity() - qty);
        }
    }
}
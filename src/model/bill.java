package model;

import java.util.Date;

public class bill {

    private String customerName;
    private double subtotal;
    private double tax;
    private double total;
    private String receiptText;
    private Date billDate;

    public bill(String customerName, double subtotal, double tax, double total, String receiptText) {
        this.customerName = customerName;
        this.subtotal = subtotal;
        this.tax = tax;
        this.total = total;
        this.receiptText = receiptText;
        this.billDate = new Date();
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getTax() {
        return tax;
    }

    public double getTotal() {
        return total;
    }

    public String getReceiptText() {
        return receiptText;
    }

    public Date getBillDate() {
        return billDate;
    }

    @Override
    public String toString() {
        return receiptText;
    }
}
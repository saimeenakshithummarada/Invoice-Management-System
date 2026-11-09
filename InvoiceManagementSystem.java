import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class InvoiceManagementSystem extends JFrame {

    JTextField customerName, phoneField;
    JTextField itemName, quantity, price, discount;

    JLabel subtotalLabel, discountLabel, totalLabel;

    DefaultTableModel tableModel;
    JTable table;

    public InvoiceManagementSystem() {

        setTitle("Invoice Management System");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Heading
        JLabel heading = new JLabel(
            "INVOICE MANAGEMENT SYSTEM",
            JLabel.CENTER
        );

        heading.setFont(new Font("Arial", Font.BOLD, 30));
        heading.setForeground(new Color(20, 70, 140));

        main.add(heading, BorderLayout.NORTH);

        // Customer details
        JPanel customerPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        customerPanel.setBorder(
            BorderFactory.createTitledBorder("Customer Details")
        );

        customerName = new JTextField();
        phoneField = new JTextField();

        customerPanel.add(new JLabel("Customer Name:"));
        customerPanel.add(customerName);

        customerPanel.add(new JLabel("Phone Number:"));
        customerPanel.add(phoneField);

        // Product details
        JPanel productPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        productPanel.setBorder(
            BorderFactory.createTitledBorder("Add Items")
        );

        itemName = new JTextField();
        quantity = new JTextField();
        price = new JTextField();

        JButton addButton = new JButton("Add Item");

        productPanel.add(new JLabel("Item Name:"));
        productPanel.add(itemName);

        productPanel.add(new JLabel("Quantity:"));
        productPanel.add(quantity);

        productPanel.add(new JLabel("Price:"));
        productPanel.add(price);

        productPanel.add(new JLabel(""));
        productPanel.add(addButton);

        // Table
        String[] columns = {
            "Item Name", "Quantity", "Price", "Amount"
        };

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setRowHeight(28);
        table.setFont(new Font("Arial", Font.PLAIN, 15));
        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 15)
        );

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(
            BorderFactory.createTitledBorder("Invoice Items")
        );

        // Center panel
        JPanel center = new JPanel(new BorderLayout(10, 10));

        JPanel top = new JPanel(new BorderLayout(10, 10));
        top.add(customerPanel, BorderLayout.NORTH);
        top.add(productPanel, BorderLayout.CENTER);

        center.add(top, BorderLayout.NORTH);
        center.add(tableScroll, BorderLayout.CENTER);

        // Summary panel
        JPanel summary = new JPanel(new GridLayout(4, 2, 10, 10));

        summary.setBorder(
            BorderFactory.createTitledBorder("Invoice Summary")
        );

        discount = new JTextField("0");

        subtotalLabel = new JLabel("Rs. 0.00");
        discountLabel = new JLabel("Rs. 0.00");
        totalLabel = new JLabel("Rs. 0.00");

        summary.add(new JLabel("Subtotal:"));
        summary.add(subtotalLabel);

        summary.add(new JLabel("Discount (%):"));
        summary.add(discount);

        summary.add(new JLabel("Discount Amount:"));
        summary.add(discountLabel);

        summary.add(new JLabel("Grand Total:"));
        summary.add(totalLabel);

        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(new Color(0, 130, 60));

        // Buttons
        JButton generateButton = new JButton("Generate Invoice");
        JButton historyButton = new JButton("Invoice History");
        JButton removeButton = new JButton("Remove Selected Item");
        JButton clearButton = new JButton("Clear");

        JPanel buttons = new JPanel(new FlowLayout());

        buttons.add(removeButton);
        buttons.add(generateButton);
        buttons.add(historyButton);
        buttons.add(clearButton);

        JPanel bottom = new JPanel(new BorderLayout(10, 10));
        bottom.add(summary, BorderLayout.CENTER);
        bottom.add(buttons, BorderLayout.SOUTH);

        center.add(bottom, BorderLayout.SOUTH);

        main.add(center, BorderLayout.CENTER);

        add(main);

        // Button actions
        addButton.addActionListener(e -> addItem());

        generateButton.addActionListener(e -> generateInvoice());

        historyButton.addActionListener(e -> showHistory());

        removeButton.addActionListener(e -> removeItem());

        clearButton.addActionListener(e -> clearForm());

        discount.addActionListener(e -> calculateTotal());
    }

    // Add item to table
    private void addItem() {

        try {

            String name = itemName.getText().trim();

            int qty = Integer.parseInt(quantity.getText().trim());
            double itemPrice = Double.parseDouble(price.getText().trim());

            if (name.isEmpty() || qty <= 0 || itemPrice < 0) {
                JOptionPane.showMessageDialog(
                    this, "Enter valid item details."
                );
                return;
            }

            double amount = qty * itemPrice;

            tableModel.addRow(new Object[] {
                name, qty, itemPrice, amount
            });

            itemName.setText("");
            quantity.setText("");
            price.setText("");

            calculateTotal();

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Enter valid quantity and price."
            );
        }
    }

    // Calculate subtotal and discount
    private void calculateTotal() {

        double subtotal = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {

            subtotal += Double.parseDouble(
                tableModel.getValueAt(i, 3).toString()
            );
        }

        try {

            double discountPercent =
                Double.parseDouble(discount.getText().trim());

            if (discountPercent < 0 || discountPercent > 100) {
                throw new NumberFormatException();
            }

            double discountAmount =
                subtotal * discountPercent / 100;

            double total = subtotal - discountAmount;

            subtotalLabel.setText(
                String.format("Rs. %.2f", subtotal)
            );

            discountLabel.setText(
                String.format("Rs. %.2f", discountAmount)
            );

            totalLabel.setText(
                String.format("Rs. %.2f", total)
            );

        } catch (NumberFormatException e) {

            subtotalLabel.setText(
                String.format("Rs. %.2f", subtotal)
            );

            discountLabel.setText("Invalid discount");
            totalLabel.setText("Invalid discount");
        }
    }

    // Generate and save invoice
    private void generateInvoice() {

        String customer = customerName.getText().trim();
        String phone = phoneField.getText().trim();

        if (customer.isEmpty()) {
            JOptionPane.showMessageDialog(
                this, "Please enter customer name."
            );
            return;
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this, "Please add at least one item."
            );
            return;
        }

        try {

            double subtotal = 0;

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                subtotal += Double.parseDouble(
                    tableModel.getValueAt(i, 3).toString()
                );
            }

            double discountPercent =
                Double.parseDouble(discount.getText().trim());

            if (discountPercent < 0 || discountPercent > 100) {
                throw new NumberFormatException();
            }

            double discountAmount =
                subtotal * discountPercent / 100;

            double total = subtotal - discountAmount;

            // Save to SQLite
            int invoiceId = Database.saveInvoice(
                customer,
                phone,
                subtotal,
                discountPercent,
                discountAmount,
                total,
                tableModel
            );

            // Display invoice
            showGeneratedInvoice(
                invoiceId,
                customer,
                phone,
                subtotal,
                discountPercent,
                discountAmount,
                total
            );

            JOptionPane.showMessageDialog(
                this,
                "Invoice saved successfully!"
            );

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                this, "Enter a valid discount between 0 and 100."
            );

        } catch (Exception e) {

            JOptionPane.showMessageDialog(
                this,
                "Error saving invoice: " + e.getMessage()
            );
        }
    }

    // Display generated invoice in a new window
    private void showGeneratedInvoice(
        int invoiceId,
        String customer,
        String phone,
        double subtotal,
        double discountPercent,
        double discountAmount,
        double total) {

        JFrame invoiceFrame = new JFrame("Customer Invoice");

        invoiceFrame.setSize(750, 600);
        invoiceFrame.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );

        JLabel title = new JLabel(
            "CUSTOMER INVOICE",
            JLabel.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(20, 70, 140));

        JPanel details = new JPanel(new GridLayout(3, 2, 10, 10));

        details.add(new JLabel("Invoice Number: " + invoiceId));
        details.add(new JLabel("Customer: " + customer));

        details.add(new JLabel("Phone: " + phone));
        details.add(new JLabel("Date: " +
            java.time.LocalDate.now()));

        details.add(new JLabel("Sai Meenakshi Collections"));
        details.add(new JLabel("Thank you for shopping!"));

        JPanel top = new JPanel(new BorderLayout(10, 10));

        top.add(title, BorderLayout.NORTH);
        top.add(details, BorderLayout.CENTER);

        JTable invoiceTable = new JTable(tableModel);
        invoiceTable.setRowHeight(28);

        JScrollPane scroll = new JScrollPane(invoiceTable);

        JPanel summary = new JPanel(new GridLayout(4, 2, 10, 10));

        summary.add(new JLabel("Subtotal:"));
        summary.add(new JLabel(String.format("Rs. %.2f", subtotal)));

        summary.add(new JLabel("Discount:"));
        summary.add(new JLabel(discountPercent + "%"));

        summary.add(new JLabel("Discount Amount:"));
        summary.add(new JLabel(
            String.format("Rs. %.2f", discountAmount)
        ));

        JLabel grandTotal = new JLabel(
            String.format("Rs. %.2f", total)
        );

        grandTotal.setFont(new Font("Arial", Font.BOLD, 20));

        summary.add(new JLabel("GRAND TOTAL:"));
        summary.add(grandTotal);

        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(summary, BorderLayout.SOUTH);

        invoiceFrame.add(panel);
        invoiceFrame.setVisible(true);
    }

    // Display previous invoice history
    private void showHistory() {

        String history = Database.getInvoiceHistory();

        JTextArea area = new JTextArea(history);

        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(area);

        scroll.setPreferredSize(new Dimension(650, 450));

        JOptionPane.showMessageDialog(
            this,
            scroll,
            "Previous Invoice History",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    // Remove selected item
    private void removeItem() {

        int selectedRow = table.getSelectedRow();

        if (selectedRow >= 0) {

            tableModel.removeRow(selectedRow);
            calculateTotal();

        } else {

            JOptionPane.showMessageDialog(
                this, "Please select an item to remove."
            );
        }
    }

    // Clear current form
    private void clearForm() {

        customerName.setText("");
        phoneField.setText("");

        itemName.setText("");
        quantity.setText("");
        price.setText("");

        discount.setText("0");

        tableModel.setRowCount(0);

        calculateTotal();
    }

    public static void main(String[] args) {

        // Initialize database
        Database.initialize();

        SwingUtilities.invokeLater(() -> {
            new InvoiceManagementSystem().setVisible(true);
        });
    }
}
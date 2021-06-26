package ims.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {

    private int id;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;
    private ObservableList<Part> associatedParts;

    /**
     * --Product Constructor--
     * @param id the id to set
     * @param name the name to set
     * @param price the price to set
     * @param stock the stock level to set
     * @param min  the min to set
     * @param max the max to set
     * @return the newly created product
     */
    public Product (int id, String name, double price, int stock, int min, int max)
    {
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * @param id the id to set
     */
    public void setID (int id) {
        this.id = id;
    }
    /**
     * @param name the name to set
     */
    public void setName (String name) { this.name = name; }
    /**
     * @param price the price to set
     */
    public void setPrice (double price) { this.price = price; }
    /**
     * @param stock the stock level to set
     */
    public void setStock (int stock) {
        this.stock = stock;
    }
    /**
     * @param min the min to set
     */
    public void setMin (int min) {
        this.min = min;
    }
    /**
     * @param max the max to set
     */
    public void setMax (int max) {
        this.max = max;
    }

    /**
     * @return the id
     */
    public int getId () {
        return id;
    }
    /**
     * @return the name
     */
    public String getName () {
        return name;
    }
    /**
     * @return the price
     */
    public double getPrice () {
        return price;
    }
    /**
     * @return the stock level
     */
    public int getStock () {
        return stock;
    }
    /**
     * @return the minimum stock level
     */
    public int getMin () {
        return min;
    }
    /**
     * @return the maximum stock level
     */
    public int getMax () {
        return max;
    }


    /**
     * Adds a part to the associated parts array.
     * @param part The part to add.
     */
    public void addAssociatedPart(Part part) { associatedParts.add(part); }

    /**
     * Delete associated part. If operation is successful, return true, else false.
     * @param part The part to delete
     * @return Will return true if deletion successful, otherwise false.
     */
    public boolean deleteAssociatedPart (Part part) {
        if(associatedParts.contains(part))
        {
            associatedParts.remove(part);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get all associated parts.
     * @return An observable list of Parts
     */
    public ObservableList<Part> getAllAssociatedParts() { return associatedParts; }
}

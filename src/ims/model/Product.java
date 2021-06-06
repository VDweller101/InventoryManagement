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

    /*
    ////    Constructor
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

    /*
    ////    Setters
     */
    public void setID (int id) {
        this.id = id;
    }
    public void setName (String name) { this.name = name; }
    public void setPrice (double price) { this.price = price; }
    public void setStock (int stock) {
        this.stock = stock;
    }
    public void setMin (int min) {
        this.min = min;
    }
    public void setMax (int max) {
        this.max = max;
    }

    /*
    ////    Getters
    */
    public int getId () {
        return id;
    }
    public String getName () {
        return name;
    }
    public double getPrice () {
        return price;
    }
    public int getStock () {
        return stock;
    }
    public int getMin () {
        return min;
    }
    public int getMax () {
        return max;
    }


    /*
    ////    Part Management
    */

    //Add part to associatedParts list of product.
    public void addAssociatedPart(Part part) { associatedParts.add(part); }

    //Delete associated part from products part list. Check if the part is within the associatedParts list. Return true if deletion successful.
    public boolean deleteAssociatedPart (Part part) {
        if(associatedParts.contains(part))
        {
            associatedParts.remove(part);
            return true;
        } else {
            return false;
        }
    }

    //Return list of parts associated with product.
    public ObservableList<Part> getAllAssociatedParts() { return associatedParts; }
}

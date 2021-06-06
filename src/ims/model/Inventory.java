package ims.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory
{
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    public static void addPart (Part part)
    {
        allParts.add(part);
    }

    public static void addProduct(Product product)
    {
        allProducts.add(product);
    }

    /*
    @return Looks through allParts for the partID and returns any matches, otherwise returns null.
     */
    public static Part lookupPart (int partId)
    {
        for (Part part: allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /*
    @return Looks through allProducts for the productID and returns any matches, otherwise returns null.
    */
    public Product lookupProduct(int productId)
    {
        for (Product product:allProducts
             ) {
            if (productId == product.getId()) { return product; }
        }
        return null;
    }

    /*
    @return Looks through allParts for the partName and returns any matches, otherwise returns null.
     */
    public static ObservableList<Part> lookupPart (String partName)
    {
        if (partName.isBlank()) {
            return allParts;
        } else {
            ObservableList<Part> matchingParts = FXCollections.observableArrayList();

            int count = partName.length();
            for (Part part : allParts
            ) {

                if (partName.length() <= part.getName().length()) {
                    String subName = part.getName().substring(0, count);
                    if (subName.equals(partName)) {
                        matchingParts.add(part);
                    }
                }
            }
            return matchingParts;
        }
    }

    /*
    @return Looks through allProducts for the productName and returns any matches, otherwise returns null.
    */
    public static ObservableList<Product> lookupProduct(String productName)
    {
        if (productName.isBlank()) { return allProducts; }
        else {
            ObservableList<Product> matchingProducts = FXCollections.observableArrayList();

            int count = productName.length();
            for (Product product:allProducts
                 ) {
                if (productName.length() <= product.getName().length()) {
                    String subName = product.getName().substring(0, count);
                    if(subName.equals(productName)) {
                        matchingProducts.add(product);
                    }
                }
            }
            return matchingProducts;
        }
    }

    // Deletes the old part and adds the new one. Will display message to console if unsuccessful
    public static void updatePart (int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    // Deletes the old product and adds the new one. Will display message to console if unsuccessful
    public static void updateProduct (int index, Product newProduct)
    {
        allProducts.set(index, newProduct);
    }

    /*
    @return Attempts to find and delete a part. Returns boolean if successful (true) or not (false)
     */
    public static boolean deletePart(Part selectedPart)
    {
        if (allParts.contains(selectedPart))
        {
            allParts.remove(selectedPart);
            System.out.println("Deleting part. Current part count: " + allParts.size());
            return true;
        } else {
            System.out.println("Tried to delete non-existant part: " + selectedPart.getName());
            return false;
        }
    }

    /*
    @return Attempts to find and delete a product. Returns boolean if successful (true) or not (false)
     */
    public boolean deleteProduct (Product selectedProduct)
    {
        if (allProducts.contains(selectedProduct))
        {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            System.out.println("Tried to delete non-existant Product: " + selectedProduct.getName());
            return false;
        }
    }

    /*
    @return Returns the ObservableList of allParts.
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /*
    @return Returns the ObservableList of allProducts.
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }
}

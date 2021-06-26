package ims.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory
{
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Adds a part to the array of all parts.
     * @param part The part to add.
     */
    public static void addPart (Part part)
    {
        allParts.add(part);
    }

    /**
     * Adds a product to the array of all products.
     * @param product The product to add.
     */
    public static void addProduct(Product product)
    {
        allProducts.add(product);
    }

    /**
     * Looks through allParts for the partID and returns any matches, otherwise returns null.
     * @param partId The part ID to search for.
     * @return Found part matching ID param, otherwise null.
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

    /**
     * Looks through allProducts for the productID and returns any matches, otherwise returns null.
     * @param productId The product ID to search for.
     * @return Found product matching ID param, otherwise null.
     */
    public static Product lookupProduct(int productId)
    {
        for (Product product:allProducts
             ) {
            if (productId == product.getId()) { return product; }
        }
        return null;
    }

    /**
     * Looks through allParts for the partName and returns any matches, otherwise returns null.
     * @param partName The part name to search for.
     * @return The part(s) that match the search string. Will return empty list if no matches found.
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

    /**
     * Looks through allProducts for the productName and returns any matches, otherwise returns empty array.
     * @param productName The product name to search for.
     * @return The Product(s) that match the search string given. If no matches are found, will return an empty list.
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

    /**
     * Deletes the old part and adds the new one.
     * @param index The index value of the part to be updated.
     * @param selectedPart The updated part that will replace the part at index given.
     */
    public static void updatePart (int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /**
     * Deletes the old product and adds the new one.
     * @param index The index value of the product to be updated.
     * @param newProduct The updated product that will replace the product at index given.
     */
    public static void updateProduct (int index, Product newProduct)
    {
        allProducts.set(index, newProduct);
    }

    /**
     * Attempts to find and delete a part. Returns boolean if successful (true) or not (false)
     * @param selectedPart The part to delete
     * @return Returns true if deletion successful, else false.
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

    /**
     * Attempts to find and delete a product. Returns boolean if successful (true) or not (false)
     * @param selectedProduct The product to delete
     * @return Returns true if deletion successful, else false.
     */
    public static boolean deleteProduct (Product selectedProduct)
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

    /**
     * Get list of all parts in inventory.
     * @return List of all parts.
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     * Get list of all products in inventory.
     * @return List of all products.
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }
}

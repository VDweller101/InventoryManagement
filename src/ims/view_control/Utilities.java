package ims.view_control;

import ims.model.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class Utilities
{

    //Used to pass part or product variables between controllers.
    public static Part CurrentSelectedPart = null;
    public static Product CurrentSelectedProduct = null;

    //Opens confirmation window checking if the user actually wants to Exit the application and exits if they click OK
    public static void ExitApplication(ActionEvent event)
    {
        if (DisplayPrompt("Confirm Exit", "Quit the application? Warning: All unsaved changes will be lost.") == true) {
            System.exit(0);
        }
    }

    public static Boolean DisplayPrompt(String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText(header);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
    /*
    @return Returns a string with an error message if any of the fields of a Part are not valid, or a blank string if the Part is valid.
     */
    public static String IsPartValid (Part part)
    {
        int stock = part.getStock();
        String errorMessage = "";
        if(part.getId() < 0) { errorMessage += "Part ID must be a positive number.\n"; }
        if (part.getName().isBlank()) { errorMessage += "Part name must not be blank.\n"; }
        if (stock < 0) { errorMessage += "Part Stock level must be greater 0.\n"; }
        if (stock > part.getMax() || stock < part.getMin()) { errorMessage += "Part stock level must be between minimum and maximum.\n";}
        if (part.getPrice() < 0.0) { errorMessage += "Part cost must be greater than 0.\n"; }
        if (part.getClass() == InHouse.class) {
            if (((InHouse)part).getMachineId() < 0) { errorMessage += "Part machine ID must be greater than 0.\n";}
        } else {
            if (((Outsourced)part).getCompanyName().isBlank()) { errorMessage += "Outsourced part company name can not be blank.\n";}
        }
        return errorMessage;
    }

    /*
    @return Returns a string with an error message if any of the fields of a Product are not valid, or a blank string if the Part is valid.
     */
    public static String IsProductValid (Product product)
    {
        int stock = product.getStock();
        String errorMessage = "";
        if(product.getId() < 0) { errorMessage += "Product ID must be a positive number.\n"; }
        if (product.getName() == "") { errorMessage += "Product name must not be blank.\n"; }
        if (stock < 0) { errorMessage += "Product Stock level must be greater 0.\n"; }
        if (stock < product.getMin()) { errorMessage += "Product Stock level must be greater than Inventory Minimum.\n"; }
        if (stock > product.getMax()) { errorMessage += "Product Stock level must be less than Inventory Maximum.\n"; }
        if (product.getPrice() < 0.0) { errorMessage += "Product cost must be greater than 0.\n"; }
        if (product.getAllAssociatedParts().size() < 1) { errorMessage += "Product must have at least 1 part.\n"; }
        return errorMessage;
    }

    public static Integer TryParseInt (String val)
    {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Double TryParseDouble (String val)
    {
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @FXML
    public static void DisplayErrorMessage (String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText(header);
        Optional<ButtonType> result = alert.showAndWait();
    }

    public static boolean DoesPartNameExist (String name)
    {
        if (Inventory.lookupPart(name).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean DoesPartIDExist (int id)
    {
        if (Inventory.lookupPart(id) == null) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean DoesProductNameExist (String name)
    {
        if (Inventory.lookupProduct(name).isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean DoesProductIDExist (int id)
    {
        if (Inventory.lookupProduct(id) == null) {
            return false;
        } else {
            return true;
        }
    }
    public static boolean IsPartInAnyProducts(Part part)
    {
        ObservableList<Product> products = Inventory.getAllProducts();
        for (Product product:products
             ) {
            if (product.getAllAssociatedParts().contains(part)) {
                return true;
            }
        }
        return false;
    }
    public static String ArePartFieldsValid (Boolean type, String id, String name, String price, String stock, String min, String max, String machineCompany)
    {
        String message = "";

        //int id, String name, double price, int stock, int min, int max, String companyName
        Integer idVal = TryParseInt(id);
        if (idVal == null) { message += "ID must be a valid Integer.\n"; }
        else {
            if (DoesPartIDExist(idVal)) { message += "Part ID already exists.\n"; }
        }

        if (name.isBlank()) { message += "Name must not be blank.\n"; }

        if (DoesPartNameExist(name)) { message += "Part name already exists.\n"; }

        Double priceVal = TryParseDouble(price);
        if (priceVal == null) { message += "Price must be a valid decimal number.\n"; }

        Integer stockVal = TryParseInt(stock);
        if (stockVal == null) { message += "Stock level must be a valid Integer.\n"; }

        Integer minVal = TryParseInt(min);
        if (minVal == null) { message += "Minimum Stock level must  be a valid integer.\n"; }
        Integer maxVal = TryParseInt(max);
        if (maxVal == null) { message += "Maximum Stock level must be a valid Integer.\n"; }

        if (type) {
            Integer machineVal = TryParseInt(machineCompany);
            if (machineVal == null) { message += "MachineID must be a valid Integer.\n"; }
        }
        else {
            if (machineCompany.isBlank()) { message += "Company Name must not be blank.\n"; }
        }
        if (minVal != null && maxVal != null && stockVal != null) {
            if (stockVal < minVal) { message += "Stock level must be higher than minimum stock level.\n"; }
            if (stockVal > maxVal) { message += "Stock level must be lower than maximum stock level.\n"; }
        }

        return message;
    }

    public static String AreProductFieldsValid(String idString, String nameString, String priceString, String stockString, String minString, String maxString, int partCount)
    {
        String message = "";
        //int id, String name, double price, int stock, int min, int max
        Integer id = Utilities.TryParseInt(idString);
        if (id == null) { message += "Product ID must be a valid integer and can not be blank.\n"; }
        if (DoesProductIDExist(id)) { message += "Product ID already exists.\n"; }
        String name = nameString;
        if (DoesProductNameExist(name)) { message += "Product name already exists.\n"; }
        if (nameString.isBlank()) { message += "Product must have a valid Name.\n"; }
        Double price = Utilities.TryParseDouble(priceString);
        if (price == null) { message += "Price must be a valid decimal number and can not be blank.\n"; }
        Integer stock = Utilities.TryParseInt(stockString);
        if (stock == null) { message += "Product stock level must be a valid integer value and must not be blank.\n"; }
        Integer min = Utilities.TryParseInt(minString);
        if (min == null) { message += "Inventory minimum level must be a valid integer value and must not be blank.\n"; }
        Integer max = Utilities.TryParseInt(maxString);
        if (max == null) { message += "Inventory maximum level must be a valid integer value and must not be blank.\n"; }
        if (partCount < 1) { message += "Product must have at east 1 part.\n"; }

        return message;
    }

    public static ObservableList<Product> GetAllProductsContainingPart (Part part) {
        ObservableList<Product> products = FXCollections.observableArrayList();
        for (Product pro : Inventory.getAllProducts()
        ) {
            if (pro.getAllAssociatedParts().contains(part)) { products.add(pro); }
        }
        return products;
    }
}

package ims.view_control;

import ims.model.Inventory;
import ims.model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AddProductController {


    @FXML
    private TextField addProductIDTextField;

    @FXML
    private TextField addProductNameTextField;

    @FXML
    private TextField addProductInventoryTextField;

    @FXML
    private TextField addProductPriceTextField;

    @FXML
    private TextField addProductMinTextField;

    @FXML
    private TextField addProductMaxTextField;

    @FXML
    private Button addProductSaveButton;

    @FXML
    private Button addProductCancelButton;

    @FXML
    private Button addProductExitButton;

    @FXML
    private TextField addProductAddPartTextField;

    @FXML
    private TableView<?> addProductAddPartTableView;

    @FXML
    private TableColumn<?, ?> addProductAddPartIDColumn;

    @FXML
    private TableColumn<?, ?> addProductAddPartNameColumn;

    @FXML
    private TableColumn<?, ?> addProductAddPartInventoryColumn;

    @FXML
    private TableColumn<?, ?> addProductAddPartPriceColumn;

    @FXML
    private Button addProductAddPartAddButton;

    @FXML
    private TableView<?> addProductAssociatedPartTableView;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartIDColumn;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartNameColumn;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartInventoryColumn;

    @FXML
    private TableColumn<?, ?> addProductAssociatedPartPriceColumn;

    @FXML
    private Button addProductAssociatedPartRemoveButton;

    @FXML
    private void addProductSaveButton (ActionEvent event)
    {
        Product product = createProductFromFields();
        if (product != null) {
            Inventory.addProduct(product);
            System.out.println("Created new product with name: " + product.getName());
            returnToMainMenu(event);
        }
    }

    @FXML
    private void addProductCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All unsaved changes will be lost.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Cancel new product creation?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            returnToMainMenu(event);
        }
    }

    @FXML
    private void addProductExitButton(ActionEvent event)
    {
        Utilities.ExitApplication(event);
    }

    private void returnToMainMenu(ActionEvent event)
    {
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setTitle("Inventory");
        stage.setScene(new Scene(root, 800, 400));
        stage.show();
    }

    private Product createProductFromFields ()
    {
        String message = "";
        //int id, String name, double price, int stock, int min, int max
        Integer id = Utilities.TryParseInt(addProductIDTextField.getText());
        if (id == null) { message += "Product ID must be a valid integer and can not be blank.\n"; }
        String name = addProductNameTextField.getText();
        Double price = Utilities.TryParseDouble(addProductPriceTextField.getText());
        if (price == null) { message += "Price must be a valid decimal number and can not be blank.\n"; }
        Integer stock = Utilities.TryParseInt(addProductInventoryTextField.getText());
        if (stock == null) { message += "Product stock level must be a valid integer value and must not be blank.\n"; }
        Integer min = Utilities.TryParseInt(addProductMinTextField.getText());
        if (min == null) { message += "Inventory minimum level must be a valid integer value and must not be blank.\n"; }
        Integer max = Utilities.TryParseInt(addProductMaxTextField.getText());
        if (max == null) { message += "Inventory maximum level must be a valid integer value and must not be blank.\n"; }

        if (message.isBlank()) {
            Product product = new Product (id, name, price, stock, min, max);
            String errorMessage = Utilities.IsProductValid(product);
            if (errorMessage.isBlank()) {
                return product;
            } else {
                Utilities.DisplayErrorMessage("Product Creation Failure", errorMessage);
            }
        } else {
            Utilities.DisplayErrorMessage("Product Creation Failure", message);
        }
        return null;
    }
}

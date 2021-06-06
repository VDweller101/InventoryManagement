package ims.view_control;

import ims.model.Inventory;
import ims.model.Part;
import ims.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableView<Part> addProductAddPartTableView;

    @FXML
    private TableColumn<Part, Integer> addProductAddPartIDColumn;

    @FXML
    private TableColumn<Part, String> addProductAddPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> addProductAddPartInventoryColumn;

    @FXML
    private TableColumn<Part, Double> addProductAddPartPriceColumn;

    @FXML
    private Button addProductAddPartAddButton;

    @FXML
    private TableView<Part> addProductAssociatedPartTableView;

    @FXML
    private TableColumn<Part, Integer> addProductAssociatedPartIDColumn;

    @FXML
    private TableColumn<Part, String> addProductAssociatedPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> addProductAssociatedPartInventoryColumn;

    @FXML
    private TableColumn<Part, Double> addProductAssociatedPartPriceColumn;

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

    @FXML
    private void setAddProductAddPartAddButton (ActionEvent event)
    {
        Part part = addProductAddPartTableView.getSelectionModel().getSelectedItem();
        if (part != null) {
            associatedParts.add(part);
            nonAssociatedParts.remove(part);
            updateIncludedParts();
            updateNonIncludedPartsTableView();
        } else {
            Utilities.DisplayErrorMessage("Select Part", "Please select a part to add.");
        }
    }

    private Product currentProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> nonAssociatedParts = FXCollections.observableArrayList();


    public void initialize()
    {
        //Creating dataset for add part and associated part table views, then initialize tableviews.
        for (Part part : Inventory.getAllParts()) { nonAssociatedParts.add(part);}
        associatedParts.clear();
        initializeTables();
        addProductAddPartTextField.textProperty().addListener(observable ->
                updateNonIncludedPartsTableView(addProductAddPartTextField.getText(), Inventory.lookupPart(addProductAddPartTextField.getText())));
    }

    //Initializes the Tableviews
    private void initializeTables()
    {
        //Non Associated Parts Tableview
        System.out.println("Initializing Add Part Add Parts tableview");
        addProductAddPartIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        addProductAddPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        addProductAddPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        addProductAddPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        addProductAddPartTableView.getItems().setAll(nonAssociatedParts);

        //Associated Parts Tableview
        System.out.println("Initializing Add Part Associated Parts tableview");
        addProductAssociatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        addProductAssociatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        addProductAssociatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        addProductAssociatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        addProductAssociatedPartTableView.getItems().setAll(associatedParts);
    }

    private void updateIncludedParts()
    {
        System.out.println("Refreshing Add Part tableview");
        addProductAddPartTableView.getSelectionModel().clearSelection();
        addProductAddPartTableView.getItems().clear();
        addProductAddPartTableView.getItems().setAll(nonAssociatedParts);
    }

    private void updateNonIncludedPartsTableView()
    {
        System.out.println("Refreshing Associated Part tableview");
        addProductAssociatedPartTableView.getSelectionModel().clearSelection();
        addProductAssociatedPartTableView.getItems().clear();
        addProductAssociatedPartTableView.getItems().setAll(associatedParts);
    }

    private void updateNonIncludedPartsTableView(String searchString, ObservableList<Part> parts)
    {
        System.out.println("Updating Parts tableview from search string");
        if (searchString.isBlank()) {
            addProductAddPartTableView.getItems().clear();
            addProductAddPartTableView.getItems().setAll(nonAssociatedParts);
        } else {
            if (Utilities.TryParseInt(searchString) != null) {
                ObservableList<Part> part = FXCollections.observableArrayList();
                part.add(Inventory.lookupPart(Utilities.TryParseInt(searchString)));
                addProductAddPartTableView.getItems().setAll(part);
            } else {
                ObservableList<Part> matches = Inventory.lookupPart(searchString);
                ObservableList<Part> searchResult = FXCollections.observableArrayList();
                for (Part part: matches
                     ) {

                    ///////////////////////////////////////////////////////////
                    //TODO FIX THE PROBLEM WITH SEARCH NOT WORKING FOR NAMES!//
                    ///////////////////////////////////////////////////////////


                    if (!nonAssociatedParts.contains(part)) { searchResult.add(part); }
                }
                addProductAddPartTableView.getItems().setAll(searchResult);
            }
        }
    }


    private Product createProductFromFields ()
    {
        String message = Utilities.AreProductFieldsValid(
                addProductIDTextField.getText(),
                addProductNameTextField.getText(),
                addProductPriceTextField.getText(),
                addProductInventoryTextField.getText(),
                addProductMinTextField.getText(),
                addProductMaxTextField.getText(),
                associatedParts.size());
        //int id, String name, double price, int stock, int min, int max
        Integer id = Utilities.TryParseInt(addProductIDTextField.getText());
        String name = addProductNameTextField.getText();
        Double price = Utilities.TryParseDouble(addProductPriceTextField.getText());
        Integer stock = Utilities.TryParseInt(addProductInventoryTextField.getText());
        Integer min = Utilities.TryParseInt(addProductMinTextField.getText());
        Integer max = Utilities.TryParseInt(addProductMaxTextField.getText());

        if (message.isBlank()) {
            Product product = new Product (id, name, price, stock, min, max);
            for (Part part: associatedParts
                 ) {
                product.addAssociatedPart(part);
            }
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
}

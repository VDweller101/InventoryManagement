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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryController {

    private Part currentSelectedPart;
    /*
        ====Parts Pane====
     */
    @FXML
    private Pane partsPane;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partsIDColumn;

    @FXML
    private TableColumn<Part, String> partsNameColumn;

    @FXML
    private TableColumn<Part, Integer> partsInventoryColumn;

    @FXML
    private TableColumn<Part, Double> partsPriceColumn;

    @FXML
    private Button partsDeleteButton;

    @FXML
    private Button partsModifyButton;

    @FXML
    private Button partsAddButton;

    @FXML
    private Label partsLabel;

    @FXML
    private TextField partsSearchField;


    /*
        ====Products Pane====
     */

    @FXML
    private Pane productsPane;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productsIdColumn;

    @FXML
    private TableColumn<Product, String> productsNameColumn;

    @FXML
    private TableColumn<Product, Integer> productsInventoryColumn;

    @FXML
    private TableColumn<Product, Double> productsPriceColumn;

    @FXML
    private Button productsDeleteButton;

    @FXML
    private Button productsModifyButton;

    @FXML
    private Button productsAddButton;

    @FXML
    private TextField productsSearchField;

    @FXML
    private Button exitButton;


    /**
     * Initializes and populates Part and Product tableviews.
     * Create listener for Part and Product search text fields.
     */
    public void initialize ()
    {
        initializePartTable();
        initializeProductTable();

        UpdatePartList();
        UpdateProductList();
        partsSearchField.textProperty().addListener(observable ->
                UpdatePartList(partsSearchField.getText()));
        productsSearchField.textProperty().addListener(observable ->
                UpdateProductList(productsSearchField.getText()));
    }

    /**
     * Switches to the Add Part scene.
     * @param event The button press that triggered the method call.
     * @throws IOException Will throw exception if switchScene() can not find the correct scene.
     */
    @FXML
    private void partsAddButton(ActionEvent event) throws IOException
    {
        UpdatePartList();
        switchScene(0, event);
    }

    /**
     * Checks if the user has selected a part to modify. If so, switch scene to Modify Part.
     * @param event The button press that triggered the method call.
     * @throws IOException Will throw exception if switchScene() can not find the correct scene.
     */
    @FXML
    private void partsModifyButton(ActionEvent event) throws IOException {
        Part selected = partsTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Utilities.CurrentSelectedPart = selected;
            switchScene(1, event);
        } else {
            Utilities.DisplayErrorMessage("Select Part", "You must select a Part to modify.");
        }
    }

    /**
     * If user has a part selected, delete the part.
     * Will fail if: User cancels, part can not be found, or part is in any product.
     * @param event The button press that triggered the method call.
     * @throws IOException Will throw exception if switchScene() can not find correct scene.
     */
    @FXML
    private void partsDeleteButton(ActionEvent event) throws IOException
    {
        Part selected = partsTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            if (Utilities.DisplayPrompt("Delete Part?", "Are you sure you want to delete the selected part? Deleted parts can not be recovered.") == true) {
                if (!Utilities.IsPartInAnyProducts(selected)) {
                    if (Inventory.deletePart(selected) != true) {
                        Utilities.DisplayErrorMessage("Failed Deletion", "The item deletion has failed.");
                    } else {
                        //Switch scene here to properly update the table view.
                        switchScene(4, event);
                    }
                } else {
                    String errorMessage = "Can not delete part because it is used in 1 or more products.\n The following products contain this part:\n\n";
                    for (Product pro : Utilities.GetAllProductsContainingPart(selected)) {
                        errorMessage += pro.getName() + "\n";
                    }
                    Utilities.DisplayErrorMessage("Part in use.", errorMessage);
                }
            }
        } else {
            Utilities.DisplayErrorMessage("Select Part", "You must select a part to delete.");
        }
    }

    /**
     * Switch scene to Add Product menu.
     * @param event The button press that triggered the method call.
     * @throws IOException Will throw exception if switchScene() can not find correct scene.
     */
    @FXML
    private void productsAddButton (ActionEvent event) throws IOException
    {
        switchScene(2, event);
    }

    /**
     * Check if user has selected a product and switch to Modify Product menu if they have.
     * @param event The button press that triggered the method call.
     * @throws IOException Will throw exception if switchScene() can not find correct scene.
     */
    @FXML
    private void productsModifyButton (ActionEvent event) throws IOException {
        if (productsTableView.getSelectionModel().getSelectedItem() != null) {
            Utilities.CurrentSelectedProduct = productsTableView.getSelectionModel().getSelectedItem();
            switchScene(3, event);
        } else {
            Utilities.DisplayErrorMessage("Select Item", "Please select a Product to modify.");
        }
    }

    /**
     * If user has a product selected, delete the product.
     * Will fail if: User cancels or product can not be found.
     * @param event The button press that triggered the method call.
     * @throws IOException Will throw exception if switchScene() can not find correct scene.
     */
    @FXML
    private void productsDeleteButton (ActionEvent event) throws IOException {
        Product selected = productsTableView.getSelectionModel().getSelectedItem();
        if (selected != null)
        {
            if (Utilities.DisplayPrompt("Delete Product?", "Are you sure you want to delete the selected product? Deleted products can not be recovered.") == true)
            {
                if (Inventory.deleteProduct(selected)) {
                    switchScene(4, event);
                } else {
                    Utilities.DisplayErrorMessage("Failed Deletion", "The item deletion has failed.");
                }
            }
        } else {
            Utilities.DisplayErrorMessage("Select Part", "You must select a part to delete.");
        }
    }

    /**
     * Call to Utilities to handle user exit request.
     * @param event The button press that triggered the method call.
     */
    @FXML
    private void inventoryExitButton(ActionEvent event)
    {
        Utilities.ExitApplication(event);
    }

    /**
     * Clear and repopulate the Part list.
     */
    public void UpdatePartList()
    {
        partsTableView.getSelectionModel().clearSelection();
        partsTableView.getItems().clear();
        partsTableView.getItems().setAll(Inventory.getAllParts());
    }

    /**
     * Clear and repopulate the Part list using a search string.
     * Will populate only with parts whose name matches the search string.
     * @param searchString The string to search for.
     */
    public void UpdatePartList(String searchString)
    {
        if (searchString.isBlank()) {
            partsTableView.getItems().clear();
            partsTableView.getItems().setAll(Inventory.getAllParts());
        } else {
            if (Utilities.TryParseInt(searchString) != null) {
                //Search by ID
                partsTableView.getItems().clear();
                ObservableList<Part> part = FXCollections.observableArrayList();
                part.add(Inventory.lookupPart(Utilities.TryParseInt(searchString)));
                partsTableView.getItems().setAll(part);
            } else {
                //Search by Name
                partsTableView.getItems().clear();
                partsTableView.getItems().setAll(Inventory.lookupPart(searchString));
            }
        }
    }

    /**
     * Initializes and populates Part tableview.
     */
    private void initializePartTable()
    {
        partsIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partsNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partsInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partsPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        partsTableView.getItems().setAll(Inventory.getAllParts());
    }

    /**
     * Clear and repopulate the Product list.
     */
    public void UpdateProductList()
    {
        productsTableView.getSelectionModel().clearSelection();
        productsTableView.getItems().clear();
        productsTableView.getItems().setAll(Inventory.getAllProducts());

    }

    /**
     * Clear and repopulate the Product list based on a search string.
     * Only Products whose name matches the search string will be shown.
     * @param searchString The string to search for.
     */
    public void UpdateProductList(String searchString)
    {
        if (searchString.isBlank()) {
            productsTableView.getItems().clear();
            productsTableView.getItems().setAll(Inventory.getAllProducts());
        } else {
            if (Utilities.TryParseInt(searchString) != null) {
                //Search by ID
                productsTableView.getItems().clear();
                ObservableList<Product> product = FXCollections.observableArrayList();
                product.add(Inventory.lookupProduct(Utilities.TryParseInt(searchString)));
                productsTableView.getItems().setAll(product);
            } else {
                //Search by Name
                productsTableView.getItems().clear();
                productsTableView.getItems().setAll(Inventory.lookupProduct(searchString));
            }
        }
    }

    /**
     * Initialize and populate the Product tableview.
     */
    private void initializeProductTable()
    {
        productsIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productsNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productsInventoryColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productsPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

        productsTableView.getItems().setAll(Inventory.getAllProducts());
    }

    /**
     * Switch to any other scene in the program. Other menus can only switch to this menu but this menu can switch to any other menu.
     * @param sceneIndex The scene to switch to: 0 = Add part | 1 = Modify part | 2 = Add Product | 3 = Modify Product | Default = This (Inventory)
     * @param event The event that triggered the method call.
     * @throws IOException Will throw exception if the scene to switch to can not be found.
     */
    @FXML
    public void switchScene(int sceneIndex, ActionEvent event) throws IOException {
        //Scene Index: 0 = Add part | 1 = Modify part | 2 = Add product | 3 = Modify Product | Default = This (Inventory)

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent root = null;
        switch (sceneIndex)
        {
            case 0:
                try {
                    root = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Add Part");
                stage.setScene(new Scene(root, 500, 600));
                break;
            case 1:
                try {
                    root = FXMLLoader.load(getClass().getResource("ModifyPart.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Modify Part");
                stage.setScene(new Scene(root, 500, 600));
                break;
            case 2:
                try {
                    root = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Add Product");
                stage.setScene(new Scene(root, 800, 600));
                break;
            case 3:
                try {
                    root = FXMLLoader.load(getClass().getResource("ModifyProduct.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Modify Product");
                stage.setScene(new Scene(root, 800, 600));
                break;
            case 4:
                try {
                    root = FXMLLoader.load(getClass().getResource("Inventory.fxml"));
                } catch (IOException e){
                    e.printStackTrace();
                }
                stage.setTitle("Inventory Management System");
                stage.setScene(new Scene(root, 800, 400));
                break;
            default:
                System.out.println("Call for unknown scene index" + sceneIndex);
                break;
        }
        stage.show();
    }

}

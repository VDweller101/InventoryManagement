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


    public void initialize ()
    {
        System.out.println("Initializing InventoryController.");
        initializePartTable();
        initializeProductTable();

        UpdatePartList();
        UpdateProductList();
        partsSearchField.textProperty().addListener(observable ->
                UpdatePartList(partsSearchField.getText(), Inventory.lookupPart(partsSearchField.getText())));
    }

    /*
    ////    Parts Pane Actions
    */
    @FXML
    private void partsAddButton(ActionEvent event) throws IOException
    {
        UpdatePartList();
        switchScene(0, event);
    }

    @FXML
    private void partsModifyButton(ActionEvent event) throws IOException {
        if (partsTableView.getSelectionModel().getSelectedItem() != null) {
            Part selected = partsTableView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Utilities.DisplayErrorMessage("Part Not Found", "Could not find the selected Part in IMS System.");
                return;
            } else {
                Utilities.CurrentSelectedPart = selected;
                switchScene(1, event);
            }
        } else {
            Utilities.DisplayErrorMessage("Select Part", "You must select a Part to modify.");
        }
    }
    @FXML
    private void partsDeleteButton(ActionEvent event) throws IOException
    {
        if (Utilities.DisplayPrompt("Delete Part?", "Are you sure you want to delete the selected part? Deleted parts can not be recovered.") == true) {
            if (partsTableView.getSelectionModel().getSelectedItem() != null) {
                Part selected = partsTableView.getSelectionModel().getSelectedItem();
                if (Inventory.deletePart(selected) != true) {
                    Utilities.DisplayErrorMessage("Failed Deletion", "The item deletion has failed.");
                } else {
                    switchScene(4, event);
                }
            } else {
                Utilities.DisplayErrorMessage("Select Part", "You must select a part to delete.");
            }
        }
    }

    @FXML
    private void productsAddButton (ActionEvent event) throws IOException
    {
        switchScene(2, event);
    }

    @FXML
    private void productsModifyButton (ActionEvent event) throws IOException {
        switchScene(3, event);
    }

    // Check if user actually wants to quit.
    @FXML
    private void inventoryExitButton(ActionEvent event)
    {
        Utilities.ExitApplication(event);
    }

    public void UpdatePartList()
    {
        System.out.println("Refreshing Parts tableview");
        partsTableView.getSelectionModel().clearSelection();
        partsTableView.getItems().clear();
        partsTableView.getItems().setAll(Inventory.getAllParts());
    }
    public void UpdatePartList(String searchString, ObservableList<Part> parts)
    {
        System.out.println("Updating Parts tableview from search string");
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

    //public void UpdatePartList(int id, )

    private void initializePartTable()
    {
        System.out.println("Initializing part tableview");
        partsIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partsNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partsInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partsPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        partsTableView.getItems().setAll(Inventory.getAllParts());
    }

    public void UpdateProductList()
    {
        System.out.println("Refreshing Products tableview");
        productsTableView.getSelectionModel().clearSelection();
        productsTableView.getItems().clear();
        productsTableView.getItems().setAll(Inventory.getAllProducts());

    }

    private void initializeProductTable()
    {
        productsIdColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("id"));
        productsNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productsInventoryColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productsPriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));

        productsTableView.getItems().setAll(Inventory.getAllProducts());
    }

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

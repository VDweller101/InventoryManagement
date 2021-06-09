package ims.view_control;

import ims.model.Inventory;
import ims.model.Part;
import ims.model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ModifyProductController {

    private Product currentSelectedProduct;
    private ObservableList<Part> currentProductAssociatedParts = FXCollections.observableArrayList();
    @FXML
    private TextField modifyProductIDTextField;

    @FXML
    private TextField modifyProductNameTextField;

    @FXML
    private TextField modifyProductInventoryTextField;

    @FXML
    private TextField modifyProductPriceTextField;

    @FXML
    private TextField modifyProductMinTextField;

    @FXML
    private TextField modifyProductMaxTextField;

    @FXML
    private Button modifyProductSaveButton;

    @FXML
    private Button modifyProductCancelButton;

    @FXML
    private Button modifyProductExitButton;

    @FXML
    private TextField modifyProductAddPartTextField;

    @FXML
    private TableView<Part> modifyProductAddPartTableView;

    @FXML
    private TableColumn<Part, Integer> modifyProductAddPartIDColumn;

    @FXML
    private TableColumn<Part, String> modifyProductAddPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> modifyProductAddPartInventoryColumn;

    @FXML
    private TableColumn<Part, Double> modifyProductAddPartPriceColumn;

    @FXML
    private Button modifyProductAddPartAddButton;

    @FXML
    private TableView<Part> modifyProductAssociatedPartTableView;

    @FXML
    private TableColumn<Part, Integer> modifyProductAssociatedPartIDColumn;

    @FXML
    private TableColumn<Part, String> modifyProductAssociatedPartNameColumn;

    @FXML
    private TableColumn<Part, Integer> modifyProductAssociatedPartInventoryColumn;

    @FXML
    private TableColumn<Part, Double> modifyProductAssociatedPartPriceColumn;

    @FXML
    private Button modifyProductAssociatedPartRemoveButton;

    @FXML
    void modifyProductCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All unsaved changes will be lost.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Cancel product modification?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            returnToMainMenu(event);
        }
    }

    @FXML
    void modifyProductExitButton(ActionEvent event) { Utilities.ExitApplication(event); }

    @FXML
    void modifyProductAddPartAddButton (ActionEvent event)
    {
        if (modifyProductAddPartTableView.getSelectionModel().getSelectedItem() != null) {
            currentProductAssociatedParts.add(modifyProductAddPartTableView.getSelectionModel().getSelectedItem());
            updateTableViews();
        } else {
            Utilities.DisplayErrorMessage("Select Part", "Please select part to add.");
        }
    }

    @FXML
    void modifyProductAssociatedPartRemoveButton (ActionEvent event)
    {
        if (modifyProductAssociatedPartTableView.getSelectionModel().getSelectedItem() != null) {
            currentProductAssociatedParts.remove(modifyProductAssociatedPartTableView.getSelectionModel().getSelectedItem());
            updateTableViews();
        } else {
            Utilities.DisplayErrorMessage("Select Part", "Please select part to remove.");
        }
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

    public void initialize()
    {
        currentSelectedProduct = Utilities.CurrentSelectedProduct;
        populateFields(currentSelectedProduct);
        for (Part part:currentSelectedProduct.getAllAssociatedParts()
             ) {
            currentProductAssociatedParts.add(part);
        }
        initializeTableViews();
    }
    private void populateFields (Product product)
    {
        //id, name, price, stock, min, max, parts
        modifyProductIDTextField.setText(String.valueOf(product.getId()));
        modifyProductNameTextField.setText(product.getName());
        modifyProductPriceTextField.setText(String.valueOf(product.getPrice()));
        modifyProductInventoryTextField.setText(String.valueOf(product.getPrice()));
        modifyProductMinTextField.setText(String.valueOf(product.getMin()));
        modifyProductMaxTextField.setText(String.valueOf(product.getMax()));
    }

    private void initializeTableViews()
    {
        //Initialize Add Part Tableview
        modifyProductAddPartIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        modifyProductAddPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        modifyProductAddPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        modifyProductAddPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        modifyProductAddPartTableView.getItems().setAll(getAvailableParts());

        //Initialize Associated Part Tableview
        modifyProductAssociatedPartIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        modifyProductAssociatedPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        modifyProductAssociatedPartInventoryColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        modifyProductAssociatedPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        modifyProductAssociatedPartTableView.getItems().setAll(currentProductAssociatedParts);
    }

    private ObservableList<Part> getAvailableParts ()
    {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (Part part:Inventory.getAllParts()
             ) {
            if (!currentProductAssociatedParts.contains(part)){ parts.add(part); }
        }
        return parts;
    }

    private void updateTableViews()
    {
        modifyProductAssociatedPartTableView.getItems().setAll(currentProductAssociatedParts);
        modifyProductAddPartTableView.getItems().setAll(getAvailableParts());
    }
}

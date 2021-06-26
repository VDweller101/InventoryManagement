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

    /**
     * Checks if all text fields contain valid data using Utilities.AreProductFieldsValid() and calls Inventory.UpdateProduct if they are.
     * @param event The event that triggered the method call.
     */
    @FXML
    void modifyProductSaveButton (ActionEvent event)
    {
        String validationMessage = Utilities.AreProductFieldsValid(
                modifyProductIDTextField.getText(),
                modifyProductNameTextField.getText(),
                modifyProductPriceTextField.getText(),
                modifyProductInventoryTextField.getText(),
                modifyProductMinTextField.getText(),
                modifyProductMaxTextField.getText(),
                currentProductAssociatedParts.size());
        if (validationMessage.isBlank()) {
            Product newProduct = createProductFromFields();
            if (newProduct != null){
                Inventory.updateProduct(Inventory.getAllProducts().indexOf(currentSelectedProduct), newProduct);
                returnToMainMenu(event);
            }
        } else {
            Utilities.DisplayErrorMessage("Product Modification Failed.", validationMessage);
        }
    }

    /**
     * Check if user is ok with losing unsaved progress. Return to main menu if yes.
     * @param event The event that triggered the method call.
     * @throws IOException Will throw exception if scene can not be found in returnToMainMenu.
     */
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

    /**
     * Exit application handled by Utilities.ExitApplication().
     * @param event The event that triggered the method call.
     */
    @FXML
    void modifyProductExitButton(ActionEvent event) { Utilities.ExitApplication(event); }

    /**
     * Moves a part from nonassociated parts to associated parts list.
     * @param event The event that triggered the method call.
     */
    @FXML
    void modifyProductAddPartAddButton (ActionEvent event)
    {
        Part part = modifyProductAddPartTableView.getSelectionModel().getSelectedItem();
        if (part != null) {
            currentProductAssociatedParts.add(part);
            updateTableViews();
        } else {
            Utilities.DisplayErrorMessage("Select Part", "Please select part to add.");
        }
    }

    /**
     * Moves a part from the associated parts to nonassociated parts list.
     * @param event The event that triggered the method call.
     */
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

    /**
     * Checks if the ID and Name the user input are already taken. If they are not, returns a new Product using values from text fields.
     * If the name or ID are taken, returns null and displays error message.
     * @return Newly created Product from text fields or null if name or ID are already in use.
     */
    private Product createProductFromFields () {
        Integer id = Utilities.TryParseInt(modifyProductIDTextField.getText());
        if (!isIDAvailable(id)) {
            Utilities.DisplayErrorMessage("ID Not Available.", "This ID is already taken by another Product.");
            return null;
        }
        String name = modifyProductNameTextField.getText();
        if (!isNameAvailable(name)) {
            Utilities.DisplayErrorMessage("Name Not Available.", "This name is already taken by another Product.");
            return null;
        }
        Double price = Utilities.TryParseDouble(modifyProductPriceTextField.getText());
        Integer stock = Utilities.TryParseInt(modifyProductInventoryTextField.getText());
        Integer min = Utilities.TryParseInt(modifyProductMinTextField.getText());
        Integer max = Utilities.TryParseInt(modifyProductMaxTextField.getText());

        Product product = new Product(id, name, price, stock, min, max);
        for (Part part : currentProductAssociatedParts
        ) {
            product.addAssociatedPart(part);
        }
        return product;
    }

    /**
     * Gets the product user wanted to modify from Utilites, populates all text fields, and calls initializeTableViews to initialize table views.
     */
    public void initialize()
    {
        currentSelectedProduct = Utilities.CurrentSelectedProduct;
        populateFields(currentSelectedProduct);
        for (Part part:currentSelectedProduct.getAllAssociatedParts()
             ) {
            currentProductAssociatedParts.add(part);
        }
        initializeTableViews();
        modifyProductAddPartTextField.textProperty().addListener(observable ->
                updateTableViews(modifyProductAddPartTextField.getText(), Inventory.lookupPart(modifyProductAddPartTextField.getText())));
    }

    /**
     * Accepts a Product and populates the text fields with values from it.
     * @param product The product to use to populate the menu's text fields.
     */
    private void populateFields (Product product)
    {
        modifyProductIDTextField.setText(String.valueOf(product.getId()));
        modifyProductNameTextField.setText(product.getName());
        modifyProductPriceTextField.setText(String.valueOf(product.getPrice()));
        modifyProductInventoryTextField.setText(String.valueOf(product.getStock()));
        modifyProductMinTextField.setText(String.valueOf(product.getMin()));
        modifyProductMaxTextField.setText(String.valueOf(product.getMax()));
    }

    /**
     * Initializes and populates associated and nonassociated parts tableviews.
     */
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

    /**
     * Gets list of all parts not associated with current Product.
     * @return ObservableList of all nonassociated parts.
     */
    private ObservableList<Part> getAvailableParts ()
    {
        ObservableList<Part> parts = FXCollections.observableArrayList();
        for (Part part:Inventory.getAllParts()) { if (!currentProductAssociatedParts.contains(part)){ parts.add(part); } }
        return parts;
    }

    /**
     * Updates both associated and nonassociated parts tableviews.
     */
    private void updateTableViews()
    {
        modifyProductAssociatedPartTableView.getItems().setAll(currentProductAssociatedParts);
        modifyProductAddPartTableView.getItems().setAll(getAvailableParts());
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //TODO Add search feature and finish commenting this script. After that...done?????//
    /////////////////////////////////////////////////////////////////////////////////////
    private void updateTableViews(String searchString, ObservableList<Part> parts)
    {
        System.out.println("Updating Parts tableview from search string");
        if (searchString.isBlank()) {
            modifyProductAddPartTableView.getItems().clear();
            modifyProductAddPartTableView.getItems().setAll(nonAssociatedParts);
        } else {
            if (Utilities.TryParseInt(searchString) != null) {
                ObservableList<Part> part = FXCollections.observableArrayList();
                Part result = Inventory.lookupPart(Utilities.TryParseInt(searchString));
                if (!associatedParts.contains(result)) {
                    part.add(result);
                }
                addProductAddPartTableView.getItems().setAll(part);
            } else {
                ObservableList<Part> searchResult = FXCollections.observableArrayList();
                for (Part part: parts
                ) {
                    if (!associatedParts.contains(part)) { searchResult.add(part); }
                }
                addProductAddPartTableView.getItems().setAll(searchResult);
            }
        }
    }

    /**
     * Closes current scene and opens main menu.
     * @param event The event that triggered the method call.
     */
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

    /**
     * Checks string against every other Product to see if a name has already been taken.
     * @param name The string used to check if the name is available.
     * @return True if the name is available, else false.
     */
    private Boolean isNameAvailable(String name)
    {
        for (Product pro:Inventory.getAllProducts()
             ) {
            if (pro.getName().compareTo(name) == 0 && pro.getName().compareTo(currentSelectedProduct.getName()) != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     *
     * Checks int against every other Product to see if an ID has already been taken.
     * @param ID The int used to check if the ID is available.
     * @return True if the ID is available, else false.
     */
    private Boolean isIDAvailable (int ID)
    {
        for (Product pro:Inventory.getAllProducts()
             ) {
            if (pro.getId() == ID && pro != currentSelectedProduct) {
                return false;
            }
        }
        return true;
    }
}

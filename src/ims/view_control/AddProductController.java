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

    private Product currentProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> nonAssociatedParts = FXCollections.observableArrayList();

    /**
     * Creates a product from the text fields and, if creation successful, adds new product to inventory.
     * @param event The event that called the method.
     */
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

    /**
     * Checks if user really wants to cancel product creation. Return to main menu if they do.
     * @param event The event that called the method.
     * @throws IOException Will throw an exception if the new scene can not be found.
     */
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

    /**
     * Handled by Utilities. Simple exit button with dialog box confirming user wants to exit.
     * @param event The event that called the method.
     */
    @FXML
    private void addProductExitButton(ActionEvent event)
    {
        Utilities.ExitApplication(event);
    }

    /**
     * Check if user has a part selected. If so, add that part to associated parts list. If not, display error message.
     * @param event The event that called the method.
     */
    @FXML
    private void addProductAddPartAddButton (ActionEvent event)
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

    /**
     * Initialize tableviews and add listener to Add Part search bar.
     */
    public void initialize()
    {
        //Creating dataset for add part and associated part table views, then initialize tableviews.
        for (Part part : Inventory.getAllParts()) { nonAssociatedParts.add(part);}
        associatedParts.clear();
        initializeTables();
        addProductAddPartTextField.textProperty().addListener(observable ->
                updateNonIncludedPartsTableView(addProductAddPartTextField.getText(), Inventory.lookupPart(addProductAddPartTextField.getText())));
    }

    /**
     * Initializes and populates tableviews for available parts and associated parts.
     */
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

    /**
     * Clears and repopulates included parts tableview.
     */
    private void updateIncludedParts()
    {
        System.out.println("Refreshing Add Part tableview");
        addProductAddPartTableView.getSelectionModel().clearSelection();
        addProductAddPartTableView.getItems().clear();
        addProductAddPartTableView.getItems().setAll(nonAssociatedParts);
    }
    /**
     * Clears and repopulates non-included parts tableview.
     */
    private void updateNonIncludedPartsTableView()
    {
        System.out.println("Refreshing Associated Part tableview");
        addProductAssociatedPartTableView.getSelectionModel().clearSelection();
        addProductAssociatedPartTableView.getItems().clear();
        addProductAssociatedPartTableView.getItems().setAll(associatedParts);
    }

    /**
     * Overloaded version that accepts a search string for user searching.
     * Updates non-included parts tableview with results of search.
     * Checks if search string is an int to allow for searching by ID
     * @param searchString The string to search for.
     * @param parts
     */
    private void updateNonIncludedPartsTableView(String searchString, ObservableList<Part> parts)
    {
        System.out.println("Updating Parts tableview from search string");
        if (searchString.isBlank()) {
            addProductAddPartTableView.getItems().clear();
            addProductAddPartTableView.getItems().setAll(nonAssociatedParts);
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
     * Runs checks on all text fields to make sure the value given by the user is valid.
     * Returns new product if all fields valid, or returns null and shows error message otherwise.
     * @return Newly created product if creation successful, else returns null
     */
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
        if (!isIDAvailable(id)) {
            Utilities.DisplayErrorMessage("ID Not Available.", "The item ID is already used by another Product.");
            return null;
        }
        String name = addProductNameTextField.getText();
        if (!isNameAvailable(name)) {
            Utilities.DisplayErrorMessage("Name Not Available.", "The item name is already used by another Product.");
            return null;
        }
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

    /**
     * Closes current stage and opens main menu.
     * @param event The event that called the method.
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
     * Checks if a given Product name is already taken.
     * @param name The name to check.
     * @return True if the name is available for use, false otherwise.
     */
    private Boolean isNameAvailable (String name)
    {
        for (Product pro:Inventory.getAllProducts()
             ) {
            if(pro.getName().compareTo(name) == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if a given Product ID is already taken.
     * @param ID The ID to check.
     * @return True if the ID is available for use, false otherwise.
     */
    private Boolean isIDAvailable (int ID)
    {
        for (Product pro:Inventory.getAllProducts()
             ) {
            if (pro.getId() == ID) {
                return false;
            }
        }
        return true;
    }
}

package ims.view_control;

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

public class ModifyProductController {

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
    private TableView<?> modifyProductAddPartTableView;

    @FXML
    private TableColumn<?, ?> modifyProductAddPartIDColumn;

    @FXML
    private TableColumn<?, ?> modifyProductAddPartNameColumn;

    @FXML
    private TableColumn<?, ?> modifyProductAddPartInventoryColumn;

    @FXML
    private TableColumn<?, ?> modifyProductAddPartPriceColumn;

    @FXML
    private Button modifyProductAddPartAddButton;

    @FXML
    private TableView<?> modifyProductAssociatedPartTableView;

    @FXML
    private TableColumn<?, ?> modifyProductAssociatedPartIDColumn;

    @FXML
    private TableColumn<?, ?> modifyProductAssociatedPartNameColumn;

    @FXML
    private TableColumn<?, ?> modifyProductAssociatedPartInventoryColumn;

    @FXML
    private TableColumn<?, ?> modifyProductAssociatedPartPriceColumn;

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

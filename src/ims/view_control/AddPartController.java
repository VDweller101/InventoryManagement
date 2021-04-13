package ims.view_control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AddPartController {

    /*
    ////    Radio Controls
     */
    @FXML
    private RadioButton addPartInHouseRadio;

    @FXML
    private RadioButton addPartOutsourcedRadio;

    /*
    ////    Attribute Fields
     */
    @FXML
    private TextField addPartIDTextField;

    @FXML
    private TextField addPartNameTextField;

    @FXML
    private TextField addPartPriceTextField;

    @FXML
    private TextField addPartStockTextField;

    @FXML
    private TextField addPartMinTextField;

    @FXML
    private TextField addPartMaxTextField;

    @FXML
    private TextField addPartMachineCompanyTextField;


    /*
    ////    Save/Cancel/Exit Buttons
     */
    @FXML
    private Button addPartSaveButton;

    @FXML
    private Button addPartCancelButton;

    @FXML
    private Button exitButton;




    /*
    ////    Save/Cancel/Exit Button Actions
     */

    @FXML
    private void addPartCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "All unsaved changes will be lost.");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setHeaderText("Cancel new part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            returnToMainMenu(event);
        }
    }

    @FXML
    private void addPartExitButton (ActionEvent event)
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
}


package ims.view_control;

import ims.model.InHouse;
import ims.model.Inventory;
import ims.model.Outsourced;
import ims.model.Part;
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

import static java.lang.Integer.*;

public class AddPartController {

    /*
    ////    Radio Controls
     */
    @FXML
    private RadioButton addPartInHouseRadio;

    @FXML
    private RadioButton addPartOutsourcedRadio;

    @FXML
    private ToggleGroup addPartToggleGroup;

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
    private Label addPartMachineCompanyLabel;

    @FXML
    private TextField addPartMachineCompanyTextField;

    /*
    ////    Radio Group
     */

    @FXML
    private void addPartToggleChanged(ActionEvent event)
    {
        RadioButton button = (RadioButton)event.getSource();
        char firstLetter = button.getText().charAt(0);
        if (firstLetter == 'O') {
            addPartMachineCompanyLabel.setText("Company Name");
        } else {
            addPartMachineCompanyLabel.setText("Machine ID");
        }
    }

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
    private void addPartSaveButton (ActionEvent event)
    {
        Boolean type;
        if (addPartInHouseRadio.isSelected()) {
            type = true;
        } else {
            type = false;
        }
        String message = Utilities.ArePartFieldsValid(
                type,
                addPartIDTextField.getText(),
                addPartNameTextField.getText(),
                addPartPriceTextField.getText(),
                addPartStockTextField.getText(),
                addPartMinTextField.getText(),
                addPartMaxTextField.getText(),
                addPartMachineCompanyTextField.getText());
        if (message.isBlank()) {
            Part part = createPartFromFields();
            if (part != null) {
                Inventory.addPart(part);
                System.out.println("AddPartController: Created new part named: " + part.getName());
                returnToMainMenu(event);
            } else {
                Utilities.DisplayErrorMessage("Error", "Something went wrong, returning to main menu.");
                returnToMainMenu(event);
            }
        } else {
            Utilities.DisplayErrorMessage("Error with Part Creation", message);
        }
    }

    @FXML
    private void addPartCancelButton(ActionEvent event) throws IOException
    {
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

    @FXML
    private Part createPartFromFields()
    {
        //Represents In-House vs Outsourced Part
        Boolean type;
        if (addPartInHouseRadio.isSelected()){ type = true; }
        else { type = false; }

        //Validate the text fields. Return error message of any problems.
        String errorMessage = Utilities.ArePartFieldsValid(
                type,
                addPartIDTextField.getText(),
                addPartNameTextField.getText(),
                addPartPriceTextField.getText(),
                addPartStockTextField.getText(),
                addPartMinTextField.getText(),
                addPartMaxTextField.getText(),
                addPartMachineCompanyTextField.getText());
        //If error message blank, part is valid. Create part.
        if (errorMessage.isBlank()) {
            Integer id = Utilities.TryParseInt(addPartIDTextField.getText());
            String name = addPartNameTextField.getText();
            Double price = Utilities.TryParseDouble(addPartPriceTextField.getText());
            Integer stock = Utilities.TryParseInt(addPartStockTextField.getText());
            Integer min = Utilities.TryParseInt(addPartMinTextField.getText());
            Integer max = Utilities.TryParseInt(addPartMaxTextField.getText());
            //if-else for In-house vs Outsourced.
            if (addPartInHouseRadio.isSelected()) {
                //Create In-House Part
                Integer machineID = Utilities.TryParseInt(addPartMachineCompanyTextField.getText());
                Part part = new InHouse(id, name, price, stock, min, max, machineID);
                String message = Utilities.IsPartValid ((InHouse)part);
                //One more validation, this time on the Part object itself. Don't want invalid parts leaving this class.
                if (message.isBlank()) { return part; }
                else { Utilities.DisplayErrorMessage("Part Creation Error", message); }
            } else {
                //Create Outsourced Part
                String companyName = addPartMachineCompanyTextField.getText();
                if (errorMessage.isBlank()) {
                    Part part = new Outsourced(id, name, price, stock, min, max, companyName);
                    //One more validation, this time on the Part object itself. Don't want invalid parts leaving this class.
                    String message = Utilities.IsPartValid ((Outsourced) part);
                    if (message.isBlank()) { return part; }
                    else { Utilities.DisplayErrorMessage("Part Creation Error", message); }
                } else {
                    Utilities.DisplayErrorMessage("Part Creation Error", errorMessage);
                }
            }
        } else {
            Utilities.DisplayErrorMessage("Part Creation Error", errorMessage);
        }
        Utilities.DisplayErrorMessage("Unknown Error", "Returning null value from AddPartController.createPartFromFields()");
        return null;
    }
}


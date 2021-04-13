package ims.view_control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class ModifyPartController {

    @FXML
    private RadioButton modifyPartInHouseRadio;

    @FXML
    private RadioButton modifyPartOutsourcedRadio;

    @FXML
    private TextField modifyPartIDTextField;

    @FXML
    private TextField modifyPartNameTextField;

    @FXML
    private TextField modifyPartPriceTextField;

    @FXML
    private TextField modifyPartStockTextField;

    @FXML
    private TextField modifyPartMinTextField;

    @FXML
    private TextField modifyPartMaxTextField;

    @FXML
    private Label modifyPartMachineCompanyLabel;

    @FXML
    private TextField modifyPartMachineCompanyTextField;

    @FXML
    private Button modifyPartSaveButton;

    @FXML
    private Button modifyPartCancelButton;

    @FXML
    private Button modifyPartExitButton;

}


package clientGUI;

import client.ClientController;
import commonClasses.Message;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.sql.rowset.CachedRowSet;

import java.sql.SQLException;

import static client.ClientController.savedWindows;

/**
 *
 *  controller class for insert complaint window
 *
 * <p> Project Name: Zer-Li (Java Application Flower Store) </p>
 *
 * @author Habib Ibrahim, Vitaly Rofman, Ibrahim Daoud, Yosif Hosen
 * @version  V1.00  2022
 */

public class InsertComplaintGUIController {

    @FXML
    private TextArea text_complaintText;

    @FXML
    private Label lbl_error;

    @FXML
    private ImageView btn_back;

    @FXML
    private ChoiceBox<String> dr_shop;
    @FXML
    private ImageView btn_insert;
    @FXML
    private TextField txy_orderId;

    ClientController conn = ClientController.getClientController();

    /**
     * Gui init
     * @throws SQLException getting data from DB
     */
    public void initialize() throws SQLException {
        lbl_error.setText("");
        Message msg = new Message();
        msg.setCommand("getAllTheShops");
        ClientController.getClientController().send(msg);
        CachedRowSet cachedMsg = (CachedRowSet) ClientController.messageFromServer.getMsg();
        while(cachedMsg.next()){ dr_shop.getItems().add(cachedMsg.getString("shop")); }
    }

    /**
     *  clickedBackBtn - click in back button to get back to user original window
     *  @param event - mouse click
     */
    @FXML
    void clickedBackBtn(MouseEvent event) {
        Stage stage = ((Stage)lbl_error.getScene().getWindow());
        stage.close();
        Stage window = savedWindows.getUserWindow();
        LoginGUIController.loginController.initialize();
        window.show();
    }

    /**
     *  clickedInsertBtn - insert complaint to data base
     *  @param event - mouse click
     */
    @FXML
    void clickedInsertBtn(MouseEvent event) {
        System.out.println(text_complaintText.getText());
        if (dr_shop.getValue() != null){
            if (!text_complaintText.getText().equals("")){
        Message msg = new Message();
        String[] orderData = new String[4];
        orderData[0] = String.valueOf(ClientController.userLoginData.getUserid());
        orderData[1] = text_complaintText.getText();
        orderData[2] = dr_shop.getValue();
        if(!txy_orderId.getText().equals("")){ orderData[3] = txy_orderId.getText(); }
        else { lbl_error.setText("Please enter order number"); return;}
        msg.setCommand("insertComplaint");
        msg.setMsg(orderData);
        conn.send(msg);
        while(conn.awaitResponse);
        lbl_error.setText("Complaint Inserted Successfully!");}
            else{ lbl_error.setText("Enter Complaint Information");}}
        else { lbl_error.setText("Select shop to insert complaint!");}
    }

    /**
     *  enteredBackBtn - function for making shadow for button
     *  @param event - entering the button with the mouse
     */
    @FXML
    void enteredBackBtn(MouseEvent event) { conn.enteredButton(btn_back);}

    /**
     *  enteredInsertBtn - function for making shadow for button
     *  @param event - entering the button with the mouse
     */
    @FXML
    void enteredInsertBtn(MouseEvent event) { conn.enteredButton(btn_insert);}

    /**
     *  leavedBackBtn - function to hide shadow for button
     *  @param event - leaving the button with the mouse
     */
    @FXML
    void leavedBackBtn(MouseEvent event) { conn.leavedButton(btn_back);}

    /**
     *  leavedInsertBtn - function to hide shadow for button
     *  @param event - leaving the button with the mouse
     */
    @FXML
    void leavedInsertBtn(MouseEvent event) { conn.leavedButton(btn_insert);}
}

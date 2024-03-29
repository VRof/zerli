package clientGUI;

import client.ClientController;
import client.NewWindowFrameController;
import clientClasses.RegistrationTable;
import commonClasses.Message;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.sql.rowset.CachedRowSet;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
/**
 * ManagePermissionsGUIController class is responsible for allowing the shop manager to
 * manage the permissions of each worker in the system - worker: any user that is not customer
 * or CEO or shop manager - changing the permission means changing their position or job
 *  by editing type field in the table and right after then clicking confirm changes button.
 *
 * <p> Project Name: Zer-Li (Flowers store in Java) </p>
 *
 * @author Habib Ibrahim, Vitaly Rofman, Ibrahim Daoud, Yosif Hosen
 * @version  V1.00  2022
*/
public class ManagePermissionsGUIController implements Initializable {
    public ClientController cc = ClientController.getClientController();

    @FXML
    private ImageView backBtn;

    @FXML
    private ImageView confirmChangesBtn;
    @FXML
    private Label errorLbl;
    @FXML
    private TableView<RegistrationTable> manageUsers;
    @FXML
    private TableColumn<RegistrationTable, String> typeCol;
    @FXML
    private TableColumn<RegistrationTable, String> userIDCol;
    @FXML
    private TableColumn<RegistrationTable, String> usernameCol;
    private ObservableList<RegistrationTable> UsersList;

    /**
     * initialize method shows the manager a table with workers = { users \ customers+managers+ceo } data,
     * so he can select one and change his permission by editing the Type.
     * @param location
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resources
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        UsersList = FXCollections.observableArrayList();
        CachedRowSet cachedMsg;
        //***
        //1.send msg to server
        Message msg = new Message(); //msg to be sent to server
        msg.setCommand("showUsersPermissionsTable");
        msg.setMsg("");
        cc.send(msg);
        //2.receive from server

        while (cc.awaitResponse) ; //wait for data from server
        cachedMsg = (CachedRowSet) (cc.messageFromServer.getMsg()); //message received from server

        //3. organize the msg from server in order to show it
        try {
            while (cachedMsg.next()) { //get user data
                RegistrationTable rt = new RegistrationTable();
                rt.setUserId(cachedMsg.getInt("userid"));
                rt.setUsername(cachedMsg.getString("username"));
                rt.setType(cachedMsg.getString("usertype"));
                UsersList.add(rt);
            }
        } catch (SQLException e) {
            System.out.println("Error read data from server " + e);
        }
        // THIS IS TO ADD THE DATA TO THE GUI
        manageUsers.setItems(UsersList);
        // *** EDIT ***
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setOnEditCommit( // edit the Type field
                (TableColumn.CellEditEvent<RegistrationTable, String> t) ->
                        (t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setType(t.getNewValue())
        );
    }

    /**
     * clickedConfirmChangesBtn method sends the server
     * the userid that the manager selected and the new type (permission)
     * @param event ConfirmChanges button clicked
     */
    @FXML
    void clickedConfirmChangesBtn(MouseEvent event) {
        Message m = new Message();
        String savedType="";
        int savedUserID = 0;
        try {
            savedUserID = manageUsers.getSelectionModel().getSelectedItem().getUserId();
            savedType = manageUsers.getSelectionModel().getSelectedItem().getType();
        } catch (Exception e) {
            System.out.println("null row selected");
        }
        CachedRowSet cachedMsg = null;
        m.setCommand("changePermission");
        Object [] data = {savedUserID,savedType};
        m.setMsg(data);
        cc.send(m);// send the row key (orderId) + the type that the user edited
        String stringShownInErrorLabel;

        if (savedUserID==0)
            stringShownInErrorLabel = "Please select user !";
        else
            stringShownInErrorLabel = String.format("user %d permission is updated to %s!", savedUserID,savedType);

        errorLbl.setText(stringShownInErrorLabel);

        while (cc.awaitResponse) ; //wait for data from server
    }
    /**
     * clickedBackBtn method directs the user to the previous GUI - ShopManagerGUI - when clicking back
     * @param event clicked back button
     * @throws Exception javafx exception when creating new window
     */
    @FXML
    void clickedBackBtn(MouseEvent event) throws Exception {
        new NewWindowFrameController("ShopManagerGUI").start(new Stage());
        Stage stage = (Stage) errorLbl.getScene().getWindow();
        stage.hide();
    }
    @FXML
    void enteredConfirmChangesBtn(MouseEvent event) {cc.enteredButton(confirmChangesBtn);}
    @FXML
    void leavedConfirmChangesBtn(MouseEvent event) {cc.leavedButton(confirmChangesBtn);}
    @FXML
    void enteredBackBtn(MouseEvent event) {cc.enteredButton(backBtn);}
    @FXML
    void leavedBackBtn(MouseEvent event) {cc.leavedButton(backBtn);}
}
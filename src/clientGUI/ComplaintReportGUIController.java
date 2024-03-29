package clientGUI;

import client.ClientController;
import commonClasses.Message;
import commonClasses.Quarter;
import commonClasses.WantedReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
/**
 *
 *  Controller class for complaint report window
 *
 * <p> Project Name: Zer-Li (Java Application Flower Store) </p>
 *
 * @author Habib Ibrahim, Vitaly Rofman, Ibrahim Daoud, Yosif Hosen
 * @version  V1.00  2022
 */
public class ComplaintReportGUIController implements Initializable {
    public ClientController cc = ClientController.getClientController();
    @FXML
    private Label errorLbl;

    @FXML
    private ImageView backBtn;

    @FXML
    private ImageView showBtn;

    @FXML
    private ChoiceBox<Integer> selectQuarter;


    @FXML
    private CategoryAxis monthsInQuarter;


    @FXML
    private NumberAxis numberOfComplaints;
    @FXML
    private BarChart<String, Number> ComplaintDiagram;
    @FXML
    private ChoiceBox<Integer> selectYear;


    /**
     * Method(initialize) initialize the relevant fields in window when opened
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
        selectQuarter.getItems().add(1);
        selectQuarter.getItems().add(2);
        selectQuarter.getItems().add(3);
        selectQuarter.getItems().add(4);
        selectYear.getItems().add(2022);
        selectYear.getItems().add(2023);
        selectYear.getItems().add(2024);
        selectYear.getItems().add(2025);

    }


    /**
     *Method(clickedShowBtn) activates when button show is pressed checks if the input from choice boxes is correct
     * if not its shows relevant msg to the client otherwise send a msg to db to extract the relevant data to fill
     * a barchart depends on quarters and number of complaints
     * @param event mouse click
     */
    @FXML
    void clickedShowBtn(MouseEvent event) {
        errorLbl.setText("");
        ComplaintDiagram.getData().clear();
        monthsInQuarter.getCategories().clear();
        
        WantedReport wr = ViewReportsGUIController.controller.getWr();
                Message msg = new Message(); //msg to be sent to server
        Quarter quarterMsg,quarter;
        if(selectYear.getSelectionModel().isEmpty()||selectQuarter.getSelectionModel().isEmpty()) {
            errorLbl.setText("you should fill quarter and year");
        }else {
            quarterMsg = new Quarter(selectYear.getSelectionModel().getSelectedItem(), selectQuarter.getSelectionModel().getSelectedItem(), wr.getShopName());
            msg.setCommand("viewComplaint");
            msg.setMsg(quarterMsg);
            cc.send(msg);
            while (cc.awaitResponse) ; //wait for data from server
            quarter = (Quarter) (cc.messageFromServer.getMsg()); //message received from server (details about quarter)
            List<String> months1 = new ArrayList<>();
            months1.add(quarter.getMonth1());
            months1.add(quarter.getMonth2());
            months1.add(quarter.getMonth3());

            ObservableList<String> months = FXCollections.observableArrayList(months1);
            monthsInQuarter.setCategories(months);

            XYChart.Series<String, Number> series1 = new XYChart.Series<>();
            series1.setName("shop :"+wr.getShopName());
            series1.getData().add(new XYChart.Data(monthsInQuarter.getCategories().get(0), quarter.getResult1()));
            series1.getData().add(new XYChart.Data(monthsInQuarter.getCategories().get(1), quarter.getResult2()));
            series1.getData().add(new XYChart.Data(monthsInQuarter.getCategories().get(2), quarter.getResult3()));
            ComplaintDiagram.getData().addAll(series1);
        }
    }

    /**
     * Method(clickedBackBtn) activates when pressing the button back showing the window from before that been
     * saved (view reports window)
     * @param event mouse click
     */
    @FXML
    void clickedBackBtn(MouseEvent event) {
        Stage stage = (Stage) ComplaintDiagram.getScene().getWindow();
        stage.hide();
        ClientController.savedWindows.getViewReportsWindow().show();

    }


    @FXML
    void enteredBackBtn(MouseEvent event) {
        cc.enteredButton(backBtn);
    }

    @FXML
    void enteredShowBtn(MouseEvent event) {
        cc.enteredButton(showBtn);
    }

    @FXML
    void leavedBackBtn(MouseEvent event) {
        cc.leavedButton(backBtn);

    }

    @FXML
    void leavedShowBtn(MouseEvent event) {
        cc.leavedButton(showBtn);

    }


}

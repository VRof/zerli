package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientConnectionWindowFrameController extends Application {
    private AnchorPane mainLayout;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("ZerLi");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/clientFXML/ClientConnectionGUI.fxml"));
        mainLayout = loader.load();
        Scene scene = new Scene(mainLayout);
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
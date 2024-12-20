package app;

import app.config.SetUpApp;
import app.config.ViewConfig.FXMLResolver;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * JavaFX App
 */
public class AppAdmin extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The starter method in JavaFx application
     *
     * @param stage the primary stage for this application, onto which
     *              the application scene can be set.
     *              Applications may create other stages, if needed, but they will
     *              not be
     *              primary stages.
     */
    @SuppressWarnings("exports")
    @Override
    public void start(Stage stage) {
        stage.initStyle(StageStyle.UNDECORATED);
        FXMLResolver.getInstance().setUp("/view", stage, 1100, 650);
        SetUpApp.setUpApp();
    }
}

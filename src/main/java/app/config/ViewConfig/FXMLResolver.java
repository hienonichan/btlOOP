package app.config.ViewConfig;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FXMLResolver implements Resolver {
    private int sceneWidth;
    private int sceneHeight;

    private String rootPath;
    private String type = "fxml";
    private static FXMLResolver fxmlResolver;
    private Stage primaryStage;
    private Scene scene;

    private FXMLResolver() {

    }

    public static FXMLResolver getInstance() {
        if (fxmlResolver == null) {
            fxmlResolver = new FXMLResolver();
        }
        return fxmlResolver;
    }

    private Parent resolve(String viewName) {
        try {
            String pathLoader = rootPath + "/" + viewName + "." + type;
            System.out.println(pathLoader);
            FXMLLoader loaderObject = new FXMLLoader(getClass().getResource(pathLoader));
            return loaderObject.load();
        } catch (IOException e) {
            System.out.println("can't find view " + viewName);
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void setUp(String path, Stage stage, int sceneWidth, int sceneHeight) {
        this.rootPath = path;
        this.primaryStage = stage;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }

    @Override
    public void renderScene(String viewName) {
        scene = new Scene(resolve(viewName), sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

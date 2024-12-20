package app.config.ViewConfig;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * {@link FXMLResolver} implements interface {@link Resolver} used for rendering
 * FXML resources
 * Use Singleton design pattern for one stage which can switch many scenes
 */
public class FXMLResolver implements Resolver {
    private int sceneWidth;
    private int sceneHeight;

    private String rootPath;
    private String type = "fxml";
    private static FXMLResolver fxmlResolver;
    private Stage primaryStage;
    private Scene scene;
    private FXMLLoader loaderObject;

    /**
     * private Constructor for Singleton design pattern
     */
    private FXMLResolver() {

    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    /**
     * get current instance of {@link FXMLResolver} or create new instance
     *
     * @return Instance of {@link FXMLResolver}
     */
    public static FXMLResolver getInstance() {
        if (fxmlResolver == null) {
            fxmlResolver = new FXMLResolver();
        }
        return fxmlResolver;
    }

    /**
     * @param viewName resources fileName/viewName
     * @return Superclass Object of Javafx 's component
     */
    private Parent resolve(String viewName) {
        try {
            String pathLoader = rootPath + "/" + viewName + "." + type;
            System.out.println(pathLoader);
            loaderObject = new FXMLLoader(getClass().getResource(pathLoader));
            return loaderObject.load();
        } catch (IOException e) {
            System.out.println("can't find view " + viewName);
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * setup starting properties for instance of {@link FXMLResolver}
     *
     * @param path        Path to resources file
     * @param stage       JavaFX 's stage
     * @param sceneWidth  sceneWidth when rendering in stage
     * @param sceneHeight sceneHeight when rendering in stage
     */
    @Override
    public void setUp(String path, Stage stage, int sceneWidth, int sceneHeight) {
        this.rootPath = path;
        this.primaryStage = stage;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }

    /**
     * Get viewName from input , create {@link Scene} and
     * set to {@link Stage}
     *
     * @param viewName resources fileName/viewName
     */
    @Override
    public void renderScene(String viewName) {
        scene = new Scene(resolve(viewName), sceneWidth, sceneHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @return Javafx {@link Stage}
     */
    public Stage getStage() {
        return this.primaryStage;
    }

    /**
     * @return Javafx {@link FXMLLoader}
     */
    public FXMLLoader getLoader() {
        return loaderObject;
    }
}

package com.rspsi;

import java.io.File;

import com.jagex.Client;
import com.jagex.map.SceneGraph;
import com.rspsi.controls.WindowControls;
import com.rspsi.game.CanvasPane;
import com.rspsi.options.Options;
import com.rspsi.resources.ResourceLoader;
import com.rspsi.util.ChangeListenerUtil;
import com.rspsi.util.FXDialogs;
import com.rspsi.util.FilterMode;
import com.rspsi.util.RetentionFileChooser;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MultiRegionMapWindow extends Application {

	private Stage primaryStage;
	
	
    @FXML
    private BorderPane topBar;
    
    @FXML
    private HBox controlBox;

    @FXML
    private HBox dockContainer;

    @FXML
    private AnchorPane mapPane;

    @FXML
    private Button redrawImageBtn;
    
    @FXML
    private Button saveImageBtn;
    

    @FXML
    private Font x312;

    @FXML
    private Color x412;

    @FXML
    private Spinner<?> currentHeightSpinner;
    
    @FXML
    private CheckBox showBordersCheck;

    @FXML
    private CheckBox showCameraCheck;
    
    @FXML
    private CheckBox showFileCheck;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/mapview.fxml"));
		loader.setController(this);
		Parent content = loader.load();
		Scene scene = new Scene(content);
		
		
		
		primaryStage.setTitle("RSPSi Multi Region Map");
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(ResourceLoader.getSingleton().getLogo64());
		
		primaryStage.setOnHiding(evt -> visible.set(false));
		
		WindowControls.addWindowControls(primaryStage, topBar, controlBox);
		
		redrawImageBtn.setOnAction(evt -> SceneGraph.minimapUpdate = true);
		Options.showBorders.bind(this.showBordersCheck.selectedProperty());
		Options.showCamera.bind(this.showCameraCheck.selectedProperty());
		Options.showMapFileNames.bind(this.showFileCheck.selectedProperty());
		ChangeListenerUtil.addListener(() -> SceneGraph.minimapUpdate = true, Options.showCamera, Options.showBorders, Options.showMapFileNames);
		saveImageBtn.setOnAction(evt -> {
			File f = RetentionFileChooser.showSaveDialog(FilterMode.PNG);
			if(f != null) {
				try {
					System.out.println(f.getAbsolutePath());
					Client.getSingleton().saveMapFullImage(f);
				} catch (Exception e) {
					e.printStackTrace();
					FXDialogs.showError("Error while loading saving image", "There was a failure while attempting to save\nthe minimap to the selected file.");
					
				}
			}
		});
	}
	
	public void resizeMap() {
		Platform.runLater(() -> {
			Screen screen = Screen.getPrimary();
			primaryStage.setMaxWidth(screen.getBounds().getWidth() - 30);
			primaryStage.setMaxHeight(screen.getBounds().getHeight() - 100);
			mapPane.getChildren().clear();
			mapPane.getChildren().add(new CanvasPane(Client.getSingleton().fullMapCanvas));
			primaryStage.sizeToScene();
		});
	}
    
	public void show() {
		visible.set(true);
		primaryStage.show();
		primaryStage.sizeToScene();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		this.primaryStage.close();
	}

	private SimpleBooleanProperty visible = new SimpleBooleanProperty();
	public SimpleBooleanProperty visibleProperty() {
		return visible;
	}


}
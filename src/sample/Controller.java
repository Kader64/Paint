package sample;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Optional;

public class Controller{
    @FXML
    private Canvas canvas;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private TextField brushSize;
    @FXML
    private TextField width;
    @FXML
    private TextField height;
    @FXML
    private CheckBox eraser;
    @FXML
    private ChoiceBox<String> shapeBox;
    @FXML
    private ChoiceBox<String> typeBox;
    @FXML
    private TextField mouseLocation;
    private GraphicsContext gc;
    private File currentFile;
    private Stage primaryStage;
    private Shapes shapes;

    @FXML
    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        shapes = new Shapes(gc);
        shapeBox.getItems().addAll("Square","Circle","Triangle","Star","Heart");
        shapeBox.setValue("Square");
        typeBox.getItems().addAll("Fill","Stroke");
        typeBox.setValue("Fill");
        canvas.setOnMouseDragged(this::drawing);
        canvas.setOnMouseClicked(this::drawing);
        canvas.setOnMouseMoved(e-> mouseLocation.setText("X: "+e.getX()+"   Y: "+e.getY()));
    }
    private void drawing(MouseEvent e)
    {
        double size;
        double w;
        double h;

        try{
            w = Double.parseDouble(width.getText());
            h = Double.parseDouble(height.getText());
            if(w>8000 || h>8000)
            {
                throw new Exception();
            }
        }
        catch(Exception ex){
            w = 800.0;
            h = 600.0;
            width.setText("800");
            height.setText("600");
        }

        try{
            size = Double.parseDouble(brushSize.getText());
            if(size>2000)
            {
                throw new Exception();
            }
        }
        catch(Exception ex){
            brushSize.setText("20");
            size = 20.0;
        }

        canvas.setWidth(w);
        canvas.setHeight(h);
        double x = e.getX() - size/2;
        double y = e.getY() - size/2;

        if(eraser.isSelected()){
            gc.clearRect(x,y,size,size);
        }
        else{
            if(typeBox.getValue().equals("Fill")){
                 gc.setFill(colorPicker.getValue());
                switch (shapeBox.getValue()) {
                    case "Square" -> gc.fillRect(x, y, size, size);
                    case "Circle" -> gc.fillOval(x, y, size, size);
                    case "Triangle" -> shapes.drawTriangle("fill",x,y,size);
                    case "Star" -> shapes.drawStar("fill",x,y,5,size/1.5,size/3);
                    case "Heart" -> shapes.drawHeart("fill",x,y,size);
                }
            }
            else {
                gc.setStroke(colorPicker.getValue());
                switch (shapeBox.getValue()) {
                    case "Square" -> gc.strokeRect(x, y, size, size);
                    case "Circle" -> gc.strokeOval(x, y, size, size);
                    case "Triangle" -> shapes.drawTriangle("stroke",x,y,size);
                    case "Star" -> shapes.drawStar("stroke",x,y,5,size,size/2);
                    case "Heart" -> shapes.drawHeart("stroke",x,y,size);
                }
            }
        }
        mouseLocation.setText("X: "+e.getX()+"   Y: "+e.getY());
    }
    private String getFileExtension(File file){
        String fileName = file.getName();
        return fileName.substring(fileName.lastIndexOf(".") + 1, file.getName().length());
    }
    public void save(){
        if(currentFile!=null){
            try{
                Image snapshot = canvas.snapshot(null,null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),getFileExtension(currentFile),new File(currentFile.getAbsolutePath()));
            }
            catch(Exception e)
            {
                System.out.println("Failed to save image: "+e);
            }
        }
        else {
            saveAs();
        }
    }
    public void open(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select file:");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );
        currentFile = fileChooser.showOpenDialog(primaryStage);
        if (currentFile != null) {
            Image image = new Image(currentFile.toURI().toString());
            gc.clearRect(0,0,canvas.getWidth(),canvas.getHeight());
            canvas.setWidth(image.getWidth());
            canvas.setHeight(image.getHeight());
            width.setText(String.valueOf(image.getWidth()));
            height.setText(String.valueOf(image.getHeight()));
            gc.drawImage(image,0,0,image.getWidth(),image.getHeight());
        }
    }
    public void saveAs(){
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file:");
            fileChooser.setInitialFileName("My file");
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("*PNG", "*.png"),
                new FileChooser.ExtensionFilter("*JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("*GIF", "*.gif")
            );
            try{
                currentFile = fileChooser.showSaveDialog(primaryStage);
                if(currentFile!=null) {
                    fileChooser.setInitialDirectory(currentFile.getParentFile());
                    Image snapshot = canvas.snapshot(null,null);
                    ImageIO.write(SwingFXUtils.fromFXImage(snapshot,null),getFileExtension(currentFile),new File(currentFile.getAbsolutePath()));
                }
            }
            catch (Exception e){
                System.out.println("Failed to save image: "+e);
            }

        }
    public void exit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Paint - Exit");
        alert.setHeaderText("Do you want to save your project?");
        ButtonType btn1 = new ButtonType("Save");
        ButtonType btn2 = new ButtonType("Don't save");
        ButtonType btn3 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(btn1,btn2,btn3);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == btn1){
            save();
            Platform.exit();
        }
        else if(result.get() == btn2) {
            Platform.exit();
        }
    }
}

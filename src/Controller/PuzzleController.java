package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PuzzleController {

    @FXML private ImageView uploadedImageView;
    @FXML private Button uploadButton;
    @FXML private Button shuffleButton;
    @FXML private Button solveButton;

    @FXML
    private void handleUploadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                // Load the image
                Image image = new Image(new FileInputStream(selectedFile));
                uploadedImageView.setImage(image);
                System.out.println("Image uploaded: " + selectedFile.getName());

                // Set the ImageView's size (square) and preserve ratio
                double size = 300; // You can change this size to fit your layout
                uploadedImageView.setFitWidth(size);
                uploadedImageView.setFitHeight(size);
                uploadedImageView.setPreserveRatio(true);

                // Now clip the image to a square from the center
                clipImageToSquare(image, size);

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void clipImageToSquare(Image image, double size) {
        // Get the image dimensions
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        // Calculate the cropping region (center of the image)
        double cropWidth = size;
        double cropHeight = size;

        // If the image is wider than tall, crop the width to fit the height
        if (imageWidth > imageHeight) {
            double centerX = (imageWidth - imageHeight) / 2;
            uploadedImageView.setViewport(new javafx.geometry.Rectangle2D(centerX, 0, imageHeight, imageHeight));
        }
        // If the image is taller than wide, crop the height to fit the width
        else if (imageHeight > imageWidth) {
            double centerY = (imageHeight - imageWidth) / 2;
            uploadedImageView.setViewport(new javafx.geometry.Rectangle2D(0, centerY, imageWidth, imageWidth));
        }
        // If the image is already a square, just show it as it is
        else {
            uploadedImageView.setViewport(new javafx.geometry.Rectangle2D(0, 0, imageWidth, imageHeight));
        }
    }

    @FXML
    private void handleShuffle() {
        System.out.println("Shuffle clicked");
        // Add shuffle logic
    }

    @FXML
    private void handleSolve() {
        System.out.println("Solve clicked");
        // Add solve logic
    }
}

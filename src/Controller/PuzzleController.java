package Controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class PuzzleController {


    @FXML private GridPane puzzleGrid;
    @FXML private ImageView backgroundImageView;
    @FXML private Button uploadButton;
    @FXML private Button shuffleButton;
    @FXML private Button solveButton;
    @FXML private Label solvedLabel;
    private ImageView[] puzzleTiles = new ImageView[9];
    private int blankTileIndex = 8;
    private Image originalImage;


    public void initialize(){
        initializePuzzleState();
    }

    public void initializePuzzleState(){

    }

    private void setupPuzzleWithImage(Image image) {
        Image croppedImage = getCroppedSquareImage(image);

        backgroundImageView.setImage(croppedImage);
        backgroundImageView.setFitWidth(365);
        backgroundImageView.setFitHeight(365);

        createTiles(croppedImage);
        displayTiles();
    }

    private Image getCroppedSquareImage(Image image) {
        double width = image.getWidth();
        double height = image.getHeight();
        double side = Math.min(width, height);

        double x = (width - side) / 2;
        double y = (height - side) / 2;

        ImageView imageView = new ImageView(image);
        imageView.setViewport(new Rectangle2D(x, y, side, side));

        SnapshotParameters params = new SnapshotParameters();
        WritableImage croppedImage = new WritableImage((int)side, (int)side);
        imageView.snapshot(params, croppedImage);

        return croppedImage;
    }

    @FXML
    private void handleUploadImage() {
        solvedLabel.setVisible(false);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                originalImage = new Image(new FileInputStream(selectedFile));
                setupPuzzleWithImage(originalImage);
                System.out.println("Image uploaded: " + selectedFile.getName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        shuffleButton.setVisible(true);
        solveButton.setVisible(true);

    }

    @FXML
    private void handleShuffle() {
        solvedLabel.setVisible(false);
        Random random = new Random();

        for (int i = 0; i < 100; i++) {
            List<Integer> adjacentIndices = getMovableIndices(blankTileIndex);
            int moveIndex = adjacentIndices.get(random.nextInt(adjacentIndices.size()));
            swapTiles(moveIndex, blankTileIndex);
            blankTileIndex = moveIndex;
        }
        displayTiles();
    }

    @FXML
    private void handleSolve() {
        puzzleGrid.setVisible(false);
        backgroundImageView.setOpacity(100);

        solvedLabel.setVisible(true);
    }

    private void createTiles(Image image) {
        int tileCount = 9;
        int tilesPerRow = 3;

        double tileWidth = image.getWidth() / tilesPerRow;
        double tileHeight = image.getHeight() / tilesPerRow;

        puzzleTiles = new ImageView[tileCount];

        for (int i = 0; i < tileCount; i++) {
            int row = i / tilesPerRow;
            int col = i % tilesPerRow;

            Rectangle2D viewport = new Rectangle2D(
                    col * tileWidth,
                    row * tileHeight,
                    tileWidth,
                    tileHeight
            );

            ImageView tileView = new ImageView(image);
            tileView.setViewport(viewport);
            tileView.setFitWidth(120);
            tileView.setFitHeight(120);
            tileView.setPreserveRatio(true);

            if (i == tileCount - 1) {
                tileView.setImage(null);
            }

            puzzleTiles[i] = tileView;
        }

        blankTileIndex = tileCount - 1;
        }

    private void displayTiles() {
        puzzleGrid.setVisible(true);
        backgroundImageView.setOpacity(0.2);
        puzzleGrid.getChildren().clear();

        for (int i = 0; i < puzzleTiles.length; i++) {
            int row = i / 3;
            int col = i % 3;

            ImageView tile = puzzleTiles[i];

            tile.setOnMouseClicked(event -> {
                int clickedIndex = Arrays.asList(puzzleTiles).indexOf(tile);
                if (isAdjacent(clickedIndex, blankTileIndex)) {
                    swapTiles(clickedIndex, blankTileIndex);
                    blankTileIndex = clickedIndex;
                    displayTiles();
                }
            });

            puzzleGrid.add(tile, col, row);
        }
    }

    private boolean isAdjacent(int i1, int i2){
        int row1 = i1 / 3, col1 = i1 % 3;
        int row2 = i2 / 3, col2 = i2 % 3;
        return (Math.abs(row1 - row2) == 1 && col1 == col2) ||
                (Math.abs(col1 - col2) == 1 && row1 == row2);
    }

    private void swapTiles(int i, int j) {
        ImageView temp = puzzleTiles[i];
        puzzleTiles[i] = puzzleTiles[j];
        puzzleTiles[j] = temp;
    }

    private List<Integer> getMovableIndices(int blankIndex){
        List<Integer> neighbors = new ArrayList<>();
        int row = blankTileIndex / 3;
        int col = blankTileIndex % 3;

        if (row > 0) neighbors.add(blankIndex - 3);
        if (row < 2) neighbors.add(blankIndex + 3);
        if (col > 0) neighbors.add(blankIndex - 1);
        if (col < 2) neighbors.add(blankIndex + 1);

        return neighbors;
    }
}

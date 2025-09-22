package Util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;

public class ImageUtils {
    public static ImageView[] sliceImageIntoTiles(Image image){
        int numRows = 3;
        int numCols = 3;

        int tileSize = (int) Math.min(image.getWidth(), image.getHeight()) / 3;
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ImageView[] tiles = new ImageView[9];

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int x = col * tileSize;
                int y = row * tileSize;
                BufferedImage tileImage = bufferedImage.getSubimage(x, y, tileSize, tileSize);
                ImageView tile = new ImageView(SwingFXUtils.toFXImage(tileImage, null));
                tile.setFitWidth(100);
                tile.setFitHeight(100);
                tile.setPreserveRatio(true);

                int index = row * 3 + col;
                tiles[index] = tile;
            }
        }
        tiles[8].setImage(null);
        return tiles;
    }
}

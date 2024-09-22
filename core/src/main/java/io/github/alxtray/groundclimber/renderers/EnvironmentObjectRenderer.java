package io.github.alxtray.groundclimber.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;

public class EnvironmentObjectRenderer implements EnvironmentObjectVisitor {
    private static final int TILE_SIZE = 18;
    private final Texture grassSingleLeftImage;
    private final Texture grassSingleMiddleImage;
    private final Texture grassSingleRightImage;
    private final Texture grassTopLeftImage;
    private final Texture grassTopMiddleImage;
    private final Texture grassTopRightImage;
    private final Texture groundLeftBottomImage;
    private final Texture groundLeftMiddleImage;
    private final Texture groundMiddleBottomImage;
    private final Texture groundNoBorderImage;
    private final Texture groundRightBottomImage;
    private final Texture groundRightMiddleImage;

    public EnvironmentObjectRenderer() {
        AssetLibrary assetLibrary = AssetLibrary.getInstance();
        grassSingleLeftImage = assetLibrary.getAsset("grass_single_left", Texture.class);
        grassSingleMiddleImage = assetLibrary.getAsset("grass_single_middle", Texture.class);
        grassSingleRightImage = assetLibrary.getAsset("grass_single_right", Texture.class);
        grassTopLeftImage = assetLibrary.getAsset("grass_top_left", Texture.class);
        grassTopMiddleImage = assetLibrary.getAsset("grass_top_middle", Texture.class);
        grassTopRightImage = assetLibrary.getAsset("grass_top_right", Texture.class);
        groundLeftBottomImage = assetLibrary.getAsset("ground_left_bottom", Texture.class);
        groundLeftMiddleImage = assetLibrary.getAsset("ground_left_middle", Texture.class);
        groundMiddleBottomImage = assetLibrary.getAsset("ground_middle_bottom", Texture.class);
        groundNoBorderImage = assetLibrary.getAsset("ground_no_border", Texture.class);
        groundRightBottomImage = assetLibrary.getAsset("ground_right_bottom", Texture.class);
        groundRightMiddleImage = assetLibrary.getAsset("ground_right_middle", Texture.class);
    }

    @Override
    public void visitPlatform(Platform platform, SpriteBatch batch) {
        Texture platformTileTexture = groundNoBorderImage;
        int rowTileNumber = 0;
        int columnTileNumber = 0;
        Vector2 platformPos = platform.getPosition();
        int numberOfRows = (int) platform.getWidth() / TILE_SIZE;
        int numberOfColumns = (int) platform.getHeight() / TILE_SIZE;
        for (float placementX = platformPos.x; placementX < platform.getWidth() + platformPos.x; placementX += TILE_SIZE) {
            rowTileNumber++;
            for (float placementY = platformPos.y; placementY < platform.getHeight() + platformPos.y; placementY += TILE_SIZE) {
                columnTileNumber++;
                // Minus half width and height as x and y used here is the adjusted version for Box2D
                if (columnTileNumber == numberOfColumns && rowTileNumber == 1) {
                    platformTileTexture = grassTopLeftImage;
                    if (numberOfColumns == 1) {
                        platformTileTexture = grassSingleLeftImage;
                    }
                } else if (columnTileNumber == numberOfColumns && rowTileNumber == numberOfRows) {
                    platformTileTexture = grassTopRightImage;
                    if (numberOfColumns == 1) {
                        platformTileTexture = grassSingleRightImage;
                    }
                } else if (columnTileNumber == numberOfColumns) {
                    platformTileTexture = grassTopMiddleImage;
                    if (numberOfColumns == 1) {
                        platformTileTexture = grassSingleMiddleImage;
                    }
                } else if (columnTileNumber == 1 && rowTileNumber == 1) {
                    platformTileTexture = groundLeftBottomImage;
                } else if (rowTileNumber == 1) {
                    platformTileTexture = groundLeftMiddleImage;
                } else if (columnTileNumber == 1 && rowTileNumber == numberOfRows) {
                    platformTileTexture = groundRightBottomImage;
                } else if (rowTileNumber == numberOfRows) {
                    platformTileTexture = groundRightMiddleImage;
                } else if (columnTileNumber == 1) {
                    platformTileTexture = groundMiddleBottomImage;
                }
                batch.draw(platformTileTexture, placementX - (platform.getWidth() / 2), placementY - (platform.getHeight() / 2));
                platformTileTexture = groundNoBorderImage;
            }
            columnTileNumber = 0;
        }
    }

}

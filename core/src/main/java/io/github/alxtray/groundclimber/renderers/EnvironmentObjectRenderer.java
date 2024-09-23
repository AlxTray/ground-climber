package io.github.alxtray.groundclimber.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;

public class EnvironmentObjectRenderer implements EnvironmentObjectVisitor {
    private static final int TILE_SIZE = 18;

    @Override
    public void visitPlatform(Platform platform, SpriteBatch batch) {
        Vector2 platformPosition = platform.getPosition();
        int numberOfRows = (int) platform.getWidth() / TILE_SIZE;
        int numberOfColumns = (int) platform.getHeight() / TILE_SIZE;
        int rowTileNumber = 0;
        int columnTileNumber = 0;
        for (float placementX = platformPosition.x; placementX < platform.getWidth() + platformPosition.x; placementX += TILE_SIZE) {
            rowTileNumber++;
            for (float placementY = platformPosition.y; placementY < platform.getHeight() + platformPosition.y; placementY += TILE_SIZE) {
                columnTileNumber++;

                String platformTileTexture = selectTileTexture(
                    columnTileNumber, numberOfColumns, rowTileNumber, numberOfRows);

                batch.draw(
                    AssetLibrary.getInstance().getAsset(platformTileTexture, Texture.class),
                    // Minus half width and height as x and y used here is the adjusted version for Box2D
                    placementX - (platform.getWidth() / 2), placementY - (platform.getHeight() / 2));
            }
            columnTileNumber = 0;
        }
    }

    private static String selectTileTexture(int columnTileNumber, int numberOfColumns, int rowTileNumber, int numberOfRows) {
        if (columnTileNumber == numberOfColumns && rowTileNumber == 1) {
            return (numberOfColumns == 1) ? "grass_single_left" : "grass_top_left";
        }
        if (columnTileNumber == numberOfColumns && rowTileNumber == numberOfRows) {
            return (numberOfColumns == 1) ? "grass_single_right" : "grass_top_right";
        }
        if (columnTileNumber == numberOfColumns) {
            return (numberOfColumns == 1) ? "grass_single_middle" : "grass_top_middle";
        }
        if (columnTileNumber == 1 && rowTileNumber == 1) return "ground_left_bottom";
        if (rowTileNumber == 1) return "ground_left_middle";
        if (columnTileNumber == 1 && rowTileNumber == numberOfRows) return "ground_right_bottom";
        if (rowTileNumber == numberOfRows) return "ground_right_middle";
        if (columnTileNumber == 1) return "ground_middle_bottom";

        return "ground_no_border";
    }

}

package io.github.alxtray.groundclimber.renderers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.alxtray.groundclimber.bodies.Platform;
import io.github.alxtray.groundclimber.enums.PlatformOrientation;
import io.github.alxtray.groundclimber.utilities.AssetLibrary;
import io.github.alxtray.groundclimber.visitors.EnvironmentObjectRenderVisitor;

public class EnvironmentObjectRenderer implements EnvironmentObjectRenderVisitor {
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
                    platform.getOrientation(), columnTileNumber, numberOfColumns, rowTileNumber, numberOfRows);

                // Minus half width and height as x and y used here is the adjusted version for Box2D
                float adjustedPlacementX = placementX - (platform.getWidth() / 2);
                float adjustedPlacementY = placementY - (platform.getHeight() / 2);
                drawRotatedPlatformTexture(
                    AssetLibrary.getInstance().getAsset(platformTileTexture, Texture.class),
                    platform,
                    adjustedPlacementX, adjustedPlacementY,
                    batch);

                Texture overlayTexture = platform.getOverlayTexture();
                if (overlayTexture != null) {
                    drawRotatedPlatformTexture(
                        overlayTexture,
                        platform,
                        adjustedPlacementX, adjustedPlacementY,
                        batch);
                }
            }
            columnTileNumber = 0;
        }
    }

    private static void drawRotatedPlatformTexture(Texture texture, Platform platform, float placementX, float placementY, SpriteBatch batch) {
        batch.draw(
            texture,
            placementX, placementY,
            TILE_SIZE / 2f, TILE_SIZE / 2f,
            TILE_SIZE, TILE_SIZE,
            1f, 1f,
            platform.getOrientation().getAngle(),
            0, 0,
            TILE_SIZE, TILE_SIZE,
            false, false);
    }

    private static String selectTileTexture(PlatformOrientation orientation, int columnTileNumber, int numberOfColumns, int rowTileNumber, int numberOfRows) {
        // Quite a horrible way of doing it but works well for now
        // Number of rows is basically far side of the platform and 1 is the near side
        // So changing what is assigned to each check variables to denote what should be placed where
        // Based on the "orientation" of the platform
        int rowTopLeftCheck = 1;
        int rowTopRightCheck = numberOfRows;
        int columnTopCheck = numberOfColumns;
        int rowGroundLeftCheck = 1;
        int rowGroundRightCheck = numberOfRows;
        int columnGroundCheck = 1;
        if (orientation == PlatformOrientation.SOUTH) {
            rowTopLeftCheck = numberOfRows;
            rowTopRightCheck = 1;
            columnTopCheck = 1;
            rowGroundLeftCheck = numberOfRows;
            rowGroundRightCheck = 1;
            columnGroundCheck = numberOfColumns;
        }

        if (columnTileNumber == columnTopCheck && rowTileNumber == rowTopLeftCheck) {
            return (numberOfColumns == 1) ? "grass_single_left" : "grass_top_left";
        }
        if (columnTileNumber == columnTopCheck && rowTileNumber == rowTopRightCheck) {
            return (numberOfColumns == 1) ? "grass_single_right" : "grass_top_right";
        }
        if (columnTileNumber == columnTopCheck) {
            return (numberOfColumns == 1) ? "grass_single_middle" : "grass_top_middle";
        }
        if (columnTileNumber == columnGroundCheck && rowTileNumber == rowGroundLeftCheck) return "ground_left_bottom";
        if (rowTileNumber == rowGroundLeftCheck) return "ground_left_middle";
        if (columnTileNumber == columnGroundCheck && rowTileNumber == rowGroundRightCheck) return "ground_right_bottom";
        if (rowTileNumber == rowGroundRightCheck) return "ground_right_middle";
        if (columnTileNumber == columnGroundCheck) return "ground_middle_bottom";

        return "ground_no_border";
    }

}

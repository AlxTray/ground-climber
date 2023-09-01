package com.alx;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.app.scene.Viewport;
import com.almasb.fxgl.dsl.views.ScrollingBackgroundView;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Map;

import static com.almasb.fxgl.dsl.FXGL.*;

/**
 * JavaFX App
 */
public class App extends GameApplication {

    private final int basePlayerSpeed = 100;
    private final double xAcceleratorMax = 2.2;
    private final double xAcceleratorModifier = 0.008;
    private final double xAcceleratorDefault = 1.0;

    private final double distanceJumpedDefault = 0.0;
    private final double yAcceleratorMax = 2.5;
    private final double yAcceleratorModifier = 0.02;
    private final double yAcceleratorDefault = 1.0;

    private final int appSizeHeight = 600;
    private final int startingAreaEndX = 700;

    private enum EntityType {
        PLAYER, PLATFORM, COIN, ENEMY, DEATH
    }

    private Entity player;
    private Viewport viewport;

    private ProceduralLevelFactory levelFactory;

    @Override
    protected void initSettings(GameSettings settings) {
        final int appSizeWidth = 800;
        settings.setWidth(appSizeWidth);
        settings.setHeight(appSizeHeight);
        settings.setTitle("Ground Climber");

        settings.setDeveloperMenuEnabled(true);
    }

    @Override
    protected void initGame() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GroundSensor",
                new Point2D(16, 38), BoundingShape.box(6, 8)));
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        final int playerSpawnX = 500;
        final int playerSpawnY = 500;
        final int playerWidth = 25;
        final int playerHeight = 25;
        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(playerSpawnX, playerSpawnY)
                .viewWithBBox(new Rectangle(playerWidth, playerHeight, Color.BLUE))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .buildAndAttach();

        levelFactory = new ProceduralLevelFactory();
        getGameWorld().addEntityFactory(levelFactory);
        levelFactory.spawnStartingPlatforms();
        levelFactory.spawnPlatformSet();

        viewport = getGameScene().getViewport();
        viewport.setBounds(-1500, 0, 250 * 70, getAppHeight());
        //viewport.bindToEntity(player, getAppWidth() / 2, getAppHeight() / 2);
    }

    @Override
    protected void initInput() {
        getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                double xAccelerator = getd("xAccelerator");
    
                player.getComponent(PlayerComponent.class).moveLeft(basePlayerSpeed * xAccelerator);
                if (xAccelerator < xAcceleratorMax) {
                    set("xAccelerator", xAccelerator + xAcceleratorModifier);
                }
            }
    
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopX();
                set("xAccelerator", xAcceleratorDefault);
            }
        }, KeyCode.A);
    
        getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                double xAccelerator = getd("xAccelerator");
    
                player.getComponent(PlayerComponent.class).moveRight(basePlayerSpeed * xAccelerator);
                if (xAccelerator < xAcceleratorMax) {
                    set("xAccelerator", xAccelerator + xAcceleratorModifier);
                }
            }
    
            @Override
            protected void onActionEnd() {
                player.getComponent(PlayerComponent.class).stopX();
                set("xAccelerator", xAcceleratorDefault);
            }
        }, KeyCode.D);
    
        getInput().addAction(new UserAction("Jump") {
            @Override
            protected void onAction() {
                double yAccelerator = getd("yAccelerator");
                if (yAccelerator < yAcceleratorMax) {
                    set("yAccelerator", yAccelerator + yAcceleratorModifier);
                }
            }
    
            @Override
            protected void onActionEnd() {
                set("jumping", true);
            }
        }, KeyCode.SPACE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("xAccelerator", xAcceleratorDefault);
        vars.put("yAccelerator", yAcceleratorDefault);
        vars.put("jumping", false);
        vars.put("distanceJumped", distanceJumpedDefault);
    }

    @Override
    protected void initUI() {
        Text text = new Text();
        text.setText("Ground Climber");
        text.setTranslateX(50);
        text.setTranslateY(100);

        entityBuilder()
            .view(new ScrollingBackgroundView(texture("tile.png").getImage(), getAppWidth(), getAppHeight()))
            .zIndex(-1)
            .with(new IrremovableComponent())
            .buildAndAttach(); 

        getGameScene().addUINode(text);
    }

    @Override
    protected void initPhysics() {
        double gravityDefault = 300;
        getPhysicsWorld().setGravity(0, gravityDefault);
    }

    @Override
    protected void onUpdate(double tpf) {
        /* TODO: Instead of closing, if on start screen respawn, if started then restart */
        if (player.getY() > appSizeHeight) {
            Stage fxglStage = (Stage) getGameScene().getRoot().getScene().getWindow();
            fxglStage.close();
        }

        if (player.getX() > startingAreaEndX) {
            viewport.setX(viewport.getX() + 1);
        }

        /* TODO: Add check using GroundSensor to see if player is not on ground and then return */
        if (!getb("jumping")) return;

        if ((levelFactory.getLastInSetX() - player.getX()) < 800 && (levelFactory.getLastInSetX() - player.getX()) > 0) {
            levelFactory.spawnPlatformSet();
        }

        final double yAccelerator = getd("yAccelerator");
        final int jumpHeightInterval = 100;
        final double amountToJump = jumpHeightInterval * yAccelerator;
        player.getComponent(PlayerComponent.class).jump(amountToJump);
        inc("distanceJumped", amountToJump);

        final int baseJumpHeight = 5_000;
        if (getd("distanceJumped") >= baseJumpHeight * yAccelerator) {
            player.getComponent(PlayerComponent.class).stopY();
            set("jumping", false);
            set("yAccelerator", yAcceleratorDefault);
            set("distanceJumped", distanceJumpedDefault);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
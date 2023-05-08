package com.alx;

import static com.almasb.fxgl.dsl.FXGL.*;

import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.CollidableComponent;
import com.almasb.fxgl.entity.components.IrremovableComponent;
import com.almasb.fxgl.input.Input;
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

/**
 * JavaFX App
 */
public class App extends GameApplication {

    private final int basePlayerSpeed = 100;
    private final double xAcceleratorMax = 2.2;
    private final double xAcceleratorModifier = 0.008;
    private final double xAcceleratorDefault = 1.0;

    private final int jumpHeightInterval = 100;
    private final int baseJumpHeight = 5000;
    private final double distanceJumpedDefault = 0.0;
    private final double yAcceleratorMax = 2.5;
    private final double yAcceleratorModifier = 0.02;
    private final double yAcceleratorDefault = 1.0;

    private final double gravityDefault = 300;

    private final int appSizeHeight = 600;
    private final int appSizeWidth = 800;

    private int playerSpawnX = 500;
    private int playerSpawnY = 500;
    private final int playerWidth = 25;
    private final int playerHeight = 25;

    private PropertyMap gameVarsMap;

    private enum EntityType {
        PLAYER, PLATFORM, COIN, ENEMY, DEATH
    }

    private Entity player;

    private UserAction moveLeft = new UserAction("Move Left") {
        @Override
        protected void onAction() {
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.getComponent(PlayerComponent.class).moveLeft(basePlayerSpeed * xAccelerator);
            if (xAccelerator < xAcceleratorMax) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + xAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            player.getComponent(PlayerComponent.class).stopX();
            gameVarsMap.setValue("xAccelerator", xAcceleratorDefault);
        }
    };

    private UserAction moveRight = new UserAction("Move Right") {
        @Override
        protected void onAction() {
            double xAccelerator = gameVarsMap.getDouble("xAccelerator");

            player.getComponent(PlayerComponent.class).moveRight(basePlayerSpeed * xAccelerator);
            if (xAccelerator < xAcceleratorMax) {
                gameVarsMap.setValue("xAccelerator", xAccelerator + xAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            player.getComponent(PlayerComponent.class).stopX();
            gameVarsMap.setValue("xAccelerator", xAcceleratorDefault);
        }
    };

    private UserAction jump = new UserAction("Jump") {
        @Override
        protected void onAction() {
            double yAccelerator = gameVarsMap.getDouble("yAccelerator");
            if (yAccelerator < yAcceleratorMax) {
                gameVarsMap.setValue("yAccelerator", yAccelerator + yAcceleratorModifier);
            }
        }

        @Override
        protected void onActionEnd() {
            gameVarsMap.setValue("jumping", true);
        }
    };

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(appSizeWidth);
        settings.setHeight(appSizeHeight);
        settings.setTitle("Ground Climber");

        settings.setDeveloperMenuEnabled(true);
    }

    @Override
    protected void initGame() {
        PhysicsComponent physics = new PhysicsComponent();
        physics.setBodyType(BodyType.DYNAMIC);
        physics.addGroundSensor(new HitBox("GroundSensor", new Point2D(0, 0), BoundingShape.box(1, 1)));
        physics.setFixtureDef(new FixtureDef().friction(0.0f));

        player = entityBuilder()
                .type(EntityType.PLAYER)
                .at(playerSpawnX, playerSpawnY)
                .viewWithBBox(new Rectangle(playerWidth, playerHeight, Color.BLUE))
                .with(physics)
                .with(new CollidableComponent(true))
                .with(new IrremovableComponent())
                .with(new PlayerComponent())
                .buildAndAttach();

        getGameWorld().addEntityFactory(new ProceduralLevelFactory());
        ProceduralLevelFactory levelFactory = new ProceduralLevelFactory();
        levelFactory.spawnStartingPlatforms();
    }

    @Override
    protected void initInput() {
        Input input = getInput();

        input.addAction(moveLeft, KeyCode.A);
        input.addAction(moveRight, KeyCode.D);
        input.addAction(jump, KeyCode.SPACE);
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        vars.put("xAccelerator", xAcceleratorDefault);
        vars.put("yAccelerator", yAcceleratorDefault);
        vars.put("jumping", false);
        vars.put("distanceJumped", distanceJumpedDefault);

        gameVarsMap = getWorldProperties();
    }

    @Override
    protected void initUI() {
        Text text = new Text();
        text.setText("Ground Climber");
        text.setTranslateX(50);
        text.setTranslateY(100);
    
        getGameScene().addUINode(text);
    }

    @Override
    protected void initPhysics() {
        getPhysicsWorld().setGravity(0, gravityDefault);
    }

    @Override
    protected void onUpdate(double tpf) {
        if (!gameVarsMap.getBoolean("jumping")) {
            return;
        }

        final double yAccelerator = gameVarsMap.getDouble("yAccelerator");
        final double amountToJump = jumpHeightInterval * yAccelerator;
        player.getComponent(PlayerComponent.class).jump(amountToJump);
        inc("distanceJumped", amountToJump);
        if (gameVarsMap.getDouble("distanceJumped") >= baseJumpHeight * yAccelerator) {
            player.getComponent(PlayerComponent.class).stopY();
            gameVarsMap.setValue("jumping", false);
            gameVarsMap.setValue("yAccelerator", yAcceleratorDefault);
            gameVarsMap.setValue("distanceJumped", distanceJumpedDefault);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
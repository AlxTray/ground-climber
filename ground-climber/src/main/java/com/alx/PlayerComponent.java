package com.alx;

import static com.almasb.fxgl.dsl.FXGL.*;

import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;

public class PlayerComponent extends Component {
    
    public PhysicsComponent physics;

    public PlayerComponent() {
    }

    public void moveLeft(double speed) {
        physics.setVelocityX(-speed);
    }

    public void moveRight(double speed) {
        physics.setVelocityX(speed);
    }

    public void stopX() {
        physics.setVelocityX(0);
    }

    public void jump(double speed) {
        physics.setVelocityY(-speed);
    }

    public void stopY() {
        physics.setVelocityY(0);
    }

}

package com.alx.groundclimber.bodies;

import com.badlogic.gdx.physics.box2d.World;

public class CrackedPlatform extends Platform {

    int crackLevel = 0;

    public CrackedPlatform(World world, float x, float y, float height, float width) {
        super(world, x, y, height, width);
        this.body.setUserData(this);
    }

    public void incrementCrackLevel() {
        crackLevel++;
    }

    public int getCrackLevel() {
        return crackLevel;
    }

}

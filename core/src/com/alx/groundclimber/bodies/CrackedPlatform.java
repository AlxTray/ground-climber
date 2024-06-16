package com.alx.groundclimber.bodies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.World;

public class CrackedPlatform extends Platform {

    int crackLevel = 0;

    public CrackedPlatform(World world, float x, float y, float height, float width) {
        super(world, x, y, height, width);
        this.body.setUserData(this);
    }

    public void incrementCrackLevel() {
        crackLevel++;
        Gdx.app.debug("Cracked Platform DEBUG", "Cracked level for platform at (" + this.getX() + ", " + this.getY() + ") is " + this.crackLevel);
    }

    public int getCrackLevel() {
        return crackLevel;
    }

}

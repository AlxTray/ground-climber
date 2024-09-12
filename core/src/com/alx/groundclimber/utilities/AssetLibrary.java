package com.alx.groundclimber.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class AssetLibrary {
    
    private AssetManager assetManager;

    private AssetLibrary() {
    }
    
    private static class LazyHolder {
        private static final AssetLibrary INSTANCE = new AssetLibrary();
    }
    
    public static AssetLibrary getInstance() {
        return LazyHolder.INSTANCE;
    }
    
    public void init() {
        assetManager = new AssetManager();
        loadInitialAssets();
    }
    
    private void loadInitialAssets() {
        SkinLoader.SkinParameter parameter = new SkinLoader.SkinParameter("skin/uiskin.atlas");
        assetManager.load("skin/uiskin.json", Skin.class, parameter);
    }
    
    
    public AssetManager getAssetManager() {
        return assetManager;
    }
    
}

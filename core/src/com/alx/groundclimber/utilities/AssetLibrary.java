package com.alx.groundclimber.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetLibrary {
    
    private AssetManager assetManager;
    private ObjectMap<String, String> filePathAliases;
    
    private String skinAtlasPath = "skin/uiskin.atlas";
    private String skinJsonPath = "skin/uiskin.json";
    private String playerImagePath = "player.png";
    private String playerFaceImagePath = "player_face.png";
    
    private String platformTilePath = "platform.png";
    private String backgroundImagePath = "background.png";

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
        filePathAliases = new ObjectMap<>();
        loadInitialAssets();
    }
    
    private void loadInitialAssets() {
        SkinLoader.SkinParameter parameter = new SkinLoader.SkinParameter(skinAtlasPath);
        assetManager.load(skinJsonPath, Skin.class, parameter);
        filePathAliases.put("skin", skinJsonPath);
        
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        assetManager.load(playerImagePath, Texture.class, textureParameter);
        filePathAliases.put("player", playerImagePath);
        assetManager.load(playerFaceImagePath, Texture.class, textureParameter);
        filePathAliases.put("playerFace", playerFaceImagePath);
    }
    
    
    public void loadGeneralLevelAssets() {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        assetManager.load(backgroundImagePath, Texture.class, textureParameter);
        filePathAliases.put("background", backgroundImagePath);
        assetManager.load(platformTilePath, Texture.class, textureParameter);
        filePathAliases.put("platformTile", platformTilePath);
    }
    
    
    public <T> T getAsset(String assetAlias, Class<T> type) {
        String assetPath = filePathAliases.get(assetAlias);
        if (assetPath == null) {
            throw new IllegalArgumentException("Asset '" + assetAlias + "' not found");
        }
        return assetManager.get(assetPath, type);
    }
    
    public AssetManager getAssetManager() {
        return assetManager;
    }
    
}

package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetLibrary {

    private static final String SKIN_ATLAS_PATH = "ui/skin.atlas";
    private static final String SKIN_JSON_PATH = "ui/skin.json";
    private static final String PLAYER_IMAGE_PATH = "player.png";
    private static final String PLAYER_FACE_IMAGE_PATH = "player_face.png";
    private static final String PLATFORM_TILE_PATH = "platform.png";
    private static final String BACKGROUND_IMAGE_PATH = "background.png";
    private static final String TITLE_TEXT_PATH = "title_text.png";
    private static final String TITLE_BACKGROUND_IMAGE_PATH = "title_background.png";
    private AssetManager assetManager;
    private ObjectMap<String, String> filePathAliases;

    private AssetLibrary() {
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
        SkinLoader.SkinParameter parameter = new SkinLoader.SkinParameter(SKIN_ATLAS_PATH);
        assetManager.load(SKIN_JSON_PATH, Skin.class, parameter);
        filePathAliases.put("skin", SKIN_JSON_PATH);

        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        assetManager.load(TITLE_TEXT_PATH, Texture.class, textureParameter);
        filePathAliases.put("title", TITLE_TEXT_PATH);
        assetManager.load(TITLE_BACKGROUND_IMAGE_PATH, Texture.class, textureParameter);
        filePathAliases.put("title_background", TITLE_BACKGROUND_IMAGE_PATH);
        assetManager.load(PLAYER_IMAGE_PATH, Texture.class, textureParameter);
        filePathAliases.put("player", PLAYER_IMAGE_PATH);
        assetManager.load(PLAYER_FACE_IMAGE_PATH, Texture.class, textureParameter);
        filePathAliases.put("playerFace", PLAYER_FACE_IMAGE_PATH);
    }

    public void loadGeneralLevelAssets() {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        assetManager.load(BACKGROUND_IMAGE_PATH, Texture.class, textureParameter);
        filePathAliases.put("background", BACKGROUND_IMAGE_PATH);
        assetManager.load(PLATFORM_TILE_PATH, Texture.class, textureParameter);
        filePathAliases.put("platformTile", PLATFORM_TILE_PATH);
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

    private static class LazyHolder {
        private static final AssetLibrary INSTANCE = new AssetLibrary();
    }

}

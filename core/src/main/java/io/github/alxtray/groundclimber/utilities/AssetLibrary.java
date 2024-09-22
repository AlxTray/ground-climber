package io.github.alxtray.groundclimber.utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class AssetLibrary {
    private static final String SKIN_ATLAS_PATH = "ui/skin.atlas";
    private static final String SKIN_JSON_PATH = "ui/skin.json";
    private AssetManager assetManager;
    private FileHandle assetsFile;
    private ObjectMap<String, String> filePathAliases;

    private AssetLibrary() {
    }

    public static AssetLibrary getInstance() {
        return LazyHolder.INSTANCE;
    }

    public void init() {
        assetManager = new AssetManager();
        filePathAliases = new ObjectMap<>();
        assetsFile = Gdx.files.internal("assets.txt");
        loadInitialAssets();
    }

    private void loadInitialAssets() {
        SkinLoader.SkinParameter parameter = new SkinLoader.SkinParameter(SKIN_ATLAS_PATH);
        assetManager.load(SKIN_JSON_PATH, Skin.class, parameter);
        filePathAliases.put("skin", SKIN_JSON_PATH);

        Array<String> assetPaths = getAssetPaths("initial");
        loadTextureAssets(assetPaths);
    }

    public void loadGeneralLevelAssets() {
        Array<String> assetPaths = getAssetPaths("level/");
        loadTextureAssets(assetPaths);
    }

    public <T> T getAsset(String assetAlias, Class<T> type) {
        String assetPath = filePathAliases.get(assetAlias);
        if (assetPath == null) {
            throw new IllegalArgumentException("Asset '" + assetAlias + "' not found");
        }
        return assetManager.get(assetPath, type);
    }

    private Array<String> getAssetPaths(String matchingSequence) {
        Array<String> assetPaths = new Array<>();
        assetsFile.readString().lines()
            .filter(line -> line.contains(matchingSequence))
            .forEach(assetPaths::add);
        return assetPaths;
    }

    private void loadTextureAssets(Array<String> assetPaths) {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        for (String assetPath : assetPaths) {
            assetManager.load(assetPath, Texture.class, textureParameter);
            filePathAliases.put(
                assetPath.substring(assetPath.lastIndexOf("/") + 1, assetPath.lastIndexOf(".")),
                assetPath);
        }
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    private static class LazyHolder {
        private static final AssetLibrary INSTANCE = new AssetLibrary();
    }

}

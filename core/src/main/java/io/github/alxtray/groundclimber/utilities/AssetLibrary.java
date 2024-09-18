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
    private static final String GRASS_SINGLE_LEFT_PATH = "grass_single_left.png";
    private static final String GRASS_SINGLE_MIDDLE_PATH = "grass_single_middle.png";
    private static final String GRASS_SINGLE_RIGHT_PATH = "grass_single_right.png";
    private static final String GRASS_TOP_LEFT_PATH = "grass_top_left.png";
    private static final String GRASS_TOP_MIDDLE_PATH = "grass_top_middle.png";
    private static final String GRASS_TOP_RIGHT_PATH = "grass_top_right.png";
    private static final String GROUND_LEFT_BOTTOM_PATH = "ground_left_bottom.png";
    private static final String GROUND_LEFT_MIDDLE_PATH = "ground_left_middle.png";
    private static final String GROUND_MIDDLE_BOTTOM_PATH = "ground_middle_bottom.png";
    private static final String GROUND_RIGHT_BOTTOM_PATH = "ground_right_bottom.png";
    private static final String GROUND_RIGHT_MIDDLE_PATH = "ground_right_middle.png";
    private static final String GROUND_NO_BORDER_PATH = "ground_no_border.png";
    private static final String BACKGROUND_IMAGE_PATH = "background.png";
    private static final String TITLE_TEXT_PATH = "title_text.png";
    private static final String TITLE_BACKGROUND_IMAGE_PATH = "title_background.png";
    private static final String LEVEL_TEXT_PATH = "select_level_title.png";
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
        assetManager.load(LEVEL_TEXT_PATH, Texture.class, textureParameter);
        filePathAliases.put("level_title", LEVEL_TEXT_PATH);
        assetManager.load(PLAYER_IMAGE_PATH, Texture.class, textureParameter);
        filePathAliases.put("player", PLAYER_IMAGE_PATH);
        assetManager.load(PLAYER_FACE_IMAGE_PATH, Texture.class, textureParameter);
        filePathAliases.put("playerFace", PLAYER_FACE_IMAGE_PATH);
    }

    public void loadGeneralLevelAssets() {
        TextureLoader.TextureParameter textureParameter = new TextureLoader.TextureParameter();
        assetManager.load(BACKGROUND_IMAGE_PATH, Texture.class, textureParameter);
        filePathAliases.put("background", BACKGROUND_IMAGE_PATH);
        assetManager.load(GRASS_SINGLE_LEFT_PATH, Texture.class, textureParameter);
        filePathAliases.put("grass_single_left", GRASS_SINGLE_LEFT_PATH);
        assetManager.load(GRASS_SINGLE_MIDDLE_PATH, Texture.class, textureParameter);
        filePathAliases.put("grass_single_middle", GRASS_SINGLE_MIDDLE_PATH);
        assetManager.load(GRASS_SINGLE_RIGHT_PATH, Texture.class, textureParameter);
        filePathAliases.put("grass_single_right", GRASS_SINGLE_RIGHT_PATH);
        assetManager.load(GRASS_TOP_LEFT_PATH, Texture.class, textureParameter);
        filePathAliases.put("grass_top_left", GRASS_TOP_LEFT_PATH);
        assetManager.load(GRASS_TOP_MIDDLE_PATH, Texture.class, textureParameter);
        filePathAliases.put("grass_top_middle", GRASS_TOP_MIDDLE_PATH);
        assetManager.load(GRASS_TOP_RIGHT_PATH, Texture.class, textureParameter);
        filePathAliases.put("grass_top_right", GRASS_TOP_RIGHT_PATH);
        assetManager.load(GROUND_LEFT_BOTTOM_PATH, Texture.class, textureParameter);
        filePathAliases.put("ground_left_bottom", GROUND_LEFT_BOTTOM_PATH);
        assetManager.load(GROUND_LEFT_MIDDLE_PATH, Texture.class, textureParameter);
        filePathAliases.put("ground_left_middle", GROUND_LEFT_MIDDLE_PATH);
        assetManager.load(GROUND_MIDDLE_BOTTOM_PATH, Texture.class, textureParameter);
        filePathAliases.put("ground_middle_bottom", GROUND_MIDDLE_BOTTOM_PATH);
        assetManager.load(GROUND_NO_BORDER_PATH, Texture.class, textureParameter);
        filePathAliases.put("ground_no_border", GROUND_NO_BORDER_PATH);
        assetManager.load(GROUND_RIGHT_BOTTOM_PATH, Texture.class, textureParameter);
        filePathAliases.put("ground_right_bottom", GROUND_RIGHT_BOTTOM_PATH);
        assetManager.load(GROUND_RIGHT_MIDDLE_PATH, Texture.class, textureParameter);
        filePathAliases.put("ground_right_middle", GROUND_RIGHT_MIDDLE_PATH);
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

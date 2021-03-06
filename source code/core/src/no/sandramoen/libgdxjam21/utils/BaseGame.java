package no.sandramoen.libgdxjam21.utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.I18NBundle;

public abstract class BaseGame extends Game implements AssetErrorListener {
    private static BaseGame game;

    public static AssetManager assetManager;
    public static final float WORLD_WIDTH = 100.0F;
    public static final float WORLD_HEIGHT = 100.0F;
    public static final float scale = 1.0F;
    private static float RATIO;
    private static boolean isCustomShadersEnabled = true;

    // game assets
    public static LabelStyle label36Style;
    public static LabelStyle label26Style;
    public static TextureAtlas textureAtlas;
    private static Skin skin;

    public static Music levelMusic1;
    public static Music levelMusic2;
    public static Music gallopSoundMusic;
    public static Music breakingSoundMusic;
    public static Music introMusic;
    public static Music introVoice;

    public static Sound armor1Sound;
    public static Sound armor2Sound;
    public static Sound armor3Sound;
    public static Sound armor4Sound;
    public static Sound armor5Sound;
    public static Sound pig1Sound;
    public static Sound pig2Sound;
    public static Sound pig3Sound;
    public static Sound enemySpawnSound;
    public static Sound dragonRoarSound;
    public static Sound hurtSound;
    public static Sound wingFlapSound;
    public static Sound invulnerableSound;
    public static Sound noHitSound;
    public static Sound glassHitSound;
    public static Sound jamPickupSound;
    public static Sound clickSound;
    public static Sound swordSlashSound;
    public static Sound bridgeBreakSound;
    public static Sound jamDrownSound;
    public static Sound bubblesSound;

    public static TiledMap level1Map;

    private static TextButtonStyle textButtonStyle;

    // game state
    public static Preferences prefs;
    public static boolean loadPersonalParameters;
    public static float soundVolume = 1f;
    public static float musicVolume = 0.3f;
    public static String currentLocale;
    public static I18NBundle myBundle;
    public static float unitScale = .17f;

    public BaseGame() {
        game = this;
    }

    public static void setActiveScreen(BaseScreen screen) {
        game.setScreen(screen);
    }

    public void create() {
        Gdx.input.setInputProcessor(new InputMultiplexer());

        assetManager();

        label36Style = new LabelStyle();
        BitmapFont myFont = new BitmapFont(Gdx.files.internal("fonts/arcadeRounded.fnt"));
        label36Style.font = myFont;

        label26Style = new LabelStyle();
        BitmapFont myFont2 = new BitmapFont(Gdx.files.internal("fonts/arcade26.fnt"));
        label26Style.font = myFont2;

        /*if (Gdx.app.getType() != Application.ApplicationType.Android) {
            Gdx.graphics.setCursor(Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal("images/excluded/cursor.png")), 0, 0));
        }*/
    }

    public void dispose() {
        super.dispose();
        try {
            assetManager.dispose();
        } catch (Error error) {
            Gdx.app.error(this.getClass().getSimpleName(), error.toString());
        }
    }

    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(this.getClass().getSimpleName(), "Could not load asset: " + asset.fileName, throwable);
    }

    private void assetManager() {
        long startTime = System.currentTimeMillis();
        assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.load("images/included/packed/images.pack.atlas", TextureAtlas.class);

        // music
        assetManager.load("audio/music/99567__shnitzelkiller__cammipple.mp3", Music.class);
        assetManager.load("audio/music/94873__shnitzelkiller__surpni.mp3", Music.class);
        assetManager.load("audio/sound/breaking.wav", Music.class);
        assetManager.load("audio/music/433563__burghrecords__cinematic-impact-intro-01.wav", Music.class);
        assetManager.load("audio/music/intro voice.wav", Music.class);

        // sound
        assetManager.load("audio/sound/armor1.wav", Sound.class);
        assetManager.load("audio/sound/armor2.wav", Sound.class);
        assetManager.load("audio/sound/armor3.wav", Sound.class);
        assetManager.load("audio/sound/armor4.wav", Sound.class);
        assetManager.load("audio/sound/armor5.wav", Sound.class);
        assetManager.load("audio/sound/gallop.wav", Music.class);
        assetManager.load("audio/sound/pig1.wav", Sound.class);
        assetManager.load("audio/sound/pig2.wav", Sound.class);
        assetManager.load("audio/sound/pig3.wav", Sound.class);
        assetManager.load("audio/sound/enemySpawn.wav", Sound.class);
        assetManager.load("audio/sound/85568__joelaudio__dragon-roar.wav", Sound.class);
        assetManager.load("audio/sound/Hit_Hurt29.wav", Sound.class);
        assetManager.load("audio/sound/wing flap.wav", Sound.class);
        assetManager.load("audio/sound/invulnerable.wav", Sound.class);
        assetManager.load("audio/sound/no hit.wav", Sound.class);
        assetManager.load("audio/sound/244193__andre-nascimento__empty-glass-bottle-rolling-on-floor.wav", Sound.class);
        assetManager.load("audio/sound/Pickup_Coin24.wav", Sound.class);
        assetManager.load("audio/sound/Blip_Select6.wav", Sound.class);
        assetManager.load("audio/sound/574821__wesleyextreme-gamer__slash1.ogg", Sound.class);
        assetManager.load("audio/sound/Explosion2.wav", Sound.class);
        assetManager.load("audio/sound/237924__foolboymedia__splat-and-crunch.wav", Sound.class);
        assetManager.load("audio/sound/398808__inspectorj__bubbling-large-a.wav", Sound.class);

        // tiled maps
        assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
        assetManager.load("maps/level1.tmx", TiledMap.class);

        assetManager.finishLoading();

        // music
        levelMusic1 = assetManager.get("audio/music/99567__shnitzelkiller__cammipple.mp3", Music.class);
        levelMusic2 = assetManager.get("audio/music/94873__shnitzelkiller__surpni.mp3", Music.class);
        breakingSoundMusic = assetManager.get("audio/sound/breaking.wav", Music.class);
        introMusic = assetManager.get("audio/music/433563__burghrecords__cinematic-impact-intro-01.wav", Music.class);
        introVoice = assetManager.get("audio/music/intro voice.wav", Music.class);

        // sound
        armor1Sound = assetManager.get("audio/sound/armor1.wav", Sound.class);
        armor2Sound = assetManager.get("audio/sound/armor2.wav", Sound.class);
        armor3Sound = assetManager.get("audio/sound/armor3.wav", Sound.class);
        armor4Sound = assetManager.get("audio/sound/armor4.wav", Sound.class);
        armor5Sound = assetManager.get("audio/sound/armor5.wav", Sound.class);
        gallopSoundMusic = assetManager.get("audio/sound/gallop.wav", Music.class);
        pig1Sound = assetManager.get("audio/sound/pig1.wav", Sound.class);
        pig2Sound = assetManager.get("audio/sound/pig2.wav", Sound.class);
        pig3Sound = assetManager.get("audio/sound/pig3.wav", Sound.class);
        enemySpawnSound = assetManager.get("audio/sound/enemySpawn.wav", Sound.class);
        dragonRoarSound = assetManager.get("audio/sound/85568__joelaudio__dragon-roar.wav", Sound.class);
        hurtSound = assetManager.get("audio/sound/Hit_Hurt29.wav", Sound.class);
        wingFlapSound = assetManager.get("audio/sound/wing flap.wav", Sound.class);
        invulnerableSound = assetManager.get("audio/sound/invulnerable.wav", Sound.class);
        noHitSound = assetManager.get("audio/sound/no hit.wav", Sound.class);
        glassHitSound = assetManager.get("audio/sound/244193__andre-nascimento__empty-glass-bottle-rolling-on-floor.wav", Sound.class);
        jamPickupSound = assetManager.get("audio/sound/Pickup_Coin24.wav", Sound.class);
        clickSound = assetManager.get("audio/sound/Blip_Select6.wav", Sound.class);
        swordSlashSound = assetManager.get("audio/sound/574821__wesleyextreme-gamer__slash1.ogg", Sound.class);
        bridgeBreakSound = assetManager.get("audio/sound/Explosion2.wav", Sound.class);
        jamDrownSound = assetManager.get("audio/sound/237924__foolboymedia__splat-and-crunch.wav", Sound.class);
        bubblesSound = assetManager.get("audio/sound/398808__inspectorj__bubbling-large-a.wav", Sound.class);

        // tiled maps
        level1Map = assetManager.get("maps/level1.tmx", TiledMap.class);

        textureAtlas = assetManager.get("images/included/packed/images.pack.atlas");
        printLoadingTime(startTime);
    }

    private void printLoadingTime(long startTime) {
        long endTime = System.currentTimeMillis();
        Gdx.app.error(this.getClass().getSimpleName(), "Asset manager took " + (endTime - startTime) + " ms to load all game assets.");
    }
}

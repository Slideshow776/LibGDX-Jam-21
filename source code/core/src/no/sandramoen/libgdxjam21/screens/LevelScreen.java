package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

import no.sandramoen.libgdxjam21.actors.Background;
import no.sandramoen.libgdxjam21.actors.Enemy;
import no.sandramoen.libgdxjam21.actors.Impassable;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.actors.SpawnPoints;
import no.sandramoen.libgdxjam21.actors.TilemapActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.BaseScreen;
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class LevelScreen extends BaseScreen {
    private Player player;
    private Background background;
    private TilemapActor tilemap;

    @Override
    public void initialize() {
        background = new Background(0, 0, mainStage);
        tilemap = new TilemapActor(BaseGame.level1Map, mainStage);

        initializePlayer();
        initializeImpassables();
        initializeSpawnPoints();
        new Enemy(5, 5, mainStage, world);

        /*GameUtils.playLoopingMusic(BaseGame.levelMusic1);*/
        GameUtils.playLoopingMusic(BaseGame.gallopSoundMusic, 0f);
    }

    @Override
    public void update(float delta) {
        background.act();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) Gdx.app.exit();
        else if (keycode == Input.Keys.R) BaseGame.setActiveScreen(new LevelScreen());
        if (keycode == Input.Keys.SPACE) player.flapWings();
        return super.keyDown(keycode);
    }

    private void initializePlayer() {
        MapObject startPoint = tilemap.getTileList("player start").get(0);
        float x = (float) startPoint.getProperties().get("x") * BaseGame.unitScale;
        float y = (float) startPoint.getProperties().get("y") * BaseGame.unitScale;
        player = new Player(x, y, mainStage, world);
    }

    private void initializeImpassables() {
        for (MapObject obj : tilemap.getTileList("impassable")) {
            MapProperties props = obj.getProperties();
            float x = (Float) props.get("x") * BaseGame.unitScale;
            float y = (Float) props.get("y") * BaseGame.unitScale;
            float width = (Float) props.get("width") * BaseGame.unitScale;
            float height = (Float) props.get("height") * BaseGame.unitScale;
            new Impassable(x, y, width, height, mainStage, world);
        }
    }

    private void initializeSpawnPoints() {
        for (MapObject obj : tilemap.getTileList("spawn")) {
            MapProperties props = obj.getProperties();
            float x = (Float) props.get("x") * BaseGame.unitScale;
            float y = (Float) props.get("y") * BaseGame.unitScale;
            float width = (Float) props.get("width") * BaseGame.unitScale;
            float height = (Float) props.get("height") * BaseGame.unitScale;
            new SpawnPoints(x, y, width, height, mainStage, world);
        }
    }
}

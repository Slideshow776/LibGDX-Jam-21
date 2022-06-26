package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import no.sandramoen.libgdxjam21.actors.Background;
import no.sandramoen.libgdxjam21.actors.Enemy;
import no.sandramoen.libgdxjam21.actors.Jam;
import no.sandramoen.libgdxjam21.actors.Map.Bridge;
import no.sandramoen.libgdxjam21.actors.Map.Impassable;
import no.sandramoen.libgdxjam21.actors.Map.Lava;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.actors.Map.Roof;
import no.sandramoen.libgdxjam21.actors.Map.SpawnPoints;
import no.sandramoen.libgdxjam21.actors.Map.TilemapActor;
import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.BaseScreen;
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class BaseLevel extends BaseScreen {
    Player player;
    Background background;
    Array<Enemy> enemies;
    Array<Jam> jams;
    float time = 0;
    TypingLabel levelLabel;
    TypingLabel lifeLabel;
    TypingLabel scoreLabel;
    Lava lava;

    private TilemapActor tilemap;
    private Array<SpawnPoints> spawnPoints;
    private Array<Bridge> bridges;
    private boolean isGameOver = false;
    private int score = 0;

    public BaseLevel(Player player, Background background) {
        if (player != null) {
            this.player.body.setTransform(player.body.getPosition().x, player.body.getPosition().y, player.body.getAngle());
            this.player.lives = player.lives;
            this.background.background1.setX(background.background1.getX());
            this.background.background2.setX(background.background2.getX());
            this.background.background3.setX(background.background3.getX());
            this.background.background4.setX(background.background4.getX());
            this.background.background5.setX(background.background5.getX());
            this.background.background6.setX(background.background6.getX());
        }
    }

    @Override
    public void initialize() {
        background = new Background(-10, -1, mainStage);
        lava = new Lava(0f, -17f, mainStage, world);
        tilemap = new TilemapActor(BaseGame.level1Map, mainStage);
        jams = new Array<>();

        initializeImpassables();
        initializeSpawnPoints();
        initializeBridges();
        initializeRoof();
        initializePlayer();
        initializeUI();

        GameUtils.playLoopingMusic(BaseGame.levelMusic1);
        GameUtils.playLoopingMusic(BaseGame.gallopSoundMusic, 0f);
        GameUtils.playLoopingMusic(BaseGame.breakingSoundMusic, 0f);
    }

    @Override
    public void update(float delta) {
        if (isGameOver) return;
        if (player.lives <= -1 && !player.death)
            gameOver();

        if (time < 10f)
            time += delta;

        background.act();

        if (player.respawn) {
            player.respawn(getRandomSpawnPoint());
            if (player.lives > -1)
                lifeLabel.setText("{COLOR=#be772b}Life: " + player.lives);
        }

        handleEnemies();
        handleJams();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) Gdx.app.exit();
        else if (keycode == Input.Keys.R) BaseGame.setActiveScreen(new Level1Screen(null, null));
        if (keycode == Input.Keys.SPACE) player.flapWings();
        return super.keyDown(keycode);
    }

    private void gameOver() {
        levelLabel.restart();
        levelLabel.setText("GAME OVER!");
        player.die();
        isGameOver = true;
    }

    private void handleEnemies() {
        for (Enemy enemy : enemies) {
            if (enemy.remove) {
                score += 100;
                scoreLabel.setText("{COLOR=#be772b}score: " + score);
                jams.add(new Jam(enemy.body.getPosition().x, enemy.body.getPosition().y, mainStage, world));
                enemy.remove();
                enemies.removeValue(enemy, true);
            }
        }
    }

    private void handleJams() {
        for (Jam jam : jams) {
            if (jam.spawn) {
                enemies.add(new Enemy(jam.body.getPosition().x, jam.body.getPosition().y, mainStage, world));
                jams.removeValue(jam, true);
                jam.remove = true;
            }

            if (jam.pickup) {
                score += 25;
                scoreLabel.setText("{COLOR=#be772b}score: " + score);
                jams.removeValue(jam, true);
                jam.remove = true;
            }
        }
    }

    void spawnEnemies(int numEnemies) {
        enemies = new Array<>();
        for (int i = 1; i <= numEnemies; i++) {
            new BaseActor(0f, 0f, mainStage).addAction(Actions.sequence(
                    Actions.delay(1f * i),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Vector2 spawnPoint = getRandomSpawnPoint();
                            enemies.add(new Enemy(spawnPoint.x, spawnPoint.y + Enemy.bodyRadius, mainStage, world));
                        }
                    })
            ));
        }
    }

    void breakBridges() {
        for (Bridge bridge : bridges)
            bridge.destroy();
    }

    private Vector2 getRandomSpawnPoint() {
        int random = MathUtils.random(0, spawnPoints.size - 1);
        return spawnPoints.get(random).body.getPosition();
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
            new Impassable(x, y, width, height, mainStage, world, "Impassable");
        }
    }

    private void initializeBridges() {
        bridges = new Array();
        for (MapObject obj : tilemap.getTileList("bridge")) {
            MapProperties props = obj.getProperties();
            float x = (Float) props.get("x") * BaseGame.unitScale;
            float y = (Float) props.get("y") * BaseGame.unitScale;
            float width = (Float) props.get("width") * BaseGame.unitScale;
            float height = (Float) props.get("height") * BaseGame.unitScale;
            bridges.add(new Bridge(x, y, width, height, mainStage, world, "Impassable"));
        }
    }

    private void initializeRoof() {
        MapObject obj = tilemap.getRectangleList("roof").get(0);
        MapProperties props = obj.getProperties();
        float x = (Float) props.get("x") * BaseGame.unitScale;
        float y = (Float) props.get("y") * BaseGame.unitScale;
        float width = (Float) props.get("width") * BaseGame.unitScale;
        float height = (Float) props.get("height") * BaseGame.unitScale;
        new Roof(x, y, width, height, world);
    }

    private void initializeSpawnPoints() {
        spawnPoints = new Array();
        for (MapObject obj : tilemap.getTileList("spawn")) {
            MapProperties props = obj.getProperties();
            float x = (Float) props.get("x") * BaseGame.unitScale;
            float y = (Float) props.get("y") * BaseGame.unitScale;
            float width = (Float) props.get("width") * BaseGame.unitScale;
            float height = (Float) props.get("height") * BaseGame.unitScale;
            spawnPoints.add(new SpawnPoints(x, y, width, height, mainStage, world, "SpawnPoints"));
        }
    }

    private void initializeUI() {
        levelLabel = new TypingLabel("", BaseGame.label36Style);
        levelLabel.addAction(Actions.sequence(
                Actions.delay(2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        levelLabel.setText("");
                    }
                })
        ));
        uiTable.add(levelLabel).colspan(2).growY().row();

        lifeLabel = new TypingLabel("{COLOR=#be772b}Life: " + player.lives, BaseGame.label26Style);
        uiTable.add(lifeLabel).padBottom(Gdx.graphics.getHeight() * .02f).padRight(Gdx.graphics.getWidth() * .01f);

        scoreLabel = new TypingLabel("{COLOR=#be772b}score: 0", BaseGame.label26Style);
        uiTable.add(scoreLabel).padBottom(Gdx.graphics.getHeight() * .02f).padLeft(Gdx.graphics.getWidth() * .01f);

        /*uiTable.setDebug(true);*/
    }
}

package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

import no.sandramoen.libgdxjam21.actors.Enemy;
import no.sandramoen.libgdxjam21.actors.Impassable;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    Player player;

    @Override
    public void initialize() {
        player = new Player(0, 10, mainstage, world);
        new Impassable(0, -15, mainstage, world);
        new Impassable(0, 16.5f, mainstage, world);
        new Enemy(5, 5, mainstage, world);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) Gdx.app.exit();
        else if (keycode == Input.Keys.R) BaseGame.setActiveScreen(new LevelScreen());
        if (keycode == Input.Keys.SPACE) player.flapWings();
        return super.keyDown(keycode);
    }
}

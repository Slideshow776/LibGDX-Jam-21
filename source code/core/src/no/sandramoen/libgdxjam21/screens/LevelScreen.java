package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;

import no.sandramoen.libgdxjam21.actors.Ground;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    Player player;

    @Override
    public void initialize() {
        player = new Player(0, 10, mainstage, world);
        new Ground(0, -15, mainstage, world);
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

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 worldCoordinates = mainstage.getCamera().unproject(new Vector3(screenX, screenY, 0f));
        System.out.println("mouse moved " + worldCoordinates);
        return false;
    }
}

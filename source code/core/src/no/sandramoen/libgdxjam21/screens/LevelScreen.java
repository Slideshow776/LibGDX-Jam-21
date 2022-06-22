package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import no.sandramoen.libgdxjam21.actors.Background;
import no.sandramoen.libgdxjam21.actors.Enemy;
import no.sandramoen.libgdxjam21.actors.Impassable;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.BaseScreen;

public class LevelScreen extends BaseScreen {
    Player player;
    Background background;

    @Override
    public void initialize() {
        background = new Background(-27, -15, mainstage);
        player = new Player(0, 10, mainstage, world);
        new Impassable(0, -15, mainstage, world);
        new Impassable(0, 16.5f, mainstage, world);
        new Enemy(5, 5, mainstage, world);

        BaseGame.levelMusic1.setVolume(BaseGame.musicVolume);
        BaseGame.levelMusic1.setLooping(true);
        BaseGame.levelMusic1.play();

        BaseGame.gallopSoundMusic.setVolume(0f);
        BaseGame.gallopSoundMusic.setLooping(true);
        BaseGame.gallopSoundMusic.play();
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
}

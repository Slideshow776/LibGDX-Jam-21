package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseScreen;

public class LevelScreen extends BaseScreen {

    @Override
    public void initialize() {
        BaseActor test = new BaseActor(0f, 0f, mainstage);
        test.loadImage("whitePixel");
        test.scaleBy(10f);
        test.rotateBy(45f);
        test.setDebug(true);
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) Gdx.app.exit();
        return super.keyDown(keycode);
    }
}

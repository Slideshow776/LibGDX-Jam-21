package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.BaseScreen;

public class MenuScreen extends BaseScreen {
    BaseActor button;

    @Override
    public void initialize() {
        /*BaseActor title = new BaseActor(0f * BaseGame.unitScale, 20f * BaseGame.unitScale, mainStage);
        title.loadImage("title");
        *//*title.setSize(50f, 15f);*//*
        title.setPosition(title.getX() -title.getWidth() / 2, title.getY());*/

        button = new BaseActor(0f * BaseGame.unitScale, 0f * BaseGame.unitScale, mainStage);
        button.loadImage("button");
        button.setPosition(button.getX() - button.getWidth() / 2, button.getY() - button.getHeight() / 2);
        /*button.setSize(2f, 2f);*/

        OrthographicCamera camera = (OrthographicCamera) mainStage.getCamera();
        camera.zoom = 2.5f;
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.Q) Gdx.app.exit();
        if (keycode == Input.Keys.ENTER) startGame();
        return super.keyDown(keycode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        startGame();
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void startGame() {
        BaseGame.clickSound.play(BaseGame.soundVolume);
        button.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.moveTo(button.getX(), -50f, 1f),
                        Actions.rotateBy(-90, 1f)
                ),
                Actions.delay(.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        BaseGame.setActiveScreen(new IntroScreen());
                    }
                })
        ));
    }
}

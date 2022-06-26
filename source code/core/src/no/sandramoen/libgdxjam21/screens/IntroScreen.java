package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.BaseScreen;

public class IntroScreen extends BaseScreen {
    BaseActor raeleus;
    BaseActor dragonQueen;
    BaseActor swordSlash;
    TypingLabel subtitles;

    @Override
    public void initialize() {
        BaseGame.introMusic.setVolume(1f);
        BaseGame.introMusic.play();

        raeleus = new BaseActor(-12f, 2f, mainStage);
        raeleus.loadImage("raeleus");

        dragonQueen = new BaseActor(4f, -9f, mainStage);
        dragonQueen.loadImage("dragon queen");

        swordSlash = new BaseActor(-100f, -100f, mainStage);
        swordSlash.loadImage("whitePixel");
        swordSlash.setOrigin(Align.center);

        playScene();

        subtitles = new TypingLabel("", BaseGame.label26Style);
        uiTable.add(subtitles).expandY().bottom().padBottom(Gdx.graphics.getHeight() * .02f);
        // uiTable.setDebug(true);
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
        BaseGame.setActiveScreen(new Level1Screen(null, null));
    }

    private void playScene() {
        new BaseActor(0f, 0f, mainStage).addAction(Actions.sequence(
                Actions.delay(1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        talk(raeleus);
                        BaseGame.introVoice.setVolume(1f);
                        BaseGame.introVoice.play();
                        setSubtitles("It's over Dragon Queen!");
                    }
                }),
                Actions.delay(2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setSubtitles("I have the {RAINBOW}high ground{ENDRAINBOW}!");
                    }
                }),
                Actions.delay(2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setSubtitles("...which you need to defeat your enemies in this game");
                    }
                }),
                Actions.delay(4f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        raeleus.clearActions();
                        talk(dragonQueen);
                        setSubtitles("you underestimate my power!");
                    }
                }),
                Actions.delay(4f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        dragonQueen.clearActions();
                        setSubtitles("");
                        swordSlash.setPosition(0f, 0f);
                        swordSlash.addAction(Actions.sequence(
                                Actions.parallel(
                                        Actions.scaleBy(300f, 8f, .2f),
                                        Actions.rotateBy(45f, 0f)
                                ),
                                Actions.moveTo(-100, -100, 0f)
                        ));
                        BaseGame.swordSlashSound.play(BaseGame.soundVolume);
                        dragonQueen.loadImage("dragon queen battle");
                    }
                }),
                Actions.delay(2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        talk(dragonQueen);
                        setSubtitles("You may have won this battle,\nbut surely my minions will lay waste to all your {COLOR=#a4dddb}floating cities{CLEARCOLOR}!");
                        dragonQueen.addAction(Actions.sequence(
                                Actions.delay(6f),
                                Actions.moveTo(50f, -50f, 1f)
                        ));
                        raeleus.addAction(Actions.sequence(
                                Actions.delay(6f),
                                Actions.moveTo(-4f, -4f, 2f)
                        ));
                    }
                }),
                Actions.delay(8f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        talk(raeleus);
                        setSubtitles("Oh hey! you're a player?\nCan you wrap this up for me please?");
                    }
                }),
                Actions.delay(4f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        talk(raeleus);
                        setSubtitles("{FAST}...I gotta go finish my own game. {WAIT=1}THANKS!");
                        raeleus.addAction(Actions.moveTo(50f, 50f, 4f));
                    }
                }),
                Actions.delay(4f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        startGame();
                    }
                })
        ));

    }

    private void setSubtitles(String text) {
        subtitles.restart();
        subtitles.setText(text);
    }

    private void talk(BaseActor baseActor) {
        baseActor.addAction(Actions.forever(Actions.sequence(
                Actions.rotateTo(-5, .2f),
                Actions.rotateTo(5, .4f),
                Actions.rotateTo(0, .2f)
        )));
    }
}

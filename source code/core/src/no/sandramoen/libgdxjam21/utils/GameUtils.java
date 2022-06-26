package no.sandramoen.libgdxjam21.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

public class GameUtils {
    public static void playRandomSound(Sound... sounds) {
        Array<Sound> list = new Array();
        for (Sound sound : sounds)
            list.add(sound);
        list.get(MathUtils.random(0, list.size - 1)).play(BaseGame.soundVolume);
    }

    public static void playLoopingMusic(Music music) {
        music.setVolume(BaseGame.musicVolume);
        music.setLooping(true);
        music.play();
    }

    public static void playLoopingMusic(Music music, float volume) {
        music.setVolume(volume);
        music.setLooping(true);
        music.play();
    }

    public static void scaleIn(BaseActor baseActor) {
        baseActor.setOrigin(Align.bottom);
        baseActor.setScaleY(0f);
        baseActor.addAction(Actions.scaleTo(1f, 1f, .5f));
    }

    public static void wrapWorld(Stage stage, Body body, Float bodyWidth) {
        Vector3 vec = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        OrthographicCamera camera = (OrthographicCamera) stage.getCamera();
        if (camera.zoom != 1.2f)
            camera.zoom = 1.2f;
        Vector3 vec2 = camera.unproject(vec);

        float modifier = .5f;
        if (body.getPosition().x + bodyWidth * modifier > vec2.x)
            body.setTransform(0 + bodyWidth * modifier, body.getPosition().y, body.getAngle());
        else if (body.getPosition().x + bodyWidth * modifier < 0)
            body.setTransform(vec2.x - bodyWidth * modifier, body.getPosition().y, body.getAngle());
    }
}

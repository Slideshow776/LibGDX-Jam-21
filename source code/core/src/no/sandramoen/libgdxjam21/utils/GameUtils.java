package no.sandramoen.libgdxjam21.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
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

    public static void wrapWorld(Stage stage, Body body, Float bodyWidth) {
        Vector3 vec = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        Vector3 vec2 = stage.getCamera().unproject(vec);

        if (body.getPosition().x + bodyWidth / 4 > vec2.x)
            body.setTransform(0 + bodyWidth / 4, body.getPosition().y, body.getAngle());
        else if (body.getPosition().x + bodyWidth / 4 < 0)
            body.setTransform(vec2.x - bodyWidth / 4, body.getPosition().y, body.getAngle());
    }
}

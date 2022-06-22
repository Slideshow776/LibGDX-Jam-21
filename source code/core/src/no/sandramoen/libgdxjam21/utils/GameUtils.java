package no.sandramoen.libgdxjam21.utils;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GameUtils {
    public static void playRandomSound(Sound... sounds) {
        Array<Sound> list = new Array();
        for (Sound sound : sounds)
            list.add(sound);
        list.get(MathUtils.random(0, list.size - 1)).play(BaseGame.soundVolume);
    }
}

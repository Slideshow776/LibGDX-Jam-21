package no.sandramoen.libgdxjam21;

import com.badlogic.gdx.math.MathUtils;

import no.sandramoen.libgdxjam21.screens.LevelScreen;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class MyGdxGame extends BaseGame {

	@Override
	public void create() {
		super.create();
		setActiveScreen(new LevelScreen());
	}
}

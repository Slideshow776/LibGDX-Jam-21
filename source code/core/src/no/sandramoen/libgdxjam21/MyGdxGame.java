package no.sandramoen.libgdxjam21;

import no.sandramoen.libgdxjam21.screens.IntroScreen;
import no.sandramoen.libgdxjam21.screens.LevelScreen;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class MyGdxGame extends BaseGame {

	@Override
	public void create() {
		super.create();
		setActiveScreen(new LevelScreen());
		// setActiveScreen(new MenuScreen());
		// setActiveScreen(new IntroScreen());
	}
}

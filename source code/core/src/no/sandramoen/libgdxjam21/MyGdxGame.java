package no.sandramoen.libgdxjam21;

import no.sandramoen.libgdxjam21.screens.Level1Screen;
import no.sandramoen.libgdxjam21.screens.MenuScreen;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class MyGdxGame extends BaseGame {

	@Override
	public void create() {
		super.create();
		// setActiveScreen(new Level1Screen(null, null));
		setActiveScreen(new MenuScreen());
		// setActiveScreen(new IntroScreen());
	}
}

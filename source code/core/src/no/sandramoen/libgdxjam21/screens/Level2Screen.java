package no.sandramoen.libgdxjam21.screens;

import no.sandramoen.libgdxjam21.actors.Background;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class Level2Screen extends BaseLevel {

    public Level2Screen(Player player, Background background) {
        super(player, background);
    }

    @Override
    public void initialize() {
        super.initialize();
        spawnEnemies(4);
        levelLabel.restart();
        levelLabel.setText("Level 2");
        lifeLabel.setText("{COLOR=#be772b}Life: " + player.lives);
        lava.rise();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        checkWinCondition();
    }

    private void checkWinCondition() {
        if (time >= 10f && enemies.size == 0 && jams.size == 0) {
            BaseGame.setActiveScreen(new Level3Screen(player, background));
        }
    }
}
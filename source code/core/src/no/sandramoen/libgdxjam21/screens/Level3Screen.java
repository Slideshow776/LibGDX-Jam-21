package no.sandramoen.libgdxjam21.screens;

import no.sandramoen.libgdxjam21.actors.Background;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class Level3Screen extends BaseLevel {

    public Level3Screen(Player player, Background background) {
        super(player, background);
    }

    @Override
    public void initialize() {
        super.initialize();
        spawnEnemies(5);
        levelLabel.restart();
        levelLabel.setText("Level 3");
        lifeLabel.setText("{COLOR=#be772b}Life: " + player.lives);
        lava.rise();
        breakBridges();
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        checkWinCondition();
    }

    private void checkWinCondition() {
        if (time >= 10f && enemies.size == 0 && jams.size == 0) {
            BaseGame.setActiveScreen(new Level1Screen(player, background));
        }
    }
}
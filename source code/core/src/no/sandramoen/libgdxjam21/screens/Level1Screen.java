package no.sandramoen.libgdxjam21.screens;

import com.badlogic.gdx.math.Vector2;

import no.sandramoen.libgdxjam21.actors.Background;
import no.sandramoen.libgdxjam21.actors.Player;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class Level1Screen extends BaseLevel {

    public Level1Screen(Player player, Background background) {
        super(player, background);
    }

    @Override
    public void initialize() {
        super.initialize();
        spawnEnemies(3);
        levelLabel.restart();
        levelLabel.setText("Level 1");
        lifeLabel.setText("{COLOR=#be772b}Life: " + player.lives);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        checkWinCondition();
    }

    private void checkWinCondition() {
        if (time >= 10f && enemies.size == 0 && jams.size == 0) {
            BaseGame.setActiveScreen(new Level2Screen(player, background));
        }
    }
}
package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class SpawnPoints extends Impassable {
    public SpawnPoints(float x, float y, float width, float height, Stage stage, World world) {
        super(x, y, width, height, stage, world);
    }
}

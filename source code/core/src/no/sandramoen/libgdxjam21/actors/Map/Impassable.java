package no.sandramoen.libgdxjam21.actors.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.libgdxjam21.utils.BaseActor;

public class Impassable extends BaseActor {
    public Body body;

    public Impassable(float x, float y, float width, float height, Stage stage, World world, String userData) {
        super(x, y, stage);
        createBody(x, y, width, height, world, userData);
    }

    private void createBody(float x, float y, float width, float height, World world, String userData) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(x + width / 2, y + height / 2));
        body = world.createBody(groundBodyDef);
        body.setUserData(this);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width / 2, (height / 2) * .8f);
        Fixture fixture = body.createFixture(groundBox, 0.0f);
        fixture.setUserData(userData);
        groundBox.dispose();
    }
}

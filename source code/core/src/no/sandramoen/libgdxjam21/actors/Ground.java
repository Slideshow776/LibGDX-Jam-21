package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.libgdxjam21.utils.BaseActor;

public class Ground extends BaseActor {
    private Body body;
    private float width = 30;
    private float height = 1;

    public Ground(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(width * 2, height * 2);
        setColor(Color.FOREST);
        createBody(x, y, world);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        centerAtPosition(body.getPosition().x, body.getPosition().y);
    }

    private void createBody(float x, float y, World world) {
        // Create our body definition
        BodyDef groundBodyDef = new BodyDef();
        // Set its world position
        groundBodyDef.position.set(new Vector2(x, y));

        // Create a body from the definition and add it to the world
        body = world.createBody(groundBodyDef);

        // Create a polygon shape
        PolygonShape groundBox = new PolygonShape();
        // Set the polygon shape as a box which is twice the size of our view port and 20 high
        // (setAsBox takes half-width and half-height as arguments)
        groundBox.setAsBox(width, height);
        // Create a fixture from our polygon shape and add it to our ground body
        body.createFixture(groundBox, 0.0f);
        // Clean up after ourselves
        groundBox.dispose();
    }
}

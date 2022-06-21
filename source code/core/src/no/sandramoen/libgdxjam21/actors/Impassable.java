package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.libgdxjam21.utils.BaseActor;

public class Impassable extends BaseActor {
    private Body body;
    private float width = 30;
    private float height = 1;

    public Impassable(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(width * 2, height * 2);
        setColor(Color.FOREST);
        createBody(x, y, world);
        centerAtPosition(body.getPosition().x, body.getPosition().y);
    }

    private void createBody(float x, float y, World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(x, y));
        body = world.createBody(groundBodyDef);
        body.setUserData(this);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width, height);
        body.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }
}

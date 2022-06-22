package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.libgdxjam21.utils.BaseActor;

public class Impassable extends BaseActor {
    private Body body;

    public Impassable(float x, float y, float width, float height, Stage stage, World world) {
        super(x, y, stage);
        // System.out.println("impassable created => x: " + x + ", y:" + y + ", width: " + width / 2 + ", height: " + height / 2);
        loadImage("ground");
        setSize(width, height * 1.25f);
        createBody(x, y, width, height, world);

        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - height / 2);
    }

    private void createBody(float x, float y, float width, float height, World world) {
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(x + width / 2, y + height / 2));
        body = world.createBody(groundBodyDef);
        body.setUserData(this);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width / 2, height / 2);
        body.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }
}

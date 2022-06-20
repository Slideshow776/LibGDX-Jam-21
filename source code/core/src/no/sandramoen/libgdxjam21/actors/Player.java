package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.libgdxjam21.utils.BaseActor;

public class Player extends BaseActor {
    private Body body;
    private float width = 1.2f;
    private float height = 1f;
    private final float MAX_GROUND_VELOCITY = 12.5f;
    private final float MAX_FLAP_VELOCITY = 1f;

    public Player(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(width * 2, height * 2);
        setColor(Color.GOLDENROD);
        createBody(x, y, world);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        keyboardPolling();
        wrapWorld();

        centerAtPosition(body.getPosition().x, body.getPosition().y);
    }

    public void flapWings() {
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0f, 15, pos.x, pos.y, true);
    }

    private void createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);

        PolygonShape box = new PolygonShape();
        box.setAsBox(width, height);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = .1f;

        Fixture fixture = body.createFixture(fixtureDef);
        box.dispose();
    }

    private void wrapWorld() {
        Vector3 vec = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        Vector3 vec2 = this.getStage().getCamera().unproject(vec);

        /*System.out.println(vec2);*/
        System.out.println(body.getPosition().x + this.width + ", " + vec2.x + ", " + (body.getPosition().x + this.width > vec2.x));

        if (body.getPosition().x + this.width / 4 > vec2.x)
            body.setTransform(-vec2.x - this.width / 4, body.getPosition().y, body.getAngle());
            // setX(-vec2.x - this.width);
        else if (body.getPosition().x + this.width / 4 < -vec2.x)
            body.setTransform(vec2.x - this.width / 4, body.getPosition().y, body.getAngle());
            // setX(vec2.x - this.width);

        if (body.getPosition().y + getHeight() / 2 > -vec2.y)
            setY(-vec2.y - this.height);
    }

    private void keyboardPolling() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();

        if (Gdx.input.isKeyPressed(Keys.A) && vel.x > -MAX_GROUND_VELOCITY)
            body.applyLinearImpulse(-1f, 0, pos.x, pos.y, true);

        if (Gdx.input.isKeyPressed(Keys.D) && vel.x < MAX_GROUND_VELOCITY)
            body.applyLinearImpulse(1f, 0, pos.x, pos.y, true);
    }
}

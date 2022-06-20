package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.libgdxjam21.utils.BaseActor;

public class Enemy extends BaseActor {
    private Body body;
    private float width = 1.2f;
    private float height = 1f;
    private final float MAX_HORIZONTAL_VELOCITY = 8f;
    private float randomImpulse;

    private float flapFrequency = .3f;
    private float flapCounter = 0f;

    public Enemy(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("whitePixel");
        setSize(width * 2, height * 2);
        setColor(Color.FIREBRICK);
        createBody(x, y, world);

        addAction(Actions.forever(Actions.sequence(
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        randomImpulse = MathUtils.random(-1f, 1f);
                    }
                }),
                Actions.delay(5f)
        )));
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        wrapWorld();
        syncGraphicsWithBody();
        AI(delta);
    }

    public void flapWings() {
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0f, 15, pos.x, pos.y, true);
    }

    private void AI(float delta) {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();

        if (randomImpulse > 0) {
            if (vel.x < MAX_HORIZONTAL_VELOCITY)
                body.applyLinearImpulse(randomImpulse, 0, pos.x, pos.y, true);
        } else {
            if (vel.x > -MAX_HORIZONTAL_VELOCITY)
                body.applyLinearImpulse(randomImpulse, 0, pos.x, pos.y, true);
        }


        if (flapCounter <= flapFrequency) {
            flapCounter += delta;
        } else {
            flapWings();
            flapCounter = 0f;
            flapFrequency = MathUtils.random(.1f, .6f);
        }
    }

    private void syncGraphicsWithBody() {
        centerAtPosition(body.getPosition().x, body.getPosition().y);
    }

    private void createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);

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

        if (body.getPosition().x + this.width / 4 > vec2.x)
            body.setTransform(-vec2.x - this.width / 4, body.getPosition().y, body.getAngle());
        else if (body.getPosition().x + this.width / 4 < -vec2.x)
            body.setTransform(vec2.x - this.width / 4, body.getPosition().y, body.getAngle());

        if (body.getPosition().y + getHeight() / 2 > -vec2.y)
            setY(-vec2.y - this.height);
    }
}

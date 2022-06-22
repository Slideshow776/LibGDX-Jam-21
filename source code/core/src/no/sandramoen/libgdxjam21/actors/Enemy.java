package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.Gdx;
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
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class Enemy extends BaseActor {
    public boolean remove = false;

    private Body body;
    private float bodyWidth = 1.2f;
    private float bodyHeight = 1f;
    private final float MAX_HORIZONTAL_VELOCITY = 8f;
    private float randomImpulse;
    private float flapFrequency = .3f;
    private float flapCounter = 0f;
    private World world;

    public Enemy(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        this.world = world;
        loadImage("enemy");
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
        GameUtils.wrapWorld(getStage(), body, bodyWidth);
        syncGraphicsWithBody();
        AI(delta);
        if (remove) remove();
    }

    @Override
    public boolean remove() {
        body.setTransform(new Vector2(-100f, -100f), body.getAngle());
        body.setActive(false);
        body.setAwake(false);
        return super.remove();
    }

    public void flapWings() {
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0f, 15, pos.x, pos.y, true);
    }

    private void AI(float delta) {
        randomMoving();
        randomFlying(delta);
    }

    private void randomMoving() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();

        if (randomImpulse > 0) {
            if (vel.x < MAX_HORIZONTAL_VELOCITY) {
                if (!isFacingRight) flip();
                body.applyLinearImpulse(randomImpulse, 0, pos.x, pos.y, true);
            }
        } else {
            if (vel.x > -MAX_HORIZONTAL_VELOCITY) {
                if (isFacingRight) flip();
                body.applyLinearImpulse(randomImpulse, 0, pos.x, pos.y, true);
            }
        }
    }

    private void randomFlying(float delta) {
        if (flapCounter <= flapFrequency) {
            flapCounter += delta;
        } else {
            flapWings();
            flapCounter = 0f;
            flapFrequency = MathUtils.random(.05f, .65f);
        }
    }

    private void syncGraphicsWithBody() {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - bodyHeight);
    }

    private void createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setUserData(this);

        PolygonShape box = new PolygonShape();
        box.setAsBox(bodyWidth, bodyHeight);

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

        if (body.getPosition().x + this.bodyWidth / 4 > vec2.x)
            body.setTransform(-vec2.x - this.bodyWidth / 4, body.getPosition().y, body.getAngle());
        else if (body.getPosition().x + this.bodyWidth / 4 < -vec2.x)
            body.setTransform(vec2.x - this.bodyWidth / 4, body.getPosition().y, body.getAngle());

        if (body.getPosition().y + getHeight() / 2 > -vec2.y)
            setY(-vec2.y - this.bodyHeight);
    }
}

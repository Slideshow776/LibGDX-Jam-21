package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class Enemy extends BaseActor {
    public static float bodyRadius = 1.2f;
    public boolean remove = false;
    public Body body;
    public World world;

    private final float MAX_HORIZONTAL_VELOCITY = 8f;
    private float randomImpulse;
    private float flapFrequency = .3f;
    private float flapCounter = 0f;

    public Enemy(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        this.world = world;
        loadImage("enemy");
        setOrigin(Align.bottom);
        GameUtils.scaleIn(this);
        createBody(x, y + bodyRadius, world);
        createRandomImpulses();
        BaseGame.enemySpawnSound.play(BaseGame.soundVolume);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        GameUtils.wrapWorld(getStage(), body, bodyRadius);
        syncGraphicsWithBody();
        AI(delta);
    }

    @Override
    public boolean remove() {
        remove = false;
        BaseGame.dragonRoarSound.play(BaseGame.soundVolume);
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

    private void createRandomImpulses() {
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

    private void syncGraphicsWithBody() {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - bodyRadius);
    }

    private void createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setFixedRotation(true);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(bodyRadius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = .1f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Enemy");
        circle.dispose();
    }
}

package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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
    public boolean respawn = false;

    private Body body;
    private float bodyWidth = 1.2f;
    private float bodyHeight = 1f;
    private final float MAX_HORIZONTAL_VELOCITY = 12.5f;

    public Player(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("player");
        createBody(x, y, world);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (respawn) respawn();
        keyboardPolling();
        wrapWorld();
        syncGraphicsWithBody();
    }

    public void flapWings() {
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0f, 15, pos.x, pos.y, true);
    }

    private void respawn() {
        body.setTransform(new Vector2(0f, 0f), body.getAngle());
        body.setLinearVelocity(0f, 0f);
        respawn = false;
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
    }

    private void keyboardPolling() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();

        if (Gdx.input.isKeyPressed(Keys.A) && vel.x > -MAX_HORIZONTAL_VELOCITY) {
            if (isFacingRight) flip();
            body.applyLinearImpulse(-1f, 0, pos.x, pos.y, true);
        }

        if (Gdx.input.isKeyPressed(Keys.D) && vel.x < MAX_HORIZONTAL_VELOCITY) {
            if (!isFacingRight) flip();
            body.applyLinearImpulse(1f, 0, pos.x, pos.y, true);
        }
    }
}

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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class Player extends BaseActor {
    public boolean respawn = false;

    private Body body;
    private float bodyWidth = 1.2f;
    private float bodyHeight = 1.2f;
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
        GameUtils.wrapWorld(getStage(), body, bodyWidth);
        syncGraphicsWithBody();
        playGallopingSound();
    }

    public void flapWings() {
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0f, 15, pos.x, pos.y, true);
    }

    public void reverseHorizontalDirection() {
        body.setLinearVelocity(new Vector2(body.getLinearVelocity().x * -1.3f, 0f));
    }

    private void playGallopingSound() {
        if (body.getLinearVelocity().y == 0 && Math.abs(body.getLinearVelocity().x) > 5)
            BaseGame.gallopSoundMusic.setVolume(BaseGame.soundVolume * .4f);
        else
            BaseGame.gallopSoundMusic.setVolume(0);
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

    private void keyboardPolling() {
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();

        if (Gdx.input.isKeyPressed(Keys.A) && vel.x > -MAX_HORIZONTAL_VELOCITY) {
            if (isFacingRight) {
                flip();
                animateTurn();
            }
            body.applyLinearImpulse(-1, 0, pos.x, pos.y, true);
        }

        if (Gdx.input.isKeyPressed(Keys.D) && vel.x < MAX_HORIZONTAL_VELOCITY) {
            if (!isFacingRight) {
                flip();
                animateTurn();
            }
            body.applyLinearImpulse(1, 0, pos.x, pos.y, true);
        }
    }

    private void animateTurn() {
        loadImage("player_turn");
        addAction(Actions.sequence(
                Actions.delay(.1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        loadImage("player");
                    }
                })
        ));
    }
}

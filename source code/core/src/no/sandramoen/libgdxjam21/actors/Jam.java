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

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class Jam extends BaseActor {
    public static float bodyRadius = .6f;
    public boolean remove = false;

    private Body body;

    public Jam(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("jam");
        createBody(x, y + bodyRadius, world);
        applyRandomDirection();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        GameUtils.wrapWorld(getStage(), body, bodyRadius);
        syncGraphicsWithBody();
        if (remove) remove();
    }

    @Override
    public boolean remove() {
        body.setTransform(new Vector2(-100f, -100f), body.getAngle());
        body.setActive(false);
        body.setAwake(false);
        return super.remove();
    }

    private void syncGraphicsWithBody() {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - bodyRadius);
        setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    private void applyRandomDirection() {
        body.applyLinearImpulse(
                MathUtils.random(-5f, 5f),
                -5f,
                body.getWorldCenter().x,
                body.getWorldCenter().y,
                true
        );
    }

    private void createBody(float x, float y, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        body = world.createBody(bodyDef);
        body.setUserData(this);

        CircleShape circle = new CircleShape();
        circle.setRadius(bodyRadius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 1f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = .1f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Jam");
        circle.dispose();
    }
}

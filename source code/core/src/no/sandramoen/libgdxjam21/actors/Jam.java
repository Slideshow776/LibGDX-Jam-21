package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.badlogic.gdx.utils.Array;

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class Jam extends BaseActor {
    public static float bodyRadius = .6f;
    public boolean remove = false;

    private Body body;
    private Animation<TextureRegion> spawnAnimation;

    public Jam(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("jam/jam");
        createBody(x, y + bodyRadius, world);
        applyRandomDirection();
        animationSetup();
        spawnEnemy();
        stop();
        syncGraphicsWithBody();
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

    private void spawnEnemy() {
        addAction(Actions.sequence(
                Actions.delay(10f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        body.setTransform(body.getPosition().x, body.getPosition().y, 0f);
                        body.setFixedRotation(true);
                        setAnimation(spawnAnimation);
                    }
                }),
                Actions.delay(5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        remove = true;
                        new Enemy(body.getPosition().x, body.getPosition().y, getStage(), body.getWorld());
                    }
                })
        ));
    }

    private void animationSetup() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        animationImages.add(BaseGame.textureAtlas.findRegion("jam/jam_spawning1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("jam/jam_spawning2"));
        animationImages.add(BaseGame.textureAtlas.findRegion("jam/jam_spawning3"));
        animationImages.add(BaseGame.textureAtlas.findRegion("jam/jam_spawning4"));
        animationImages.add(BaseGame.textureAtlas.findRegion("jam/jam_spawning5"));
        spawnAnimation = new Animation(1f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();
    }

    private void stop() {
        addAction(Actions.sequence(
                Actions.delay(5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        body.setLinearVelocity(0f, 0f);
                    }
                })
        ));
    }

    private void syncGraphicsWithBody() {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
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
        fixtureDef.restitution = .5f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Jam");
        circle.dispose();
    }
}

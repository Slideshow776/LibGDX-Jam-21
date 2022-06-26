package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.libgdxjam21.actors.Map.TilemapActor;
import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;
import no.sandramoen.libgdxjam21.utils.GameUtils;

public class Player extends BaseActor {
    public boolean respawn = false;
    public static int numFootContacts = 0;
    public Body body;
    public final float MAX_HORIZONTAL_VELOCITY = 12.5f;

    private float bodyRadius = 1.2f;
    private float invulnerableMinimalDuration = 1f;
    private float invulnerableTotalDuration = 5f;
    private RepeatAction invulnerableAnimation;
    private boolean disableInput = false;

    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> runningAnimation;
    private Animation<TextureRegion> flyingAnimation;

    private enum state {IDLE, FLYING, RUNNING}

    private boolean isFlying = false;

    private state currentState;

    public Player(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        createBody(x, y, world);
        animationSetup();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        keyboardPolling();
        GameUtils.wrapWorld(getStage(), body, bodyRadius);
        syncGraphicsWithBody();
        playGallopingSound();
        standingStill();
        isFlying = numFootContacts == 0;
        if (isFlying)
            BaseGame.breakingSoundMusic.setVolume(0);
        if (isShakyCam)
            shakeCamera();
    }

    public void flapWings() {
        Vector2 pos = body.getPosition();
        body.applyLinearImpulse(0f, 15, pos.x, pos.y, true);
        if (!body.isActive())
            setVulnerable();
        BaseGame.wingFlapSound.play(BaseGame.soundVolume, MathUtils.random(.8f, 1.2f), 0f);
        if (currentState != state.FLYING) {
            setAnimation(flyingAnimation);
            currentState = state.FLYING;
        }
    }

    public void reverseHorizontalDirection() {
        shakeCameraABit();
        if (body.getLinearVelocity().x < 0)
            body.setLinearVelocity(1.2f * MAX_HORIZONTAL_VELOCITY, 0f);
        else
            body.setLinearVelocity(-1.2f * MAX_HORIZONTAL_VELOCITY, 0f);
    }

    public void reverseDirection() {
        shakeCameraABit();
        body.setLinearVelocity(body.getLinearVelocity().x * -1.5f, body.getLinearVelocity().y * -1.5f);
    }

    public void respawn(Vector2 spawnPoint) {
        body.setTransform(new Vector2(spawnPoint.x, spawnPoint.y + 1.4f * bodyRadius), body.getAngle());
        body.setLinearVelocity(0f, 0f);
        setInvulnerable();
        BaseGame.hurtSound.play(BaseGame.soundVolume);
        GameUtils.scaleIn(this);
        respawn = false;
        shakeCameraABit();
    }

    public void bounceOffOfRoof() {
        body.setLinearVelocity(
                body.getLinearVelocity().x,
                body.getLinearVelocity().y + 100f
        );
        System.out.println(body.getLinearVelocity().x);
    }

    private void shakeCameraABit() {
        isShakyCam = true;
        new BaseActor(0f, 0f, getStage()).addAction(Actions.sequence(
                Actions.delay(1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        isShakyCam = false;
                        TilemapActor.setFocalPoint(getStage());
                    }
                })));
    }

    private void standingStill() {
        if (body.getLinearVelocity().x == 0f && body.getLinearVelocity().y == 0f) {
            if (currentState != state.IDLE) {
                setAnimation(idleAnimation);
                currentState = state.IDLE;
            }
        }
        if (body.getLinearVelocity().x == 0f) {
            BaseGame.breakingSoundMusic.setVolume(0f);
        }
    }

    private void animationSetup() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        for (int i = 0; i <= 20; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("player/idle1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("player/idle2"));
        idleAnimation = new Animation(.25f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("player/running1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("player/running2"));
        animationImages.add(BaseGame.textureAtlas.findRegion("player/running3"));
        animationImages.add(BaseGame.textureAtlas.findRegion("player/running4"));
        runningAnimation = new Animation(.125f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("player/flying1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("player/flying2"));
        animationImages.add(BaseGame.textureAtlas.findRegion("player/flying3"));
        animationImages.add(BaseGame.textureAtlas.findRegion("player/flying4"));
        flyingAnimation = new Animation(.125f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        setAnimation(idleAnimation);
        currentState = state.IDLE;
    }

    private void playGallopingSound() {
        if (body.getLinearVelocity().y == 0 && Math.abs(body.getLinearVelocity().x) > 2)
            BaseGame.gallopSoundMusic.setVolume(BaseGame.soundVolume * .4f);
        else
            BaseGame.gallopSoundMusic.setVolume(0);
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
        fixtureDef.density = 1.2f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = .1f;

        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData("Player");
        circle.dispose();

        //add foot sensor fixture
        PolygonShape polygonShape = new PolygonShape();
        polygonShape.setAsBox(0.3f, 0.3f, new Vector2(0, -1.5f), 0);
        fixtureDef.isSensor = true;
        fixtureDef.shape = polygonShape;
        Fixture footSensorFixture = body.createFixture(fixtureDef);
        footSensorFixture.setUserData("playerSensor");
    }

    private void setInvulnerable() {
        body.setActive(false);
        invulnerableAnimation = Actions.forever(Actions.sequence(
                Actions.color(Color.BLACK, .25f),
                Actions.color(Color.WHITE, .25f)
        ));
        addAction(invulnerableAnimation);
        disableInput = true;
        addAction(Actions.sequence(
                Actions.delay(invulnerableMinimalDuration),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        BaseGame.invulnerableSound.play(BaseGame.soundVolume);
                        disableInput = false;
                    }
                }),
                Actions.delay(invulnerableTotalDuration - invulnerableMinimalDuration),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        setVulnerable();
                    }
                })
        ));
    }

    private void setVulnerable() {
        body.setActive(true);
        invulnerableAnimation.finish();
        setColor(Color.WHITE);
        BaseGame.invulnerableSound.stop();
    }

    private void keyboardPolling() {
        if (disableInput) return;
        Vector2 vel = body.getLinearVelocity();
        Vector2 pos = body.getPosition();

        if (Gdx.input.isKeyPressed(Keys.A) && vel.x > -MAX_HORIZONTAL_VELOCITY) {
            if (vel.y == 0 && currentState != state.RUNNING) {
                setAnimation(runningAnimation);
                currentState = state.RUNNING;
            }
            if (!body.isActive())
                setVulnerable();
            if (isFacingRight) {
                flip();
                animateTurn();
            }
            if (vel.x > 0 && !isFlying)
                BaseGame.breakingSoundMusic.setVolume(BaseGame.soundVolume);
            else
                BaseGame.breakingSoundMusic.setVolume(0f);
            body.applyLinearImpulse(-1, 0, pos.x, pos.y, true);
        }

        if (Gdx.input.isKeyPressed(Keys.D) && vel.x < MAX_HORIZONTAL_VELOCITY) {
            if (vel.y == 0 && currentState != state.RUNNING) {
                setAnimation(runningAnimation);
                currentState = state.RUNNING;
            }
            if (!body.isActive())
                setVulnerable();
            if (!isFacingRight) {
                flip();
                animateTurn();
            }
            if (vel.x < 0f && !isFlying)
                BaseGame.breakingSoundMusic.setVolume(BaseGame.soundVolume);
            else
                BaseGame.breakingSoundMusic.setVolume(0f);
            body.applyLinearImpulse(1, 0, pos.x, pos.y, true);
        }
    }

    private void animateTurn() {
        if (isFlying) loadImage("player/turn flying");
        else loadImage("player/turn ground");
        addAction(Actions.sequence(
                Actions.delay(.1f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        if (currentState == state.FLYING)
                            setAnimation(flyingAnimation);
                        else if (currentState == state.RUNNING)
                            setAnimation(runningAnimation);
                        else if (currentState == state.IDLE)
                            setAnimation(idleAnimation);
                    }
                })
        ));
    }
}

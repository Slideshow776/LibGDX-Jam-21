package no.sandramoen.libgdxjam21.actors.Map;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.libgdxjam21.utils.BaseActor;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class Lava extends BaseActor {
    public Body body;

    public Lava(float x, float y, Stage stage, World world) {
        super(x, y, stage);
        loadImage("lava");
        createBody(x, y, getWidth(), getHeight(), world);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        syncGraphicsWithBody();
    }

    public void rise() {
        BaseGame.bubblesSound.play(BaseGame.soundVolume);
        body.setLinearVelocity(0f, 1f);
        new BaseActor(0f, 0f, getStage()).addAction(Actions.sequence(
                Actions.delay(2f),
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
    }

    private void createBody(float x, float y, float width, float height, World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.position.set(new Vector2(x + width / 2, y + height / 2));
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(width / 2, height / 2);
        Fixture fixture = body.createFixture(groundBox, 0.0f);
        fixture.setUserData("Lava");
        fixture.setFriction(1f);
        groundBox.dispose();
    }
}

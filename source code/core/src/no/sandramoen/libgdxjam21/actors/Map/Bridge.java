package no.sandramoen.libgdxjam21.actors.Map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import no.sandramoen.libgdxjam21.actors.particles.BridgeBreakEffect;
import no.sandramoen.libgdxjam21.utils.BaseGame;

public class Bridge extends Impassable {
    public Bridge(float x, float y, float width, float height, Stage stage, World world, String userData) {
        super(x, y, width, height, stage, world, userData);
        loadImage("bridge");
        setScaleY(1.14f); // idk why I need to scale..
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        syncGraphicsWithBody();
    }

    public void destroy() {
        addAction(Actions.sequence(
                Actions.delay(MathUtils.random(0f, 2f)),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        BaseGame.bridgeBreakSound.play(BaseGame.soundVolume);
                        body.setTransform(body.getPosition().x, body.getPosition().y - 50f, body.getAngle());
                        startEffect();
                    }
                })
        ));
    }

    private void syncGraphicsWithBody() {
        setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
    }

    private void startEffect() {
        BridgeBreakEffect effect = new BridgeBreakEffect();
        effect.setScale(.02f);
        effect.centerAtActor(this);
        getStage().addActor(effect);
        effect.start();
    }
}

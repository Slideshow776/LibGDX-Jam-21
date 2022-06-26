package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;

import no.sandramoen.libgdxjam21.utils.BaseActor;

public class Background {
    public BaseActor background1;
    public BaseActor background2;
    public BaseActor background3;
    public BaseActor background4;
    public BaseActor background5;
    public BaseActor background6;
    private float scrollFromHere;

    public Background(float x, float y, Stage stage) {
        scrollFromHere = x;

        background1 = new BaseActor(x, y, stage);
        background1.loadImage("background3");
        background1.setScaleX(1.01f);
        background2 = new BaseActor(x + background1.getWidth(), y, stage);
        background2.loadImage("background3");

        background3 = new BaseActor(x, y, stage);
        background3.loadImage("background2");
        background3.setScaleX(1.01f);
        background4 = new BaseActor(x + background1.getWidth(), y, stage);
        background4.loadImage("background2");

        background5 = new BaseActor(x, y, stage);
        background5.loadImage("background1");
        background5.setScaleX(1.01f);
        background6 = new BaseActor(x + background1.getWidth(), y, stage);
        background6.loadImage("background1");
    }

    public void act() {
        layerScroll(background1, background2, .008f);
        layerScroll(background3, background4, .016f);
        layerScroll(background5, background6, .032f);
    }

    private void layerScroll(BaseActor baseActor1, BaseActor baseActor2, float speed) {
        baseActor1.setX(baseActor1.getX() - speed);
        baseActor2.setX(baseActor2.getX() - speed);

        if (baseActor1.getX() < scrollFromHere - baseActor1.getWidth())
            baseActor1.setX(baseActor2.getX() + baseActor2.getWidth());

        if (baseActor2.getX() < scrollFromHere - baseActor2.getWidth())
            baseActor2.setX(baseActor1.getX() + baseActor1.getWidth());
    }
}

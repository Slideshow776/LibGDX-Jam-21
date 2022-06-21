package no.sandramoen.libgdxjam21.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import no.sandramoen.libgdxjam21.actors.Enemy;
import no.sandramoen.libgdxjam21.actors.Player;

public class ListenerClass implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        String entityA = contact.getFixtureA().getBody().getUserData().getClass().getSimpleName();
        String entityB = contact.getFixtureB().getBody().getUserData().getClass().getSimpleName();

        if (entityA.equals("Player") && entityB.equals("Enemy")) {
            Player player = (Player)(contact.getFixtureA().getBody().getUserData());
            Enemy enemy = (Enemy) (contact.getFixtureB().getBody().getUserData());
            playerCollidedWithAnEnemy(player, enemy);
        } else if (entityA.equals("Enemy") && entityB.equals("Player")) {
            Player player = (Player)(contact.getFixtureB().getBody().getUserData());
            Enemy enemy = (Enemy) (contact.getFixtureA().getBody().getUserData());
            playerCollidedWithAnEnemy(player, enemy);
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void playerCollidedWithAnEnemy(Player player, Enemy enemy) {
        if (player.getY() > enemy.getY() + enemy.getHeight() * .5f)
            enemy.remove = true;
        else if (enemy.getY() > player.getY() + player.getHeight() * .5f)
            player.respawn = true;
    }
}
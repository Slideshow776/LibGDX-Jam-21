package no.sandramoen.libgdxjam21.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import no.sandramoen.libgdxjam21.actors.Enemy;
import no.sandramoen.libgdxjam21.actors.Jam;
import no.sandramoen.libgdxjam21.actors.Player;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {

        String entityA = contact.getFixtureA().getUserData().toString();
        String entityB = contact.getFixtureB().getUserData().toString();
        // System.out.println("A: " + entityA + ", B: " + entityB);

        checkEnemyCollision(contact, entityA, entityB);
        checkImpassableCollision(contact, entityA, entityB);
        checkRoofCollision(contact, entityA, entityB);

        if (entityA.equals("playerSensor") || entityB.equals("playerSensor"))
            Player.numFootContacts++;
    }

    @Override
    public void endContact(Contact contact) {
        String entityA = contact.getFixtureA().getUserData().toString();
        String entityB = contact.getFixtureB().getUserData().toString();

        if (entityA.equals("playerSensor") || entityB.equals("playerSensor"))
            Player.numFootContacts--;
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

    private void checkRoofCollision(Contact contact, String entityA, String entityB) {
        if (entityA.equals("Player") && entityB.equals("Roof")) {
            Player player = (Player) (contact.getFixtureA().getBody().getUserData());
            player.bounceOffOfRoof();
        } else if (entityA.equals("Roof") && entityB.equals("Player")) {
            Player player = (Player) (contact.getFixtureB().getBody().getUserData());
            player.bounceOffOfRoof();
        }
    }

    private void checkEnemyCollision(Contact contact, String entityA, String entityB) {
        if (entityA.equals("Player") && entityB.equals("Enemy")) {
            Player player = (Player) (contact.getFixtureA().getBody().getUserData());
            Enemy enemy = (Enemy) (contact.getFixtureB().getBody().getUserData());
            playerCollidedWithAnEnemy(player, enemy);
        } else if (entityA.equals("Enemy") && entityB.equals("Player")) {
            Player player = (Player) (contact.getFixtureB().getBody().getUserData());
            Enemy enemy = (Enemy) (contact.getFixtureA().getBody().getUserData());
            playerCollidedWithAnEnemy(player, enemy);
        }
    }

    private void checkImpassableCollision(Contact contact, String entityA, String entityB) {
        if (entityA.equals("Player") && entityB.equals("Impassable")) {
            Player player = (Player) (contact.getFixtureA().getBody().getUserData());
            if (Math.abs(player.body.getLinearVelocity().y) > 15f)
                playCollisionSounds();
        } else if (entityA.equals("Impassable") && entityB.equals("Player")) {
            Player player = (Player) (contact.getFixtureB().getBody().getUserData());
            if (Math.abs(player.body.getLinearVelocity().y) > 15f)
                playCollisionSounds();
        }
    }

    private void playerCollidedWithAnEnemy(Player player, Enemy enemy) {
        playCollisionSounds();

        if (player.getY() > enemy.getY() + enemy.getHeight() * (1 / 4f)) {
            enemy.remove = true;
            player.reverseDirection();
        } else if (enemy.getY() > player.getY() + player.getHeight() * (1 / 4f)) {
            player.respawn = true;
        } else {
            BaseGame.noHitSound.play(BaseGame.soundVolume);
            player.reverseHorizontalDirection();
        }
    }

    private void playCollisionSounds() {
        GameUtils.playRandomSound(BaseGame.pig1Sound, BaseGame.pig2Sound, BaseGame.pig3Sound);
        GameUtils.playRandomSound(BaseGame.armor1Sound, BaseGame.armor2Sound, BaseGame.armor3Sound, BaseGame.armor4Sound, BaseGame.armor5Sound);
    }
}

# LibGDX Jam 21
A short game created for the [Libgdx Jam 21](https://itch.io/jam/libgdx-jam-21).

## Project comments
### Removing box2D bodies
Hacky, but it works.
CollisionListener.java
```
private void playerCollidedWithAnEnemy(Player player, Enemy enemy) {
    enemy.remove = true;
}
```

Enemy.java
```
@Override
public void act(float delta) {
    super.act(delta);
    wrapWorld();
    syncGraphicsWithBody();
    AI(delta);
    if (remove) remove();
}

@Override
public boolean remove() {
    body.setTransform(new Vector2(-100f, -100f), body.getAngle());
    body.setActive(false);
    body.setAwake(false);
    return super.remove();
}
```

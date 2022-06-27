# LibGDX Jam 21
A short game created for the [Libgdx Jam 21](https://itch.io/jam/libgdx-jam-21).

[Play it](https://sandramoen.itch.io/pig-joust) for free now? : )

_`A` and `D` to move, `SPACE` to fly, and you can press `R` to restart._
![demo](https://user-images.githubusercontent.com/4059636/175880979-05871499-8461-4df5-ab79-446ed0ff7dba.gif)
Also available [here](https://slideshow776.github.io/LibGDX-Jam-21/).

## Project comments
The project board is [here](https://github.com/Slideshow776/LibGDX-Jam-21/projects/1).

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

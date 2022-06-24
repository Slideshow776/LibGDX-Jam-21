# LibGDX Jam 21
A short game created for the [Libgdx Jam 21](https://itch.io/jam/libgdx-jam-21).

[Play it](https://slideshow776.github.io/LibGDX-Jam-21/) for free now? : )

_`A` and `D` to move, `SPACE` to fly, and you can press `R` to restart._
![image](https://user-images.githubusercontent.com/4059636/175490318-9707b11f-4f3a-41da-b553-fd34a60ddc2f.png)

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

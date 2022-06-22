package no.sandramoen.libgdxjam21.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import java.util.ArrayList;
import java.util.Iterator;

import no.sandramoen.libgdxjam21.utils.BaseGame;

public class TilemapActor extends Actor {
    public static Vector2 focalPoint;
    private TiledMap tiledMap;
    private OrthoCachedTiledMapRenderer tiledMapRenderer;

    public TilemapActor(TiledMap tiledMap, Stage stage) {
        this.tiledMap = tiledMap;
        tiledMapRenderer = new OrthoCachedTiledMapRenderer(this.tiledMap, BaseGame.unitScale);
        tiledMapRenderer.setBlending(true);
        stage.addActor(this);
        calculateFocalPoint();
        setFocalPoint();
    }

    public ArrayList<MapObject> getRectangleList(String propertyName) {
        ArrayList<MapObject> list = new ArrayList<MapObject>();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof RectangleMapObject))
                    continue;

                MapProperties props = obj.getProperties();

                if (props.containsKey("name") && props.get("name").equals(propertyName))
                    list.add(obj);
            }
        }
        return list;
    }

    public ArrayList<MapObject> getTileList(String propertyName) {
        ArrayList<MapObject> list = new ArrayList();

        for (MapLayer layer : tiledMap.getLayers()) {
            for (MapObject obj : layer.getObjects()) {
                if (!(obj instanceof TiledMapTileMapObject))
                    continue;

                MapProperties props = obj.getProperties();

                // Default MapProperties are stored within associated Tile object
                // Instance-specific overrides are stored in MapObject

                TiledMapTileMapObject tmtmo = (TiledMapTileMapObject) obj;
                TiledMapTile t = tmtmo.getTile();
                MapProperties defaultProps = t.getProperties();

                if (defaultProps.containsKey("name") && defaultProps.get("name").equals(propertyName))
                    list.add(obj);

                // get list of default property keys
                Iterator<String> propertyKeys = defaultProps.getKeys();

                // iterate over keys; copy default values into props if needed
                while (propertyKeys.hasNext()) {
                    String key = propertyKeys.next();

                    // check if value already exists; if not, create property with default value
                    if (props.containsKey(key))
                        continue;
                    else {
                        Object value = defaultProps.get(key);
                        props.put(key, value);
                    }
                }
            }
        }
        return list;
    }

    public void draw(Batch batch, float parentAlpha) {
        tiledMapRenderer.setView((OrthographicCamera) getStage().getCamera());
        tiledMapRenderer.render();
    }

    private void calculateFocalPoint() {
        int tileWidth          = (int)tiledMap.getProperties().get("tilewidth");
        int tileHeight         = (int)tiledMap.getProperties().get("tileheight");
        int numTilesHorizontal = (int)tiledMap.getProperties().get("width");
        int numTilesVertical   = (int)tiledMap.getProperties().get("height");
        int mapWidth  = tileWidth  * numTilesHorizontal;
        int mapHeight = tileHeight * numTilesVertical;
        focalPoint = new Vector2(mapWidth / 2, mapHeight / 2);
    }

    private void setFocalPoint() {
        OrthographicCamera camera = (OrthographicCamera) getStage().getViewport().getCamera();
        camera.position.set(new Vector3(
                TilemapActor.focalPoint.x * BaseGame.unitScale,
                TilemapActor.focalPoint.y * BaseGame.unitScale,
                0f
        ));
        camera.update();
    }
}

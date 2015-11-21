package me.tomassetti.civs.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import me.tomassetti.civs.model.Band;
import me.tomassetti.civs.model.Position;
import me.tomassetti.civs.simulation.Simulation;

import java.io.File;
import java.util.*;

import static org.worldengine.world.WorldFileMsgPackLoaderKt.loadFromMsgPack;
import static me.tomassetti.civs.logic.LogicKt.*;


public class TileNavApp extends ApplicationAdapter implements InputProcessor {

    private final static int N_INITIAL_TRIBES = 500;

    TiledMap tiledMap;
    public static OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    long mapWidthInPixels;
    long mapHeightInPixels;
    TiledMapTileLayer groundLayer;
    TiledMapTile tentTile;
    TiledMapTile emptyTile;

    List<TiledMapTileLayer> decorationLayers = new ArrayList<>();
    List<TiledMapTileLayer> terrainLayers = new ArrayList<>();
    Map<Position, TiledMapTile> originalTiles = new HashMap<>();

    private TiledMapTileLayer getDecorationLayer(int x, int y) {
        for (int i=0; i<terrainLayers.size();i++){
            TiledMapTileLayer.Cell cell = terrainLayers.get(i).getCell(x, y);
            if (cell != null) {
                return decorationLayers.get(i);
            }
        }
        throw new UnsupportedOperationException("No layers found at "+x+", " + y);
    }

    private void preserving(Position position) {
        TiledMapTileLayer layer = getDecorationLayer(position.getX(), position.getY());
        TiledMapTileLayer.Cell prevCell = layer.getCell(position.getX(), position.getY());
        TiledMapTile prevTile = prevCell == null ? emptyTile : prevCell.getTile();
        if (!originalTiles.containsKey(position)) {
            originalTiles.put(position, prevTile);
        }
    }

    private void settingTent(Position position) {
        preserving(position);
        TiledMapTileLayer.Cell tentCell = new TiledMapTileLayer.Cell();
        tentCell.setTile(tentTile);
        TiledMapTileLayer layer = getDecorationLayer(position.getX(), position.getY());
        layer.setCell(position.getX(), position.getY(), tentCell);
    }

    private void restore(Position position) {
        TiledMapTileLayer oldLayer = getDecorationLayer(position.getX(), position.getY());
        TiledMapTileLayer.Cell restoredCell = new TiledMapTileLayer.Cell();
        restoredCell.setTile(originalTiles.get(position));
        oldLayer.setCell(position.getX(), position.getY(), restoredCell);
    }

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.position.set(w / 2f, h / 2f, 0);
        camera.update();
        tiledMap = new TmxMapLoader().load("tiled_seed_18000_comp.tmx");

        Simulation.INSTANCE.setWorld(loadFromMsgPack(new File("/home/federico/repos/worldengine/seed_18000.world")));
        //tiledMap = new TmxMapLoader().load("tiled_seed_124.tmx");
        //Simulation.INSTANCE.setWorld(loadFromMsgPack(new File("/home/federico/repos/worldengine/seed_124.world")));

        groundLayer = (TiledMapTileLayer) tiledMap.getLayers().get("decoration ground");
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration high mountain"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration half high mountain"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration med mountain"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration half med mountain"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration low mountain"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration half low mountain"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration hill"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration half hill"));
        decorationLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("decoration ground"));

        for (int i=0; i<decorationLayers.size(); i++) {
            if (decorationLayers.get(i) == null) {
                throw new RuntimeException("LVL " + i);
            }
        }

        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("high_mountain"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("high_mountain half"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("med_mountain"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("med_mountain half"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("low_mountain"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("low_mountain half"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("hill"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("hill half"));
        terrainLayers.add((TiledMapTileLayer) tiledMap.getLayers().get("ground"));

        for (int i=0; i<terrainLayers.size(); i++) {
            if (terrainLayers.get(i) == null) {
                throw new RuntimeException("LVL " + i);
            }
        }

        tentTile = tiledMap.getTileSets().getTileSet("256_decor").getTile(133);
        emptyTile = tiledMap.getTileSets().getTileSet("256_decor").getTile(256);


        createBands(Simulation.INSTANCE.getWorld(), N_INITIAL_TRIBES);
        Simulation.INSTANCE.bandsCopy().forEach(b -> settingTent(b.getPosition()));

        tiledMapRenderer = new MyTiledMapRendered(tiledMap, new LayerFinder() {
            @Override
            public TiledMapTileLayer decorationLayer(int x, int y) {
                return getDecorationLayer(x, y);
            }
        });
        Gdx.input.setInputProcessor(this);


        mapWidthInPixels = (long)(groundLayer.getTileWidth() * (Integer)tiledMap.getProperties().get("width"));
        mapHeightInPixels = (long)(groundLayer.getTileHeight() * (Integer)tiledMap.getProperties().get("height"));

        Random random = new Random(1);

        TimerTask updateTask = new TimerTask() {
            @Override
            public void run() {
                Simulation.INSTANCE.nextTurn();
                List<Band> newBands = new ArrayList<>();
                for (Band band : Simulation.INSTANCE.bandsCopy()) {
                    restore(band.getPosition());

                    me.tomassetti.civs.logic.PopulationKt.updatePopulation(band, Simulation.INSTANCE.getWorld(), random);

                    if (band.isAlive()) {
                        band.setPosition(me.tomassetti.civs.logic.LogicKt.determineNewPosition(band, Simulation.INSTANCE.getWorld(), random));
                        Position newPos = band.getPosition();
                        settingTent(newPos);

                        if (decideIfSplit(band)) {
                            Band newBand = split(band);
                            newBands.add(newBand);
                        }
                    } else {
                        restore(band.getPosition());
                    }
                }
                newBands.stream().forEach(b -> Simulation.INSTANCE.addBand(b));
            }
        };
        new Timer().scheduleAtFixedRate(updateTask, 5000, 1000);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        tiledMapRenderer.setView(camera);
        tiledMapRenderer.render();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        final float MOVEMENT_INC = 32.0f;
        final float ZOOM_INC = 2.0f;
        final float MIN_ZOOM = 0.5f;
        final float MAX_ZOOM = 512.0f;

        int movInc = (int)(MOVEMENT_INC * camera.zoom);

        if (keycode == Input.Keys.A) {
            camera.zoom *= ZOOM_INC;
        }
        if (keycode == Input.Keys.Q) {
            camera.zoom /= ZOOM_INC;
        }
        if (keycode == Input.Keys.LEFT) {
            camera.translate(-movInc, 0);
        }
        if (keycode == Input.Keys.RIGHT) {
            camera.translate(movInc, 0);
        }
        if (keycode == Input.Keys.UP) {
            camera.translate(0, movInc);
        }
        if (keycode == Input.Keys.DOWN) {
            camera.translate(0, -movInc);
        }

        camera.zoom = Math.max(camera.zoom, MIN_ZOOM);
        camera.zoom = Math.min(camera.zoom, MAX_ZOOM);

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
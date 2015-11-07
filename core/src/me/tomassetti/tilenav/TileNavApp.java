package me.tomassetti.tilenav;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;

public class TileNavApp extends ApplicationAdapter implements InputProcessor {
    Texture img;
    TiledMap tiledMap;
    OrthographicCamera camera;
    TiledMapRenderer tiledMapRenderer;
    long mapWidthInPixels;
    long mapHeightInPixels;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.position.set(w / 2f, h / 2f, 0);
        camera.update();
        //tiledMap = new TmxMapLoader().load("tiled_seed_54925.tmx");
        tiledMap = new TmxMapLoader().load("tiled_seed_888_comp.tmx");
        tiledMapRenderer = new MyTiledMapRendered(tiledMap);
        Gdx.input.setInputProcessor(this);

        TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get(0);
        mapWidthInPixels = (long)(layer.getTileWidth() * (Integer)tiledMap.getProperties().get("width"));
        mapHeightInPixels = (long)(layer.getTileHeight() * (Integer)tiledMap.getProperties().get("height"));
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
        int movInc = (int)(32.0f * camera.zoom);

        if (keycode == Input.Keys.A) {
            camera.zoom *= 2.0f;
        }
        if (keycode == Input.Keys.Q) {
            camera.zoom /= 2.0f;
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

        camera.zoom = Math.max(camera.zoom, 0.25f);
        camera.zoom = Math.min(camera.zoom, 32.0f);

        float effectiveViewportWidth = camera.viewportWidth * camera.zoom;
        float effectiveViewportHeight = camera.viewportHeight * camera.zoom;

        //camera.position.x = Math.max(effectiveViewportWidth/2.0f, camera.position.x);
        //camera.position.y = Math.max(effectiveViewportHeight/2.0f, camera.position.y);
        //camera.position.x = Math.min(mapWidthInPixels - effectiveViewportWidth / 2.0f, camera.position.x);
        //camera.position.y = Math.min(mapHeightInPixels - effectiveViewportHeight / 2.0f, camera.position.y);

        System.out.println("=== UPDATE ===");
        System.out.println("Position " + camera.position.x + ", "+camera.position.y);
        System.out.println("Effective viewport " + effectiveViewportWidth + " x " + effectiveViewportHeight);

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
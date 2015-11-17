package me.tomassetti.civs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import me.tomassetti.civs.model.Band;
import me.tomassetti.civs.simulation.Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.badlogic.gdx.graphics.g2d.Batch.*;

public class MyTiledMapRendered extends BatchTiledMapRenderer {

    private Matrix4 isoTransform;
    private Matrix4 invIsotransform;
    private Vector3 screenPos = new Vector3();

    private Vector2 topRight = new Vector2();
    private Vector2 bottomLeft = new Vector2();
    private Vector2 topLeft = new Vector2();
    private Vector2 bottomRight = new Vector2();

    private BitmapFont font;
    private LayerFinder layerFinder;

    public MyTiledMapRendered(TiledMap map, LayerFinder layerFinder) {
        super(map);
        init();
        this.layerFinder = layerFinder;
    }

    private void init () {
        // create the isometric transform
        isoTransform = new Matrix4();
        isoTransform.idt();

        // isoTransform.translate(0, 32, 0);
        isoTransform.scale((float)(Math.sqrt(2.0) / 2.0), (float)(Math.sqrt(2.0) / 4.0), 1.0f);
        isoTransform.rotate(0.0f, 0.0f, 1.0f, -45);

        // ... and the inverse matrix
        invIsotransform = new Matrix4(isoTransform);
        invIsotransform.inv();

        font = new BitmapFont();
        font.setColor(Color.BLACK);
        font.getData().setScale(3.0f, 3.0f);
    }

    private Vector3 translateScreenToIso (Vector2 vec) {
        screenPos.set(vec.x, vec.y, 0);
        screenPos.mul(invIsotransform);

        return screenPos;
    }

    private float getOffsetY(TiledMapTileLayer layer) {
        if (layer.getName().replaceAll("_", " ").contains("high mountain")) {
            if (layer.getName().contains("half")) {
                return -48.0f * 7;
            } else {
                return -48.0f * 8;
            }
        } else if (layer.getName().replaceAll("_", " ").contains("low mountain")) {
            if (layer.getName().contains("half")) {
                return -48.0f * 5;
            } else {
                return -48.0f * 6;
            }
        } else if (layer.getName().replaceAll("_", " ").contains("med mountain")) {
            if (layer.getName().contains("half")) {
                return -48.0f * 3;
            } else {
                return -48.0f * 4;
            }
        } else if (layer.getName().replaceAll("_", " ").contains("hill")) {
            if (layer.getName().contains("half")) {
                return -48.0f * 1;
            } else {
                return -48.0f * 2;
            }
        } else {
            if (!layer.getName().contains("ground")) {
                throw new RuntimeException(layer.getName());
            }
            return 0.0f;
        }
    }

    @Override
    public void renderTileLayer(TiledMapTileLayer layer) {
        final Color batchColor = batch.getColor();
        final float color = Color.toFloatBits(batchColor.r, batchColor.g, batchColor.b, batchColor.a * layer.getOpacity());

        float tileWidth = layer.getTileWidth() * unitScale;
        float tileHeight = layer.getTileHeight() * unitScale;
        float halfTileWidth = tileWidth * 0.5f;
        float halfTileHeight = tileHeight * 0.5f;

        float layerOffsetY = getOffsetY(layer);

        // setting up the screen points
        // COL1
        topRight.set(viewBounds.x + viewBounds.width, viewBounds.y);
        // COL2
        bottomLeft.set(viewBounds.x, viewBounds.y + viewBounds.height);
        // ROW1
        topLeft.set(viewBounds.x, viewBounds.y);
        // ROW2
        bottomRight.set(viewBounds.x + viewBounds.width, viewBounds.y + viewBounds.height);

        // transforming screen coordinates to iso coordinates
        int row1 = (int)(translateScreenToIso(topLeft).y / tileWidth) - 2;
        int row2 = (int)(translateScreenToIso(bottomRight).y / tileWidth) + 2;

        int col1 = (int)(translateScreenToIso(bottomLeft).x / tileWidth) - 2;
        int col2 = (int)(translateScreenToIso(topRight).x / tileWidth) + 2;

        for (int row = row2; row >= row1; row--) {
            for (int col = col1; col <= col2; col++) {
                float x = (col * halfTileWidth) + (row * halfTileWidth);
                float y = (row * halfTileHeight) - (col * halfTileHeight);

                final TiledMapTileLayer.Cell cell = layer.getCell(col, row);
                if (cell == null) continue;
                final TiledMapTile tile = cell.getTile();

                if (tile != null) {
                    final boolean flipX = cell.getFlipHorizontally();
                    final boolean flipY = cell.getFlipVertically();
                    final int rotations = cell.getRotation();

                    TextureRegion region = tile.getTextureRegion();

                    float x1 = x + tile.getOffsetX() * unitScale;
                    float y1 = y + tile.getOffsetY() * unitScale - layerOffsetY;
                    float x2 = x1 + region.getRegionWidth() * unitScale;
                    float y2 = y1 + region.getRegionHeight() * unitScale;

                    float u1 = region.getU();
                    float v1 = region.getV2();
                    float u2 = region.getU2();
                    float v2 = region.getV();

                    vertices[X1] = x1;
                    vertices[Y1] = y1;
                    vertices[C1] = color;
                    vertices[U1] = u1;
                    vertices[V1] = v1;

                    vertices[X2] = x1;
                    vertices[Y2] = y2;
                    vertices[C2] = color;
                    vertices[U2] = u1;
                    vertices[V2] = v2;

                    vertices[X3] = x2;
                    vertices[Y3] = y2;
                    vertices[C3] = color;
                    vertices[U3] = u2;
                    vertices[V3] = v2;

                    vertices[X4] = x2;
                    vertices[Y4] = y1;
                    vertices[C4] = color;
                    vertices[U4] = u2;
                    vertices[V4] = v1;

                    if (flipX) {
                        float temp = vertices[U1];
                        vertices[U1] = vertices[U3];
                        vertices[U3] = temp;
                        temp = vertices[U2];
                        vertices[U2] = vertices[U4];
                        vertices[U4] = temp;
                    }
                    if (flipY) {
                        float temp = vertices[V1];
                        vertices[V1] = vertices[V3];
                        vertices[V3] = temp;
                        temp = vertices[V2];
                        vertices[V2] = vertices[V4];
                        vertices[V4] = temp;
                    }
                    if (rotations != 0) {
                        switch (rotations) {
                            case TiledMapTileLayer.Cell.ROTATE_90: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V2];
                                vertices[V2] = vertices[V3];
                                vertices[V3] = vertices[V4];
                                vertices[V4] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U2];
                                vertices[U2] = vertices[U3];
                                vertices[U3] = vertices[U4];
                                vertices[U4] = tempU;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_180: {
                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U3];
                                vertices[U3] = tempU;
                                tempU = vertices[U2];
                                vertices[U2] = vertices[U4];
                                vertices[U4] = tempU;
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V3];
                                vertices[V3] = tempV;
                                tempV = vertices[V2];
                                vertices[V2] = vertices[V4];
                                vertices[V4] = tempV;
                                break;
                            }
                            case TiledMapTileLayer.Cell.ROTATE_270: {
                                float tempV = vertices[V1];
                                vertices[V1] = vertices[V4];
                                vertices[V4] = vertices[V3];
                                vertices[V3] = vertices[V2];
                                vertices[V2] = tempV;

                                float tempU = vertices[U1];
                                vertices[U1] = vertices[U4];
                                vertices[U4] = vertices[U3];
                                vertices[U3] = vertices[U2];
                                vertices[U2] = tempU;
                                break;
                            }
                        }
                    }
                    batch.draw(region.getTexture(), vertices, 0, NUM_VERTICES);
                }
            }
        }
    }

    @Override
    public void render () {
        beginRender();
        Map<MapLayer, List<Band>> bandsByLayer = divideBandsByLayer();
        for (MapLayer layer : map.getLayers()) {
            if (layer.isVisible()) {
                if (layer instanceof TiledMapTileLayer) {
                    renderTileLayer((TiledMapTileLayer)layer);
                    renderLabels(bandsByLayer.get(layer), (TiledMapTileLayer)layer);
                } if (layer instanceof TiledMapImageLayer) {
                    renderImageLayer((TiledMapImageLayer)layer);
                } else {
                    renderObjects(layer);
                }
            }
        }
        endRender();
        renderHui();
    }

    private void renderHui() {
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), 50);
        shapeRenderer.setColor(0, 1, 1, 1);
        shapeRenderer.rect(0, 49, Gdx.graphics.getWidth(), 2);
        shapeRenderer.end();


        BitmapFont huiFont = new BitmapFont();
        huiFont.setColor(Color.WHITE);
        huiFont.getData().setScale(1.0f, 1.0f);

        SpriteBatch huiBatch = new SpriteBatch();
        huiBatch.begin();
        huiFont.draw(huiBatch, "TURN " + Simulation.INSTANCE.getTurn(), 30, 30);
        huiFont.draw(huiBatch, "BANDS " + Simulation.INSTANCE.nBandsAlive(), 180, 30);
        huiFont.draw(huiBatch, "POP " + Simulation.INSTANCE.totalPopulation(), 330, 30);
        huiBatch.end();
    }

    private void renderLabels(List<Band> bands, TiledMapTileLayer layer) {
        if (bands == null) {
            return;
        }
        float tileWidth = layer.getTileWidth() * unitScale;
        float tileHeight = layer.getTileHeight() * unitScale;
        float halfTileWidth = tileWidth * 0.5f;
        float halfTileHeight = tileHeight * 0.5f;
        float layerOffsetY = getOffsetY(layer);
        for (Band band : bands) {
            if (band.isAlive()) {
                drawText(halfTileWidth, halfTileHeight, layerOffsetY,
                        band.getPosition().getX(), band.getPosition().getY(),
                        band.getName() + " (" + band.getPopulation().total() + ")");
            }
        }
    }

    private Map<MapLayer, List<Band>> divideBandsByLayer() {
        Map<MapLayer, List<Band>> bandsByLayer = new HashMap<>();
        for (Band band : Simulation.INSTANCE.getBands()) {
            MapLayer layer = layerFinder.decorationLayer(band.getPosition().getX(), band.getPosition().getY());
            if (!bandsByLayer.containsKey(layer)) {
                bandsByLayer.put(layer, new ArrayList<>());
            }
            bandsByLayer.get(layer).add(band);
        }
        return bandsByLayer;
    }

    private void drawText(float halfTileWidth, float halfTileHeight, float layerOffsetY, int textCol, int textRow, String text) {
        GlyphLayout layout = new GlyphLayout(font, text);
        float textX = halfTileWidth + (textCol * halfTileWidth) + (textRow * halfTileWidth)  - layout.width / 2;
        float textY = (halfTileHeight*3) + (textRow * halfTileHeight) - (textCol * halfTileHeight)  - layerOffsetY;

        font.draw(batch, text, textX, textY);
    }
}

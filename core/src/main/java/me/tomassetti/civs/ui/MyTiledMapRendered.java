package me.tomassetti.civs.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapImageLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.BatchTiledMapRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.collision.Ray;
import com.badlogic.gdx.scenes.scene2d.Stage;
import kotlin.Pair;
import me.tomassetti.civs.model.Band;
import me.tomassetti.civs.simulation.Simulation;

import java.util.*;

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
        if (TileNavApp.camera.zoom > 8.0f) {
            if (!layer.getName().equals("ground")) {
                return;
            }
        }


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

        int step = -1;
        if (TileNavApp.camera.zoom > 8.0f) {
            step = (int)(TileNavApp.camera.zoom/8);
        }

        for (int row = row2; row >= row1; row--) {
            if (step != -1 && row % step != 0) {
                continue;
            }
            for (int col = col1; col <= col2; col++) {
                if (step != -1 && col % step != 0) {
                    continue;
                }
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
                    if (step != -1) {
                        x2 = x1 + region.getRegionWidth() * unitScale * step;
                        y2 = y1 + region.getRegionHeight() * unitScale * step;
                    }

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
        //stage.act();
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
        checkTileTouched();
        renderBottomHui();
        renderLateralHui();
    }

    private void renderBottomHui() {
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

    private void renderLateralHui() {
        int panelWidth = 175;
        int startX = Gdx.graphics.getWidth() - panelWidth;

        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);
        shapeRenderer.rect(startX, 49, panelWidth, Gdx.graphics.getHeight() - 49);
        shapeRenderer.setColor(0, 1, 1, 1);
        shapeRenderer.rect(startX, 49, 2, Gdx.graphics.getHeight() - 49);
        shapeRenderer.end();
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
        for (Band band : new ArrayList<>(Simulation.INSTANCE.bandsCopy())) {
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

    private Optional<Pair<Integer, Integer>> selectedTile = Optional.empty();

    private void highlightCell(Pair<Integer, Integer> cellCoords) {
        float fx = TileNavApp.camera.viewportWidth/Gdx.graphics.getWidth();
        float fy = TileNavApp.camera.viewportHeight/Gdx.graphics.getHeight();

        float baseX = - TileNavApp.camera.position.x / (fx*TileNavApp.camera.zoom) + ((TileNavApp.camera.viewportWidth/fx)/2);/*+ (cellCoords.getSecond() * fx + TileNavApp.camera.viewportWidth/2) * TileNavApp.camera.zoom*/;
        float baseY = - (TileNavApp.camera.position.y - 64) / (fy*TileNavApp.camera.zoom) + (TileNavApp.camera.viewportHeight/fy)/2;
        baseX += 128.0/(fx*TileNavApp.camera.zoom) * (cellCoords.getSecond() + cellCoords.getFirst());
        baseY += 64/(fy*TileNavApp.camera.zoom) * (-cellCoords.getSecond() + cellCoords.getFirst());
        //baseX = fx;
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.line(baseX, 0 + baseY, baseX + 128.0f/(TileNavApp.camera.zoom*fx), baseY + 64.0f/(TileNavApp.camera.zoom*fy));
        shapeRenderer.line(baseX, 0 + baseY, baseX + 128.0f/(TileNavApp.camera.zoom*fx), baseY - 64.0f/(TileNavApp.camera.zoom*fy));
        shapeRenderer.line(baseX + 128.0f/(TileNavApp.camera.zoom*fx), 0 + baseY - 64.0f/(TileNavApp.camera.zoom*fy), baseX + 256.0f/(TileNavApp.camera.zoom*fx), baseY);
        shapeRenderer.line(baseX + 128.0f/(TileNavApp.camera.zoom*fx), 0 + baseY + 64.0f/(TileNavApp.camera.zoom*fy), baseX + 256.0f/(TileNavApp.camera.zoom*fx), baseY);
        shapeRenderer.end();

    }

    private void checkTileTouched() {
        Pair<Integer, Integer> cellCoords = new CellCoordinatesCalculator().findCellCoords(
                TileNavApp.camera.viewportWidth, TileNavApp.camera.viewportHeight,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
                TileNavApp.camera.position.x, TileNavApp.camera.position.y, TileNavApp.camera.zoom,
                Gdx.input.getX(), Gdx.input.getY());
        if (cellCoords.getFirst() < 0 || cellCoords.getSecond() < 0) {
            return;
        }
        selectedTile = Optional.of(cellCoords);
        SpriteBatch huiBatch = new SpriteBatch();
        BitmapFont huiFont = new BitmapFont();
        huiFont.setColor(Color.WHITE);
        huiFont.getData().setScale(1.0f, 1.0f);

        highlightCell(cellCoords);

        huiBatch.begin();
        //huiFont.draw(huiBatch, ("VIEWPORT WIDTH="+TileNavApp.camera.viewportWidth+", HEIGHT="+TileNavApp.camera.viewportHeight), 30, Gdx.graphics.getHeight() - 10);
        //huiFont.draw(huiBatch, ("CAMERA X="+TileNavApp.camera.position.x+", Y="+TileNavApp.camera.position.y + ", ZOOM="+TileNavApp.camera.zoom), 30, Gdx.graphics.getHeight() - 35);
        //huiFont.draw(huiBatch, ("SCREEN WIDTH="+Gdx.graphics.getWidth()+", HEIGHT="+Gdx.graphics.getHeight()), 30, Gdx.graphics.getHeight() - 60);
        //huiFont.draw(huiBatch, ("INPUT X="+Gdx.input.getX()+", Y="+Gdx.input.getY()), 30, Gdx.graphics.getHeight() - 90);
        huiFont.draw(huiBatch, ("ROW="+cellCoords.getFirst()+", COL="+cellCoords.getSecond()), 30, Gdx.graphics.getHeight() - 10);
        huiBatch.end();
    }

    /*


    private boolean existLevel(Pair<Integer, Integer> coords, int level) {
        return level == 0;
    }

    private Optional<Pair<Integer, Integer>> findCellCoords(float x, float y, int level) {
       //y -= 48 * level * TileNavApp.camera.zoom;
        int normalizedX = (int)(x/128);
        int normalizedY = (int)(y/64) - 1;
        int restX = (int)(x/2 - normalizedX * 64);
        int restY = (int)(y - normalizedY * 64) - 64;
        if (restY < 0) {
            restY += 64;
        }
        Pair<Integer, Integer> coords = new CellCoordinatesCalculator().cellCoords(normalizedX, normalizedY, restX, restY);
        if (existLevel(coords, level)) {
            return Optional.of(coords);
        } else {
            return Optional.empty();
        }
    }

    private static int TOP_LAYER = 10;
    private static int BOTTOM_LAYER = 0;

    private Pair<Integer, Integer> findCellCoords(float x, float y) {
        for (int level=TOP_LAYER;level>=BOTTOM_LAYER;level--) {
            Optional<Pair<Integer, Integer>> res = findCellCoords(x, y, level);
            if (res.isPresent()) {
                System.out.println("FIND AT LEVEL " + level);
                return res.get();
            }
        }

        throw new RuntimeException();
    }*/

}

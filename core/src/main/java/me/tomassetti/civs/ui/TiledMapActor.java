package me.tomassetti.civs.ui;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TiledMapActor extends Actor {

    private TiledMap tiledMap;

    private TiledMapTileLayer tiledLayer;

    private TiledMapTileLayer.Cell cell;

    public TiledMap getTiledMap() {
        return tiledMap;
    }

    public TiledMapTileLayer getTiledLayer() {
        return tiledLayer;
    }

    public TiledMapTileLayer.Cell getCell() {
        return cell;
    }

    public TiledMapActor(TiledMap tiledMap, TiledMapTileLayer tiledLayer, TiledMapTileLayer.Cell cell) {
        this.tiledMap = tiledMap;
        this.tiledLayer = tiledLayer;
        this.cell = cell;
    }

}
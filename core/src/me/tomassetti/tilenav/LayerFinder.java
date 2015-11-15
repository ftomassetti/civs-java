package me.tomassetti.tilenav;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;
import java.util.List;

public interface LayerFinder {

    TiledMapTileLayer decorationLayer(int x, int y);
}

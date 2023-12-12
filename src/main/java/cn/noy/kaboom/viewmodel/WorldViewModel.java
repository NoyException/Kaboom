package cn.noy.kaboom.viewmodel;

import cn.noy.kaboom.common.TileType;
import cn.noy.kaboom.model.world.World;

public class WorldViewModel extends ViewModel{
    private final World world;

    private final TileType[][][] tiles;

    public WorldViewModel(World world){
        this.world = world;
        this.tiles = world.getTiles();
    }

    @Override
    public void onAttached() {
        super.onAttached();
        getPropertyChangeSupport().firePropertyChange("tiles", null, tiles);
    }
}

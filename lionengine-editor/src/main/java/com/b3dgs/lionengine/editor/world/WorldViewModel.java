/*
 * Copyright (C) 2013-2014 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.b3dgs.lionengine.editor.world;

import com.b3dgs.lionengine.game.CameraGame;
import com.b3dgs.lionengine.game.FactoryObjectGame;
import com.b3dgs.lionengine.game.entity.EntityGame;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.map.TileGame;

/**
 * Contains the objects of the world.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public enum WorldViewModel
{
    /** Instance. */
    INSTANCE;

    /** Camera reference. */
    private final CameraGame camera;
    /** Map reference. */
    private MapTile<? extends TileGame> map;
    /** Factory entity reference. */
    private FactoryObjectGame<?, ?> factoryEntity;
    /** Selected entity class. */
    private Class<? extends EntityGame> selectedEntity;

    /**
     * Constructor.
     */
    private WorldViewModel()
    {
        camera = new CameraGame();
    }

    /**
     * Set the map reference.
     * 
     * @param map The map reference.
     */
    public void setMap(MapTile<? extends TileGame> map)
    {
        this.map = map;
    }

    /**
     * Set the factory entity reference.
     * 
     * @param factoryEntity The factory entity reference.
     */
    public void setFactoryEntity(FactoryObjectGame<?, ?> factoryEntity)
    {
        this.factoryEntity = factoryEntity;
    }

    /**
     * Set the selected entity type.
     * 
     * @param selectedEntity The selected entity type.
     */
    public void setSelectedEntity(Class<? extends EntityGame> selectedEntity)
    {
        this.selectedEntity = selectedEntity;
    }

    /**
     * Get the camera reference.
     * 
     * @return The camera reference.
     */
    public CameraGame getCamera()
    {
        return camera;
    }

    /**
     * Get the map reference.
     * 
     * @return The map reference.
     */
    public MapTile<? extends TileGame> getMap()
    {
        return map;
    }

    /**
     * Get the factory entity reference.
     * 
     * @return The factory entity reference.
     */
    public FactoryObjectGame<?, ?> getFactoryEntity()
    {
        return factoryEntity;
    }

    /**
     * Get the selected entity type.
     * 
     * @return The selected entity type.
     */
    public Class<? extends EntityGame> getSelectedEntity()
    {
        return selectedEntity;
    }
}

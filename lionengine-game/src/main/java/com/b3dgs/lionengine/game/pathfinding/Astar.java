/*
 * Copyright (C) 2013-2016 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.pathfinding;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.map.MapTile;

/**
 * A star factory.
 */
public final class Astar
{
    /**
     * Create a path finder.
     * 
     * @param map The map to be searched. Must have the {@link com.b3dgs.lionengine.game.pathfinding.MapTilePath}
     *            feature.
     * @param maxSearchDistance The maximum depth we'll search before giving up.
     * @param heuristic The heuristic used to determine the search order of the map.
     * @return The path finder instance.
     */
    public static PathFinder createPathFinder(MapTile map, int maxSearchDistance, Heuristic heuristic)
    {
        return new PathFinderImpl(map, maxSearchDistance, heuristic);
    }

    /**
     * Create the closest heuristic.
     * 
     * @return The closest heuristic.
     */
    public static Heuristic createHeuristicClosest()
    {
        return new HeuristicClosest();
    }

    /**
     * Create the closest squared heuristic.
     * 
     * @return The closest squared heuristic.
     */
    public static Heuristic createHeuristicClosestSquared()
    {
        return new HeuristicClosestSquared();
    }

    /**
     * Create the closest heuristic.
     * 
     * @param minimumCost The minimum cost value.
     * @return The closest heuristic.
     */
    public static Heuristic createHeuristicManhattan(int minimumCost)
    {
        return new HeuristicManhattan(minimumCost);
    }

    /**
     * Private constructor.
     */
    private Astar()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }
}

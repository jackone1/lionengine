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
package com.b3dgs.lionengine.game.tile;

import java.util.Collection;
import java.util.HashSet;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.game.Orientation;

/**
 * Represents a map tile constraint. It describes the allowed tiles for the specified orientation.
 */
public class TileConstraint
{
    /** Constraint orientation. */
    private final Orientation orientation;
    /** Allowed tiles. */
    private final Collection<TileRef> allowed = new HashSet<TileRef>();

    /**
     * Create constraint.
     * 
     * @param orientation The orientation.
     * @throws LionEngineException If <code>null</code> argument.
     */
    public TileConstraint(Orientation orientation)
    {
        Check.notNull(orientation);

        this.orientation = orientation;
    }

    /**
     * Add an allowed tile.
     * 
     * @param tile The allowed tile reference.
     */
    public void add(TileRef tile)
    {
        allowed.add(tile);
    }

    /**
     * Get the allowed tiles for this orientation.
     * 
     * @return The allowed tiles.
     */
    public Collection<TileRef> getAllowed()
    {
        return allowed;
    }

    /**
     * Get the constraint orientation.
     * 
     * @return The constraint orientation.
     */
    public Orientation getOrientation()
    {
        return orientation;
    }

    /*
     * Object
     */

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + allowed.hashCode();
        result = prime * result + orientation.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof TileConstraint))
        {
            return false;
        }
        final TileConstraint other = (TileConstraint) obj;
        return orientation == other.orientation && allowed.equals(other.allowed);
    }

    @Override
    public String toString()
    {
        return new StringBuilder().append(orientation).append(Constant.SPACE).append(allowed).toString();
    }
}

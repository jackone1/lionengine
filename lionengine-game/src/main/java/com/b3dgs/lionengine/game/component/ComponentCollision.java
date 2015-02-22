/*
 * Copyright (C) 2013-2015 Byron 3D Games Studio (www.b3dgs.com) Pierre-Alexandre (contact@b3dgs.com)
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
package com.b3dgs.lionengine.game.component;

import com.b3dgs.lionengine.game.Collision;
import com.b3dgs.lionengine.game.handler.HandledObjects;
import com.b3dgs.lionengine.game.trait.Collidable;

/**
 * Default collision component implementation. Designed to check collision between {@link Collidable}.
 * Collision events are notified to {@link ComponentCollisionListener}.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class ComponentCollision
        implements ComponentUpdatable
{
    /**
     * Create a collision component.
     */
    public ComponentCollision()
    {
        // Nothing to do
    }

    /*
     * Component
     */

    @Override
    public void update(double extrp, HandledObjects objects)
    {
        final Iterable<Collidable> collidables = objects.get(Collidable.class);
        for (final Collidable objectA : collidables)
        {
            for (final Collidable objectB : collidables)
            {
                if (objectA != objectB)
                {
                    final Collision collision = objectA.collide(objectB);
                    if (collision != null)
                    {
                        objectB.notifyCollided(objectA);
                    }
                }
            }
        }
    }
}
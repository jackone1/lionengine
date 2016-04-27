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

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.test.UtilTests;

/**
 * Test the tile group type enum.
 */
public class TileGroupTypeTest
{
    /**
     * Test the enum.
     * 
     * @throws Exception If error.
     */
    @Test
    public void testEnum() throws Exception
    {
        UtilTests.testEnum(TileGroupType.class);
    }

    /**
     * Test the enum creation from string.
     */
    @Test
    public void testFromString()
    {
        for (final TileGroupType type : TileGroupType.values())
        {
            Assert.assertEquals(type, TileGroupType.from(type.name()));
        }
    }

    /**
     * Test the enum creation from string.
     */
    @Test(expected = LionEngineException.class)
    public void testFromStringInvalid()
    {
        Assert.assertNull(TileGroupType.from("null"));
    }
}

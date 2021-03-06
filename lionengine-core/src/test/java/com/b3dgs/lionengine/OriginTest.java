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
package com.b3dgs.lionengine;

import org.junit.Assert;
import org.junit.Test;

import com.b3dgs.lionengine.test.UtilEnum;
import com.b3dgs.lionengine.test.UtilTests;

/**
 * Test the origin class.
 */
public class OriginTest
{
    /**
     * Test the enum.
     * 
     * @throws Exception If error.
     */
    @Test
    public void testEnum() throws Exception
    {
        UtilTests.testEnum(Origin.class);
    }

    /**
     * Test the origin enum switch.
     */
    @Test
    public void testEnumSwitch()
    {
        for (final Origin origin : Origin.values())
        {
            switch (origin)
            {
                case TOP_LEFT:
                    Assert.assertEquals(3.0, origin.getX(3, 2), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 2), 0.001);

                    Assert.assertEquals(3.0, origin.getX(3, 0), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 0), 0.001);
                    break;
                case BOTTOM_LEFT:
                    Assert.assertEquals(3.0, origin.getX(3, 2), 0.001);
                    Assert.assertEquals(1.0, origin.getY(3, 2), 0.001);

                    Assert.assertEquals(3.0, origin.getX(3, 0), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 0), 0.001);
                    break;
                case BOTTOM_RIGHT:
                    Assert.assertEquals(1.0, origin.getX(3, 2), 0.001);
                    Assert.assertEquals(1.0, origin.getY(3, 2), 0.001);

                    Assert.assertEquals(3.0, origin.getX(3, 0), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 0), 0.001);
                    break;
                case CENTER_TOP:
                    Assert.assertEquals(2.0, origin.getX(3, 2), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 2), 0.001);

                    Assert.assertEquals(3.0, origin.getX(3, 0), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 0), 0.001);
                    break;
                case CENTER_BOTTOM:
                    Assert.assertEquals(2.0, origin.getX(3, 2), 0.001);
                    Assert.assertEquals(1.0, origin.getY(3, 2), 0.001);

                    Assert.assertEquals(3.0, origin.getX(3, 0), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 0), 0.001);
                    break;
                case MIDDLE:
                    Assert.assertEquals(2.0, origin.getX(3, 2), 0.001);
                    Assert.assertEquals(2.0, origin.getY(3, 2), 0.001);

                    Assert.assertEquals(3.0, origin.getX(3, 0), 0.001);
                    Assert.assertEquals(3.0, origin.getY(3, 0), 0.001);
                    break;
                default:
                    Assert.fail();
            }
        }
    }

    /**
     * Test the origin enum fail.
     */
    @Test
    public void testEnumFail()
    {
        final UtilEnum<Origin> hack = new UtilEnum<Origin>(Origin.class, Origin.class);
        final Origin fail = hack.make("FAIL");

        try
        {
            Assert.assertEquals(-1, fail.getX(0.0, 0), UtilTests.PRECISION);
        }
        catch (final LionEngineException exception)
        {
            // Success
        }
        try
        {
            Assert.assertEquals(-1, fail.getY(0.0, 0), UtilTests.PRECISION);
        }
        catch (final LionEngineException exception)
        {
            // Success
        }
    }
}

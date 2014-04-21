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
package com.b3dgs.lionengine.core;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.Resolution;

/**
 * Mock sequence.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class SequenceMock
        extends Sequence
{
    /** Width. */
    private int width;
    /** Height. */
    private int height;
    /** Config. */
    private Config config;
    /** Device. */
    private Object device;

    /**
     * Constructor.
     * 
     * @param loader The loader reference.
     */
    public SequenceMock(Loader loader)
    {
        super(loader, new Resolution(320, 240, 60));
        setExtrapolated(true);
        addKeyListener(null);
        setSystemCursorVisible(true);
    }

    @Override
    protected void load()
    {
        width = getWidth();
        height = getHeight();
        config = getConfig();
        setResolution(new Resolution(640, 480, 60));
        device = getInputDevice(null);
    }

    @Override
    protected void update(double extrp)
    {
        loadInternal();
        start(true, SequenceStartMock.class);
    }

    @Override
    protected void render(Graphic g)
    {
        Verbose.info("Sequence mock info");
        Verbose.info(String.valueOf(width));
        Verbose.info(String.valueOf(height));
        Verbose.info(String.valueOf(config));
        Verbose.info(String.valueOf(device));
    }
}

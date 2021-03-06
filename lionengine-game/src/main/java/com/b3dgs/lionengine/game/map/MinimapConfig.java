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
package com.b3dgs.lionengine.game.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Constant;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.game.tile.TileConfig;
import com.b3dgs.lionengine.game.tile.TileRef;
import com.b3dgs.lionengine.graphic.ColorRgba;
import com.b3dgs.lionengine.stream.Xml;
import com.b3dgs.lionengine.stream.XmlNode;

/**
 * Represents the minimap configuration.
 * 
 * @see com.b3dgs.lionengine.game.map.Minimap
 */
public final class MinimapConfig
{
    /** Default filename. */
    public static final String FILENAME = "minimap.xml";
    /** Minimap root node. */
    public static final String NODE_MINIMAP = Constant.XML_PREFIX + "minimap";
    /** Color node. */
    public static final String NODE_COLOR = Constant.XML_PREFIX + "color";
    /** Red name attribute. */
    public static final String ATTRIBUTE_COLOR_RED = "r";
    /** Green name attribute. */
    public static final String ATTRIBUTE_COLOR_GREEN = "g";
    /** Blue name attribute. */
    public static final String ATTRIBUTE_COLOR_BLUE = "b";

    /**
     * Create the minimap data from node.
     * 
     * @param configMinimap The minimap configuration media.
     * @return The minimap data.
     * @throws LionEngineException If unable to read data.
     */
    public static Map<TileRef, ColorRgba> imports(Media configMinimap)
    {
        Check.notNull(configMinimap);

        final Map<TileRef, ColorRgba> colors = new HashMap<TileRef, ColorRgba>();
        final XmlNode nodeMinimap = Xml.load(configMinimap);

        for (final XmlNode nodeColor : nodeMinimap.getChildren(NODE_COLOR))
        {
            final ColorRgba color = new ColorRgba(nodeColor.readInteger(ATTRIBUTE_COLOR_RED),
                                                  nodeColor.readInteger(ATTRIBUTE_COLOR_GREEN),
                                                  nodeColor.readInteger(ATTRIBUTE_COLOR_BLUE));

            for (final XmlNode nodeTileRef : nodeColor.getChildren(TileConfig.NODE_TILE))
            {
                final TileRef tileRef = TileConfig.create(nodeTileRef);
                colors.put(tileRef, color);
            }
        }

        return colors;
    }

    /**
     * Export tiles colors data to configuration media.
     * 
     * @param configMinimap The configuration media output.
     * @param tiles The tiles data.
     * @throws LionEngineException If error on writing.
     */
    public static void exports(Media configMinimap, Map<TileRef, ColorRgba> tiles)
    {
        Check.notNull(configMinimap);

        final Map<ColorRgba, Collection<TileRef>> colors = convertToColorKey(tiles);
        final XmlNode nodeMinimap = Xml.create(NODE_MINIMAP);

        for (final Map.Entry<ColorRgba, Collection<TileRef>> entry : colors.entrySet())
        {
            final ColorRgba color = entry.getKey();
            final XmlNode nodeColor = nodeMinimap.createChild(NODE_COLOR);
            nodeColor.writeInteger(ATTRIBUTE_COLOR_RED, color.getRed());
            nodeColor.writeInteger(ATTRIBUTE_COLOR_GREEN, color.getGreen());
            nodeColor.writeInteger(ATTRIBUTE_COLOR_BLUE, color.getBlue());

            for (final TileRef tileRef : entry.getValue())
            {
                final XmlNode nodeTileRef = TileConfig.export(tileRef);
                nodeColor.add(nodeTileRef);
            }
        }

        Xml.save(nodeMinimap, configMinimap);
    }

    /**
     * Convert map associating color per tile to tiles per color.
     * 
     * @param tiles The tiles data.
     * @return The map with color as key.
     */
    private static Map<ColorRgba, Collection<TileRef>> convertToColorKey(Map<TileRef, ColorRgba> tiles)
    {
        final Map<ColorRgba, Collection<TileRef>> colors = new HashMap<ColorRgba, Collection<TileRef>>();
        for (final Map.Entry<TileRef, ColorRgba> entry : tiles.entrySet())
        {
            final Collection<TileRef> tilesRef = getTiles(colors, entry.getValue());
            tilesRef.add(entry.getKey());
        }
        return colors;
    }

    /**
     * Get the tiles corresponding to the color. Create empty list if new color.
     * 
     * @param colors The colors data.
     * @param color The color to check.
     * @return The associated tiles.
     */
    private static Collection<TileRef> getTiles(Map<ColorRgba, Collection<TileRef>> colors, ColorRgba color)
    {
        if (!colors.containsKey(color))
        {
            colors.put(color, new HashSet<TileRef>());
        }
        return colors.get(color);
    }

    /**
     * Disabled constructor.
     */
    private MinimapConfig()
    {
        throw new LionEngineException(LionEngineException.ERROR_PRIVATE_CONSTRUCTOR);
    }
}

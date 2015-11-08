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
package com.b3dgs.lionengine.game.map;

import java.io.IOException;
import java.util.Collection;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Localizable;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Renderable;
import com.b3dgs.lionengine.Surface;
import com.b3dgs.lionengine.drawable.SpriteTiled;
import com.b3dgs.lionengine.game.Featurable;
import com.b3dgs.lionengine.game.tile.Tile;
import com.b3dgs.lionengine.game.tile.TileExtractor;
import com.b3dgs.lionengine.game.tile.TileGroup;
import com.b3dgs.lionengine.stream.FileReading;
import com.b3dgs.lionengine.stream.FileWriting;

/**
 * Describe a map using tile for its representation. This is the lower level interface to describe a 2D map using tiles.
 * Each tiles are stored vertically and then horizontally. A sheet id represents a tilesheet number (surface number
 * containing tiles). A map can have one or more sheets. The map picks its resources from a sheets folder, which
 * must contains the files images. Example of a sheet configuration file:
 * 
 * <pre>
 * &lt;lionengine:sheets xmlns:lionengine="http://lionengine.b3dgs.com"&gt;
 *    &lt;lionengine:tileSize width="16" height="16"/&gt;
 *    &lt;lionengine:sheet&gt;ground.png&lt;/lionengine:sheet&gt;
 *    &lt;lionengine:sheet&gt;wall.png&lt;/lionengine:sheet&gt;
 *    &lt;lionengine:sheet&gt;water.png&lt;/lionengine:sheet&gt;
 * &lt;/lionengine:sheets&gt;
 * </pre>
 * 
 * <p>
 * Note: ground.png, wall.png and water.png are in the same directory of this configuration file.
 * </p>
 * 
 * @see MapTileGame
 * @see Minimap
 * @see MapTileFeature
 * @see Tile
 */
public interface MapTile extends Surface, MapTileRenderer, Renderable, Featurable<MapTileFeature>
{
    /** Number of horizontal tiles to make a bloc. Used to reduce saved map file size. */
    int BLOC_SIZE = 256;

    /**
     * Create and prepare map memory area. Must be called before assigning tiles.
     * Previous map data (if existing) will be cleared.
     * 
     * @param widthInTile The map width in tile (must be strictly positive).
     * @param heightInTile The map height in tile (must be strictly positive).
     * @throws LionEngineException If size if invalid.
     * @see #create(Media, Media, Media)
     */
    void create(int widthInTile, int heightInTile);

    /**
     * Create a map from a level rip and the associated tiles directory.
     * A level rip is an image file (*.PNG, *.BMP) that represents the full map in one time.
     * The file will be read pixel by pixel to recognize tiles and their location. Data structure will be created.
     * Previous map data (if existing) will be cleared.
     * <p>
     * {@link com.b3dgs.lionengine.game.tile.ConfigTileSheets#FILENAME} and
     * {@link com.b3dgs.lionengine.game.tile.ConfigTileGroups#FILENAME} will be used as default, by calling
     * {@link #create(Media, Media, Media)}.
     * </p>
     * 
     * @param levelrip The file describing the levelrip as a single image.
     * @throws LionEngineException If error when importing map.
     * @see #create(int, int)
     * @see LevelRipConverter
     * @see TileExtractor
     */
    void create(Media levelrip);

    /**
     * Create a map from a level rip and the associated tiles directory.
     * A level rip is an image file (*.PNG, *.BMP) that represents the full map in one time.
     * The file will be read pixel by pixel to recognize tiles and their location. Data structure will be created.
     * Previous map data (if existing) will be cleared.
     * 
     * @param levelrip The file describing the levelrip as a single image.
     * @param sheetsConfig The file that define the sheets configuration.
     * @param groupsConfig The tile collision groups descriptor.
     * @throws LionEngineException If error when importing map.
     * @see #create(int, int)
     * @see LevelRipConverter
     * @see TileExtractor
     */
    void create(Media levelrip, Media sheetsConfig, Media groupsConfig);

    /**
     * Create a feature from its type, and automatically {@link #addFeature} it.
     * The feature instance must provide a public constructor with {@link com.b3dgs.lionengine.game.object.Services} as
     * single argument, or the public default constructor. Else, create manually the instance and use
     * {@link #addFeature} on it.
     * 
     * @param <F> The feature type.
     * @param feature The feature class.
     * @return The feature instance already added.
     * @throws LionEngineException If unable to create feature or <code>null</code>.
     */
    <F extends MapTileFeature> F createFeature(Class<F> feature);

    /**
     * Create a tile.
     * 
     * @return The created tile.
     */
    Tile createTile();

    /**
     * Load map sheets (tiles surfaces) from directory. Must be called before rendering map.
     * Clear previous sheets if has.
     * 
     * @param sheetsConfig The file that define the sheets configuration.
     * @throws LionEngineException If error when reading sheets.
     */
    void loadSheets(Media sheetsConfig);

    /**
     * Load tiles group from an external file.
     * 
     * @param groupsConfig The tile collision groups descriptor.
     * @throws LionEngineException If error when reading groups.
     */
    void loadGroups(Media groupsConfig);

    /**
     * Load a map from a specified file as binary data.
     * <p>
     * Data are loaded this way (see {@link #save(FileWriting)} order):
     * </p>
     * 
     * <pre>
     * <code>(String)</code> sheets file configuration
     * <code>(short)</code> width in tiles
     * <code>(short)</code> height in tiles
     * <code>(byte)</code> tile width
     * <code>(byte)</code> tile height
     * <code>(short)</code> number of {@value #BLOC_SIZE} horizontal blocs (widthInTile / {@value #BLOC_SIZE})
     * for each blocs tile
     *   <code>(short)</code> number of tiles in this bloc
     *   for each tile in this bloc
     *     create blank tile
     *     call load(file)
     *     call setTile(...) to update map with this new tile
     * </pre>
     * 
     * @param file The input level file.
     * @throws IOException If error on reading.
     */
    void load(FileReading file) throws IOException;

    /**
     * Save map to specified file as binary data. Data are saved this way (using specific types to save space):
     * 
     * <pre>
     * <code>(String)</code> sheets configuration file
     * <code>(short)</code> width in tiles
     * <code>(short)</code> height in tiles
     * <code>(byte)</code> tile width (use of byte because tile width &lt; 255)
     * <code>(byte)</code> tile height (use of byte because tile height &lt; 255)
     * <code>(short)</code> number of {@value #BLOC_SIZE} horizontal blocs (widthInTile / {@value #BLOC_SIZE})
     * for each blocs tile
     *   <code>(short)</code> number of tiles in this bloc
     *   for each tile in this bloc
     *     call tile.save(file)
     * </pre>
     * 
     * @param file The output level file.
     * @throws IOException If error on writing.
     */
    void save(FileWriting file) throws IOException;

    /**
     * Append an existing map, starting at the specified offsets. Offsets start at the beginning of the map (0, 0).
     * A call to {@link #append(MapTile, int, int)} at ({@link #getInTileWidth()}, {@link #getInTileHeight()}) will add
     * the new map at the top-right.
     * 
     * @param map The map to append.
     * @param offsetX The horizontal offset in tile (positive).
     * @param offsetY The vertical offset in tile (positive).
     */
    void append(MapTile map, int offsetX, int offsetY);

    /**
     * Remove all tiles from map and clear internal data. Keep existing loaded tile sheets ({@link #loadSheets(Media)}).
     */
    void clear();

    /**
     * Set an external map tile renderer, defining how tiles are rendered.
     * 
     * @param renderer The renderer reference.
     * @throws LionEngineException If renderer is <code>null</code>.
     */
    void setTileRenderer(MapTileRenderer renderer);

    /**
     * Set a tile at specified map location.
     * 
     * @param tx The horizontal tile index location [0 - {@link #getInTileWidth()} excluded].
     * @param ty The vertical tile index location [0 - {@link #getInTileHeight()} excluded].
     * @param tile The tile reference.
     * @throws LionEngineException If outside map range.
     */
    void setTile(int tx, int ty, Tile tile);

    /**
     * Get tile from specified map location (in tile index). If the returned tile is equal to <code>null</code>, this
     * means that there is not tile at this location. It is not an error, just a way to avoid useless tile storage.
     * 
     * @param tx The horizontal tile index location.
     * @param ty The vertical tile index location.
     * @return The tile found at this location, <code>null</code> if none.
     */
    Tile getTile(int tx, int ty);

    /**
     * Get the tile at the localizable.
     * 
     * @param localizable The localizable reference.
     * @param offsetX The horizontal offset search.
     * @param offsetY The vertical offset search.
     * @return The tile found at the localizable, <code>null</code> if none.
     */
    Tile getTile(Localizable localizable, int offsetX, int offsetY);

    /**
     * Get the tile at the location.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     * @return The tile found at the location, <code>null</code> if none.
     */
    Tile getTileAt(double x, double y);

    /**
     * Get the list of tiles from old location to current.
     * 
     * @param ox The old horizontal location.
     * @param oy The old vertical location.
     * @param x The current horizontal location.
     * @param y The current vertical location.
     * @return The tiles found.
     */
    Collection<Tile> getTilesHit(double ox, double oy, double x, double y);

    /**
     * Get location x relative to map referential as tile.
     * 
     * @param localizable The localizable reference.
     * @return The location x relative to map referential as tile.
     */
    int getInTileX(Localizable localizable);

    /**
     * Get location y relative to map referential as tile.
     * 
     * @param localizable The localizable reference.
     * @return The location y relative to map referential as tile.
     */
    int getInTileY(Localizable localizable);

    /**
     * Get the sheets configuration media file.
     * 
     * @return The sheet configuration media file.
     */
    Media getSheetsConfig();

    /**
     * Get the groups configuration media file.
     * 
     * @return The groups configuration media file.
     */
    Media getGroupsConfig();

    /**
     * Get list of sheets id.
     * 
     * @return The set of sheets id.
     */
    Collection<Integer> getSheets();

    /**
     * Get sheet from its id.
     * 
     * @param sheet The sheet id.
     * @return The sheet found.
     * @throws LionEngineException If sheet not found.
     */
    SpriteTiled getSheet(Integer sheet);

    /**
     * Get the group from its name.
     * 
     * @param name The group name.
     * @return The supported group reference.
     * @throws LionEngineException If group not found.
     */
    TileGroup getGroup(String name);

    /**
     * Get the group from its tile reference.
     * 
     * @param sheet The sheet number.
     * @param number The tile number.
     * @return The supported group reference.
     * @throws LionEngineException If group not found.
     */
    TileGroup getGroup(Integer sheet, int number);

    /**
     * Get the groups list.
     * 
     * @return The groups list.
     */
    Collection<TileGroup> getGroups();

    /**
     * Get the number of used sheets.
     * 
     * @return The number of used sheets.
     */
    int getSheetsNumber();

    /**
     * Get number of active tiles (which are not <code>null</code>).
     * 
     * @return The number of non <code>null</code> tile.
     */
    int getTilesNumber();

    /**
     * Get width of a tile.
     * 
     * @return The tile width.
     */
    int getTileWidth();

    /**
     * Get height of a tile.
     * 
     * @return The tile height.
     */
    int getTileHeight();

    /**
     * Get number of horizontal tiles.
     * 
     * @return The number of horizontal tiles.
     */
    int getInTileWidth();

    /**
     * Get number of vertical tiles.
     * 
     * @return The number of vertical tiles.
     */
    int getInTileHeight();

    /**
     * Get the radius in tile.
     * 
     * @return The radius in tile.
     */
    int getInTileRadius();

    /**
     * Check if map has been created.
     * 
     * @return <code>true</code> if created, <code>false</code> else.
     */
    boolean isCreated();
}

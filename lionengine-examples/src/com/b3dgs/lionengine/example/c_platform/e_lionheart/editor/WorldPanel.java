package com.b3dgs.lionengine.example.c_platform.e_lionheart.editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import com.b3dgs.lionengine.Graphic;
import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.Verbose;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.Context;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.Editor;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.Level;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.WorldData;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.Entity;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.EntityMover;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.FactoryEntity;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.TypeEntity;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.entity.TypeEntityMovement;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.map.Map;
import com.b3dgs.lionengine.file.File;
import com.b3dgs.lionengine.file.FileReading;
import com.b3dgs.lionengine.file.FileWriting;
import com.b3dgs.lionengine.game.CoordTile;
import com.b3dgs.lionengine.game.platform.CameraPlatform;
import com.b3dgs.lionengine.input.Mouse;

/**
 * Represents the world scene, containing the map and the entities.
 */
public class WorldPanel
        extends JPanel
        implements MouseListener, MouseMotionListener
{
    /** Uid. */
    private static final long serialVersionUID = -3609110757656654125L;
    /** Color of the selection area. */
    private static final Color COLOR_MOUSE_SELECTION = new Color(128, 128, 192, 192);
    /** Color of the box around the selected entity. */
    private static final Color COLOR_ENTITY_SELECTION = new Color(128, 240, 128, 192);
    /** Color of the entity patrol. */
    private static final Color COLOR_ENTITY_PATROL = new Color(240, 240, 128, 192);
    /** Color if the entity patrol area. */
    private static final Color COLOR_ENTITY_PATROL_AREA = new Color(128, 128, 128, 192);

    /**
     * Draw the grid.
     * 
     * @param g The graphic output.
     * @param tw Horizontal grid spacing (width).
     * @param th Vertical grid spacing (height).
     * @param areaX Horizontal global grid size.
     * @param areaY Vertical global grid size.
     * @param color Grid color.
     */
    private static void drawGrid(Graphics2D g, int tw, int th, int areaX, int areaY, Color color)
    {
        g.setColor(color);
        for (int v = 0; v <= areaY; v += tw)
        {
            g.drawLine(0, v, areaX, v);
        }
        for (int h = 0; h <= areaX; h += th)
        {
            g.drawLine(h, 0, h, areaY);
        }
    }

    /**
     * Get the rounded value.
     * 
     * @param value The value.
     * @param round The round factor.
     * @return The rounded value.
     */
    private static int getRounded(int value, int round)
    {
        return value / round * round;
    }

    /** Level reference. */
    public final Level level;
    /** The map reference. */
    public final Map map;
    /** The camera reference. */
    public final CameraPlatform camera;
    /** Context. */
    public final Context context;
    /** World data. */
    public final WorldData worldData;
    /** The entity handler reference. */
    public final Handler handlerEntity;
    /** The factory reference. */
    public final FactoryEntity factoryEntity;
    /** The editor reference. */
    private final Editor editor;
    /** Current horizontal mouse location. */
    private int mouseX;
    /** Current vertical mouse location. */
    private int mouseY;
    /** Selecting flag. */
    private boolean selecting;
    /** Selected flag. */
    private boolean selected;
    /** Clicking flag. */
    private boolean clicking;
    /** Selection starting horizontal location. */
    private int selectStartX;
    /** Selection starting vertical location. */
    private int selectStartY;
    /** Selection ending horizontal location. */
    private int selectEndX;
    /** Selection ending vertical location. */
    private int selectEndY;
    /** Current player selection state. */
    private TypeSelectionPlayer playerSelection;

    /**
     * Constructor.
     * 
     * @param editor The editor reference.
     */
    public WorldPanel(final Editor editor)
    {
        super();
        this.editor = editor;
        camera = new CameraPlatform(640, 480);
        factoryEntity = new FactoryEntity();
        handlerEntity = new Handler(factoryEntity);
        level = new Level(factoryEntity, handlerEntity);
        worldData = level.worldData;
        map = level.map;
        context = new Context(level, 60);
        setPreferredSize(new Dimension(640, 480));
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    /**
     * Save a level to a file.
     * 
     * @param media The file to save level to.
     */
    public void save(Media media)
    {
        try (final FileWriting file = File.createFileWriting(media);)
        {
            level.save(file);
        }
        catch (final IOException
                     | NullPointerException exception)
        {
            Verbose.exception(MenuBar.class, "saveLevel", exception, "An error occured while saving map:",
                    media.getPath());
        }
    }

    /**
     * Load a level from a file.
     * 
     * @param media The level file.
     */
    public void load(Media media)
    {
        try (final FileReading file = File.createFileReading(media);)
        {
            level.load(file);
        }
        catch (final IOException exception)
        {
            Verbose.exception(Editor.class, "loadLevel", exception, "An error occured while loading map:",
                    media.getPath());
        }
    }

    /**
     * Set the current player selection type.
     * 
     * @param selection The current selection.
     */
    public void setPlayerSelection(TypeSelectionPlayer selection)
    {
        playerSelection = selection;
    }

    /**
     * Check if mouse is clicking.
     * 
     * @return <code>true</code> if clicking, <code>false</code> else.
     */
    public boolean isClicking()
    {
        return clicking;
    }

    /**
     * Draw all entities.
     * 
     * @param g The graphic output.
     * @param hOff The horizontal offset.
     * @param vOff The vertical offset.
     * @param height The rendering height (render from bottom).
     */
    private void drawEntities(Graphics2D g, int hOff, int vOff, int height)
    {
        final int th = map.getTileHeight();
        for (final Entity entity : handlerEntity.list())
        {
            final int sx = entity.getLocationIntX();
            final int sy = entity.getLocationIntY();

            // Patrol
            if (entity instanceof EntityMover)
            {
                final EntityMover mover = (EntityMover) entity;
                drawEntityMovement(g, mover, hOff, vOff, height);
            }

            if (entity.isSelected() || entity.isOver())
            {
                g.setColor(WorldPanel.COLOR_ENTITY_SELECTION);
                g.fillRect(sx - hOff, -sy + vOff - entity.getHeight() + WorldPanel.getRounded(height, th),
                        entity.getWidth(), entity.getHeight());
            }
            entity.render(new Graphic(g), camera);
        }
    }

    /**
     * Draw entity movement
     * 
     * @param g The graphic output.
     * @param mover The entity reference.
     * @param hOff The horizontal offset.
     * @param vOff The vertical offset.
     * @param height The rendering height (render from bottom).
     */
    private void drawEntityMovement(Graphics2D g, EntityMover mover, int hOff, int vOff, int height)
    {
        if (mover.getMovementType() != TypeEntityMovement.NONE)
        {
            final int sx = mover.getLocationIntX();
            final int sy = mover.getLocationIntY();
            final int th = map.getTileHeight();
            final int left = Map.TILE_WIDTH * mover.getPatrolLeft();
            final int right = Map.TILE_WIDTH * (mover.getPatrolLeft() + mover.getPatrolRight());
            g.setColor(WorldPanel.COLOR_ENTITY_PATROL_AREA);
            g.fillRect(sx - hOff - left, -sy + vOff + WorldPanel.getRounded(height, th) - mover.getHeight(),
                    mover.getWidth() + right, mover.getHeight());
            g.setColor(WorldPanel.COLOR_ENTITY_PATROL);
            if (mover.getMovementType() == TypeEntityMovement.HORIZONTAL)
            {
                g.fillRect(sx - hOff - left + mover.getWidth() / 2, -sy + vOff + WorldPanel.getRounded(height, th),
                        right, Map.TILE_HEIGHT);
            }
        }
    }

    /**
     * Draw the cursor.
     * 
     * @param g The graphic output.
     * @param tw The tile width.
     * @param th The tile height.
     * @param areaX Maximum width.
     * @param areaY Maximum height.
     */
    private void drawCursor(Graphics2D g, int tw, int th, int areaX, int areaY)
    {
        if (!selecting)
        {
            if (mouseX >= 0 && mouseY >= 0 && mouseX < areaX && mouseY < areaY)
            {
                final int mx = WorldPanel.getRounded(mouseX, tw);
                final int my = WorldPanel.getRounded(mouseY, th);

                g.setColor(WorldPanel.COLOR_MOUSE_SELECTION);
                g.fill3DRect(mx, my, tw, th, true);
            }
        }
    }

    /**
     * Draw the current selection.
     * 
     * @param g The graphic output.
     */
    private void drawSelection(Graphics2D g)
    {
        if (selecting)
        {
            final int sx = selectStartX;
            final int sy = selectStartY;
            final int w = selectEndX - sx;
            final int h = selectEndY - sy;
            g.setColor(WorldPanel.COLOR_MOUSE_SELECTION);
            g.fillRect(sx, sy, w, h);
        }
    }

    /**
     * Start the selection.
     * 
     * @param mx The mouse x.
     * @param my The mouse y.
     */
    private void beginSelection(int mx, int my)
    {
        if (!selecting)
        {
            final int sx = WorldPanel.getRounded(mx, map.getTileWidth());
            final int sy = WorldPanel.getRounded(my, map.getTileHeight());
            selectStartX = sx;
            selectStartY = sy;
            selectEndX = sx;
            selectEndY = sy;
            selecting = true;
            selected = false;
        }
    }

    /**
     * Update the active selection.
     * 
     * @param mx The mouse x.
     * @param my The mouse y.
     */
    private void updateSelection(int mx, int my)
    {
        if (selecting)
        {
            selectEndX = WorldPanel.getRounded(mx, map.getTileWidth());
            selectEndY = WorldPanel.getRounded(my, map.getTileHeight());
            selecting = true;
            selected = true;
        }
    }

    /**
     * Terminate current selection.
     * 
     * @param mx The mouse x.
     * @param my The mouse y.
     */
    private void endSelection(int mx, int my)
    {
        if (selecting)
        {
            int sx = selectStartX;
            int sy = selectStartY;
            int ex = WorldPanel.getRounded(mx, map.getTileWidth());
            int ey = WorldPanel.getRounded(my, map.getTileHeight());

            // Ensure selection is positive
            if (ex < sx)
            {
                final int tmp = sx;
                sx = ex;
                ex = tmp;
            }
            if (ey > sy)
            {
                final int tmp = sy;
                sy = ey;
                ey = tmp;
            }
            selectStartX = sx;
            selectStartY = sy;
            selectEndX = ex;
            selectEndY = ey;
            selecting = false;
        }
    }

    /**
     * Update the mouse.
     * 
     * @param mx The mouse horizontal location.
     * @param my The mouse vertical location.
     */
    private void updateMouse(int mx, int my)
    {
        mouseX = mx;
        mouseY = my;
        repaint();
    }

    /**
     * Reset the selection.
     */
    private void resetSelection()
    {
        selectStartX = -1;
        selectStartY = -1;
        selectEndX = -1;
        selectEndY = -1;
        selecting = false;
    }

    /**
     * Check if entity is hit.
     * 
     * @param x The horizontal location.
     * @param y The vertical location.
     * @return <code>true</code> if hit, <code>false</code> else.
     */
    private Entity hitEntity(int x, int y)
    {
        final int mx = WorldPanel.getRounded(x, map.getTileWidth());
        final int my = WorldPanel.getRounded(getHeight() - y, map.getTileHeight());
        for (final Entity entity : handlerEntity.list())
        {
            if (hitEntities(entity, mx, my, mx + map.getTileWidth(), my + map.getTileHeight()))
            {
                return entity;
            }
        }
        return null;
    }

    /**
     * Check if entity is hit.
     * 
     * @param entity The entity to check.
     * @param x1 First point x.
     * @param y1 First point y.
     * @param x2 Second point x.
     * @param y2 Second point y.
     * @return <code>true</code> if hit, <code>false</code> else.
     */
    private boolean hitEntities(Entity entity, int x1, int y1, int x2, int y2)
    {
        if (entity != null)
        {
            final int sx = WorldPanel.getRounded(entity.getLocationIntX(), map.getTileWidth())
                    - editor.getOffsetViewH();
            final int sy = WorldPanel.getRounded(entity.getLocationIntY(), map.getTileHeight())
                    - editor.getOffserViewV();
            final Rectangle2D r1 = new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1);
            final Rectangle2D r2 = new Rectangle2D.Float(sx, sy, entity.getWidth(), entity.getHeight());
            if (r1.intersects(r2))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Select all entities.
     */
    private void selectEntities()
    {
        for (final Entity entity : handlerEntity.list())
        {
            entity.setSelection(false);
            final int offy = getHeight() - WorldPanel.getRounded(getHeight(), map.getTileHeight());
            final int sx = WorldPanel.getRounded(selectStartX, map.getTileWidth());
            final int sy = WorldPanel.getRounded(getHeight() - selectStartY - offy, map.getTileHeight());
            final int ex = WorldPanel.getRounded(selectEndX, map.getTileWidth());
            final int ey = WorldPanel.getRounded(getHeight() - selectEndY - offy, map.getTileHeight());
            if (hitEntities(entity, sx, sy, ex, ey))
            {
                entity.setSelection(true);
            }
        }
    }

    /**
     * Unselect entities.
     */
    private void unSelectEntities()
    {
        for (final Entity entity : handlerEntity.list())
        {
            entity.setSelection(false);
        }
    }

    /**
     * Get the list of selected entities.
     * 
     * @param first Get only the first element.
     * @return The selected entities.
     */
    private List<Entity> getSelectedEnties(boolean first)
    {
        final List<Entity> list = new ArrayList<>(0);
        for (final Entity entity : handlerEntity.list())
        {
            if (entity.isSelected())
            {
                list.add(entity);
                if (first)
                {
                    return list;
                }
            }
        }
        return list;
    }

    /*
     * JPanel
     */

    @Override
    public void paintComponent(Graphics gd)
    {
        final Graphics2D g = (Graphics2D) gd;
        final int width = getWidth();
        final int height = getHeight();
        final int tw = map.getTileWidth();
        final int th = map.getTileHeight();
        final int hOff = editor.getOffsetViewH();
        final int vOff = editor.getOffserViewV();
        final int areaX = WorldPanel.getRounded(width, tw);
        final int areaY = WorldPanel.getRounded(height, th);
        camera.setView(0, 0, areaX, areaY);

        // Background
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, width, height);

        // Map area
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, areaX, areaY);

        // Renders
        if (!map.getPatterns().isEmpty())
        {
            map.render(new Graphic(g), camera);
        }
        drawEntities(g, hOff, vOff, height);
        g.setColor(Color.GREEN);
        g.fillRect(worldData.getStartX() - hOff, -worldData.getStartY() + vOff + WorldPanel.getRounded(height, th)
                - Map.TILE_HEIGHT, Map.TILE_WIDTH, Map.TILE_HEIGHT);
        g.setColor(Color.RED);
        g.fillRect(worldData.getEndX() - hOff, -worldData.getEndY() + vOff + WorldPanel.getRounded(height, th)
                - Map.TILE_HEIGHT, Map.TILE_WIDTH, Map.TILE_HEIGHT);

        for (final CoordTile p : worldData.getCheckpoints())
        {
            g.setColor(Color.YELLOW);
            g.fillRect(p.getX() - hOff, -p.getY() + vOff + WorldPanel.getRounded(height, th) - Map.TILE_HEIGHT,
                    Map.TILE_WIDTH, Map.TILE_HEIGHT);
        }

        drawCursor(g, tw, th, areaX, areaY);
        WorldPanel.drawGrid(g, tw, th, areaX, areaY, Color.GRAY);
        drawSelection(g);
    }

    /*
     * MouseListener
     */

    @Override
    public void mouseEntered(MouseEvent e)
    {
        // Nothing to do
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        // Nothing to do
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        // Nothing to do
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        final int mx = e.getX();
        final int my = e.getY();
        final int tw = map.getTileWidth();
        final int th = map.getTileHeight();
        final int h = WorldPanel.getRounded(getHeight(), th) - map.getTileHeight();
        final int x = editor.getOffsetViewH() + WorldPanel.getRounded(mx, tw);
        final int y = editor.getOffserViewV() - WorldPanel.getRounded(my, th) + h;
        clicking = true;

        switch (editor.getSelectionState())
        {
            case SELECT:
                if (e.getButton() == Mouse.LEFT)
                {
                    final Entity entity = hitEntity(mx, my);
                    editor.toolBar.entityEditor.setSelectedEntity(entity);
                    if (entity != null)
                    {
                        selected = false;
                        if (!entity.isSelected())
                        {
                            unSelectEntities();
                            entity.setSelection(true);
                        }
                    }
                    else
                    {
                        unSelectEntities();
                        beginSelection(mx, my);
                    }
                }
                break;
            case PLACE:
                if (hitEntity(mx, my) == null)
                {
                    unSelectEntities();
                    final int id = editor.getSelectedEntity().getIndex();
                    final Entity entity = factoryEntity.createEntity(TypeEntity.get(id));
                    entity.teleport(WorldPanel.getRounded(x, tw), WorldPanel.getRounded(y, th));
                    handlerEntity.add(entity);
                    handlerEntity.update();
                }
                break;
            case DELETE:
                final Entity entity = hitEntity(mx, my);
                if (entity != null)
                {
                    handlerEntity.remove(entity);
                    handlerEntity.update();
                }
                break;
            case PLAYER:
                switch (playerSelection)
                {
                    case PLACE_START:
                        worldData.setStarting(x, y);
                        break;
                    case PLACE_END:
                        worldData.setEnding(x, y);
                        break;
                    case ADD_CHECKPOINT:
                        worldData.addCheckpoint(x, y);
                        break;
                    case REMOVE_CHECKPOINT:
                        worldData.removeCheckpoint(worldData.getCheckpointAt(x, y));
                        break;
                    default:
                        throw new LionEngineException("Unknown selection: " + playerSelection);
                }
                break;
            default:
                throw new LionEngineException("Unknown selection: " + editor.getSelectionState());
        }
        updateMouse(mx, my);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        final int mx = e.getX();
        final int my = e.getY();
        clicking = false;

        endSelection(mx, my);
        if (selected)
        {
            selectEntities();
        }
        for (final Entity ent : getSelectedEnties(false))
        {
            ent.teleport(WorldPanel.getRounded(ent.getLocationIntX(), map.getTileWidth()),
                    WorldPanel.getRounded(ent.getLocationIntY(), map.getTileHeight()));
        }
        resetSelection();
        updateMouse(mx, my);
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        final int mx = e.getX();
        final int my = e.getY();
        updateSelection(mx, my);

        for (final Entity entity : handlerEntity.list())
        {
            if (entity.isSelected())
            {
                entity.moveLocation(1.0, mx - mouseX, mouseY - my);
            }
        }

        updateMouse(mx, my);
    }

    @Override
    public void mouseMoved(MouseEvent e)
    {
        final int mx = e.getX();
        final int my = e.getY();
        final int offy = getHeight() - WorldPanel.getRounded(getHeight(), map.getTileHeight());
        final int x = WorldPanel.getRounded(mx, map.getTileWidth());
        final int y = WorldPanel.getRounded(getHeight() - my - offy, map.getTileHeight());

        for (final Entity entity : handlerEntity.list())
        {
            entity.setOver(false);
            if (hitEntities(entity, x, y, x + map.getTileWidth(), y + map.getTileHeight()))
            {
                entity.setOver(true);
            }
        }

        updateMouse(mx, my);
    }
}
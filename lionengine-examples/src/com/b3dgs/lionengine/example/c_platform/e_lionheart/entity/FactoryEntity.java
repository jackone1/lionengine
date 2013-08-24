package com.b3dgs.lionengine.example.c_platform.e_lionheart.entity;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.Media;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.AppLionheart;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.TypeEntity;
import com.b3dgs.lionengine.example.c_platform.e_lionheart.map.Map;
import com.b3dgs.lionengine.game.SetupEntityGame;
import com.b3dgs.lionengine.game.entity.FactoryEntityGame;
import com.b3dgs.lionengine.game.platform.CameraPlatform;

/**
 * Handle the entity creation by containing all necessary object for their instantiation.
 */
public final class FactoryEntity
        extends FactoryEntityGame<TypeEntity, SetupEntityGame, Entity>
{
    /** Entity configuration file extension. */
    private static final String CONFIG_FILE_EXTENSION = ".xml";
    /** Unknown entity error message. */
    private static final String UNKNOWN_ENTITY_ERROR = "Unknown entity: ";
    /** Map reference. */
    private final Map map;
    /** Camera reference. */
    private final CameraPlatform camera;
    /** Desired fps. */
    private final int desiredFps;

    /**
     * Standard constructor.
     * 
     * @param camera The camera reference.
     * @param map The map reference.
     * @param desiredFps The desired fps.
     */
    public FactoryEntity(CameraPlatform camera, Map map, int desiredFps)
    {
        super(TypeEntity.class);
        this.camera = camera;
        this.map = map;
        this.desiredFps = desiredFps;
        loadAll(TypeEntity.values());
    }

    /**
     * Create a new valdyn.
     * 
     * @return The instance of valdyn.
     */
    public Valdyn createValdyn()
    {
        return new Valdyn(getSetup(TypeEntity.valdyn), camera, map, desiredFps);
    }

    /*
     * FactoryEntityGame
     */

    @Override
    public Entity createEntity(TypeEntity type)
    {
        switch (type)
        {
            case valdyn:
                return createValdyn();
            default:
                throw new LionEngineException(FactoryEntity.UNKNOWN_ENTITY_ERROR + type);
        }
    }

    @Override
    protected SetupEntityGame createSetup(TypeEntity id)
    {
        return new SetupEntityGame(Media.get(AppLionheart.ENTITIES_DIR, id + FactoryEntity.CONFIG_FILE_EXTENSION));
    }
}
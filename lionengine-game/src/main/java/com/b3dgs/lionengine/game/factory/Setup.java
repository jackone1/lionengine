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
package com.b3dgs.lionengine.game.factory;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.game.configurer.Configurer;

/**
 * Define a structure used to create configurer objects.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 * @see Configurer
 */
public class Setup
{
    /** Class error. */
    private static final String ERROR_CLASS = "Class not found for: ";

    /** Configurer reference. */
    private final Configurer configurer;
    /** Config file name. */
    private final Media configFile;
    /** Class reference. */
    private Class<?> clazz;

    /**
     * Create a setup.
     * 
     * @param config The config media.
     * @throws LionEngineException If error when opening the media.
     */
    public Setup(Media config) throws LionEngineException
    {
        configurer = new Configurer(config);
        configFile = config;
    }

    /**
     * Clear the setup.
     */
    public void clear()
    {
        clazz = null;
    }

    /**
     * Get the configurer reference.
     * 
     * @return The configurer reference.
     */
    public final Configurer getConfigurer()
    {
        return configurer;
    }

    /**
     * Get the configuration file.
     * 
     * @return The configuration file.
     */
    public final Media getConfigFile()
    {
        return configFile;
    }

    /**
     * Get the class mapped to the setup. Lazy call (load class only first time, and keep its reference after).
     * 
     * @param classLoader The class loader used.
     * @return The class mapped to the setup.
     * @throws LionEngineException If the class was not found by the class loader.
     */
    public final Class<?> getConfigClass(ClassLoader classLoader) throws LionEngineException
    {
        if (clazz == null)
        {
            try
            {
                clazz = classLoader.loadClass(configurer.getClassName());
            }
            catch (final ClassNotFoundException exception)
            {
                throw new LionEngineException(exception, Setup.ERROR_CLASS, configFile.getPath());
            }
        }
        return clazz;
    }
}
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
package com.b3dgs.lionengine.file;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.b3dgs.lionengine.LionEngineException;
import com.b3dgs.lionengine.UtilityFile;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.FactoryMediaProvider;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.mock.FactoryMediaMock;

/**
 * Test the file package.
 * 
 * @author Pierre-Alexandre (contact@b3dgs.com)
 */
public class FileTest
{
    /** Resources path. */
    private static String PATH;

    /**
     * Prepare the test.
     */
    @BeforeClass
    public static void prepareTest()
    {
        FactoryMediaProvider.setFactoryMedia(new FactoryMediaMock());
        FileTest.PATH = UtilityFile.getPath("src", "test", "resources", "file");
    }

    /**
     * Clean up test.
     */
    @AfterClass
    public static void cleanUp()
    {
        FactoryMediaProvider.setFactoryMedia(null);
    }

    /**
     * Test the failure cases.
     * 
     * @throws IOException If error.
     */
    private static void testFailures() throws IOException
    {
        try
        {
            File.createFileWriting(null);
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
        }

        try
        {
            File.createFileReading(null);
            Assert.fail();
        }
        catch (LionEngineException
               | IOException exception)
        {
            // Success
        }

        try
        {
            File.createXmlNode(null);
            Assert.fail();
        }
        catch (final LionEngineException exception)
        {
            // Success
        }

        final XmlParser parser = File.createXmlParser();
        Assert.assertNotNull(parser);
    }

    /**
     * Test the file factory.
     * 
     * @throws NoSuchMethodException If error.
     * @throws SecurityException If error.
     * @throws InstantiationException If error.
     * @throws IllegalAccessException If error.
     * @throws IllegalArgumentException If error.
     * @throws IOException If error.
     */
    @Test
    public void testFactory() throws NoSuchMethodException, SecurityException, InstantiationException,
            IllegalAccessException, IllegalArgumentException, IOException
    {
        final Constructor<File> constructor = File.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        try
        {
            final File file = constructor.newInstance();
            Assert.assertNotNull(file);
            Assert.fail();
        }
        catch (final InvocationTargetException exception)
        {
            // Success
        }

        FileTest.testFailures();
        Assert.assertNotNull(File.createXmlNode("test"));
        try (FileReading reading = File.createFileReading(Core.MEDIA.create(FileTest.PATH, "malformed.xml"));)
        {
            Assert.assertNotNull(reading);
        }
        final Media media = Core.MEDIA.create(FileTest.PATH, "test");
        try (FileWriting writing = File.createFileWriting(media);)
        {
            Assert.assertNotNull(writing);
        }
        Assert.assertTrue(media.getFile().delete());
        UtilityFile.deleteFile(media.getFile());
    }
}
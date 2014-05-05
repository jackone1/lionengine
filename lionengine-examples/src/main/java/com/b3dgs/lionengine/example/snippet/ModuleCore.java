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
package com.b3dgs.lionengine.example.snippet;

import java.io.IOException;

import org.junit.Assert;

import com.b3dgs.lionengine.Align;
import com.b3dgs.lionengine.Check;
import com.b3dgs.lionengine.Checksum;
import com.b3dgs.lionengine.Config;
import com.b3dgs.lionengine.ImageInfo;
import com.b3dgs.lionengine.Resolution;
import com.b3dgs.lionengine.Strings;
import com.b3dgs.lionengine.TextStyle;
import com.b3dgs.lionengine.Timing;
import com.b3dgs.lionengine.Version;
import com.b3dgs.lionengine.anim.Anim;
import com.b3dgs.lionengine.anim.Animation;
import com.b3dgs.lionengine.anim.Animator;
import com.b3dgs.lionengine.core.Core;
import com.b3dgs.lionengine.core.Engine;
import com.b3dgs.lionengine.core.Graphic;
import com.b3dgs.lionengine.core.Loader;
import com.b3dgs.lionengine.core.Media;
import com.b3dgs.lionengine.core.Sequence;
import com.b3dgs.lionengine.core.Text;
import com.b3dgs.lionengine.core.Verbose;
import com.b3dgs.lionengine.drawable.Drawable;
import com.b3dgs.lionengine.drawable.Image;
import com.b3dgs.lionengine.drawable.Sprite;
import com.b3dgs.lionengine.drawable.SpriteAnimated;
import com.b3dgs.lionengine.drawable.SpriteTiled;
import com.b3dgs.lionengine.file.File;
import com.b3dgs.lionengine.file.FileReading;
import com.b3dgs.lionengine.file.FileWriting;
import com.b3dgs.lionengine.file.XmlNode;
import com.b3dgs.lionengine.file.XmlNodeNotFoundException;
import com.b3dgs.lionengine.file.XmlParser;

@SuppressWarnings("all")
public class ModuleCore
{
    int value = 0;
    double extrp = 0f;
    Graphic g = null;

    class MyClass
    {

    }

    /*
     * Snippet code
     */

    void check()
    {
        Check.argument(value > 0, "Value was not positive:", String.valueOf(value));
        final Object object = null;
        Check.notNull(object, "Object is null !");
    }

    void checksum()
    {
        final Checksum checksum = Checksum.createSha256();
        final int integer = 489464795;
        final String value = "keyToBeEncoded";
        final String other = "anotherKey";
        final String signature = checksum.getSha256(value);
        final String test = checksum.getSha256(integer);

        Assert.assertTrue(checksum.check(value, signature));
        Assert.assertFalse(checksum.check(other, signature));
        Assert.assertTrue(checksum.check(integer, test));
    }

    void config()
    {
        final Resolution output = new Resolution(640, 480, 60);
        new Config(output, 16, true);
    }

    void imageInfo()
    {
        final ImageInfo info = ImageInfo.get(Core.MEDIA.create("dot.png"));
        Assert.assertEquals(64, info.getWidth());
        Assert.assertEquals(32, info.getHeight());
        Assert.assertEquals("png", info.getFormat());
    }

    void loader()
    {
        Engine.start("First Code", Version.create(1, 0, 0), Verbose.CRITICAL, "resources");
        final Resolution output = new Resolution(640, 480, 60);
        final Config config = new Config(output, 16, true);
        final Loader loader = new Loader(config);
        loader.start(Scene.class);
    }

    void media()
    {
        Engine.start("First Code", Version.create(1, 0, 0), Verbose.CRITICAL, "resources");
        Core.MEDIA.create("img", "image.png");
        System.out.println(Core.MEDIA.create()); // print: resources/img/image.png
    }

    final class Scene
            extends Sequence
    {
        Scene(Loader loader)
        {
            super(loader, new Resolution(320, 240, 60));
        }

        @Override
        protected void load()
        {
        }

        @Override
        protected void update(double extrp)
        {
        }

        @Override
        protected void render(Graphic g)
        {
        }
    }

    void strings()
    {
        final String str = "test";
        final String str1 = Strings.getStringRef(str);
        final String str2 = Strings.getStringRef(str);

        Assert.assertTrue(!Strings.getStringsRefs().isEmpty());
        Assert.assertTrue(str1 == str2);
        Assert.assertEquals(str1, str2);

        for (final String string : Strings.getStringsRefs())
        {
            Assert.assertTrue(string == str);
        }

        Strings.removeStringRef(str);
        Strings.clearStringsRef();
        Assert.assertTrue(Strings.getStringsRefs().isEmpty());
    }

    void text()
    {
        // Create the text
        final Text text = Core.GRAPHIC.createText(Text.SANS_SERIF, 12, TextStyle.NORMAL);

        // Rendering type 1
        text.setText("Hello");
        text.setLocation(0, 0);
        text.setAlign(Align.CENTER);
        text.render(g);

        // Rendering type 2
        text.draw(g, 0, 0, "World");

    }

    void timing() throws InterruptedException
    {
        final Timing timer = new Timing();
        Assert.assertFalse(timer.isStarted());
        timer.start();
        Assert.assertTrue(timer.isStarted());

        Thread.sleep(100);

        Assert.assertTrue(timer.isStarted());
        Assert.assertTrue(timer.elapsed(100));
        Assert.assertTrue(timer.elapsed() >= 100);
        timer.pause();

        Thread.sleep(100);

        timer.unpause();
        Assert.assertFalse(timer.elapsed(200000000));
        timer.stop();
        Assert.assertFalse(timer.isStarted());
        Assert.assertTrue(timer.get() >= 0);
    }

    void verbose()
    {
        Verbose.info("Code reached");
        try
        {
            Thread.sleep(1000);
            Verbose.warning(MyClass.class, "function", "Warning level here");
            Verbose.critical(MyClass.class, "function", "Critical level here");
        }
        catch (final InterruptedException exception)
        {
            Thread.currentThread().interrupt();
            Verbose.exception(MyClass.class, "function", exception);
        }
    }

    void version()
    {
        Version.create(1, 0, 0);
    }

    void animation()
    {
        final Animation animation = Anim.createAnimation(4, 6, 0.125, false, true);
        System.out.println(animation.getFirst()); // 4
        System.out.println(animation.getLast()); // 6
        System.out.println(animation.getSpeed()); // 0.125
        System.out.println(animation.getReverse()); // false
        System.out.println(animation.getRepeat()); // true
    }

    void animator()
    {
        final Animator animator = Anim.createAnimator();
        final Animation animation = Anim.createAnimation(4, 6, 0.125, false, true);
        animator.play(animation);

        // ... (loop)
        animator.updateAnimation(extrp);
        // (loop) ...
    }

    void animState()
    {
        final Animator animator = Anim.createAnimator();
        final Animation animation = Anim.createAnimation(1, 2, 1.0, false, false);
        animator.getAnimState(); // returns STOPPED
        animator.play(animation);
        animator.updateAnimation(extrp);
        animator.getAnimState(); // returns PLAYING
    }

    void fileReading()
    {
        final Media file = Core.MEDIA.create("test.txt");
        try (FileReading reading = File.createFileReading(file);)
        {
            reading.readBoolean();
            reading.readByte();
            reading.readChar();
            reading.readShort();
            reading.readInteger();
            reading.readFloat();
            reading.readLong();
            reading.readDouble();
        }
        catch (final IOException exception)
        {
            Assert.fail(exception.getMessage());
        }
    }

    void fileWriting()
    {
        final Media file = Core.MEDIA.create("test.txt");
        try (FileWriting writing = File.createFileWriting(file);)
        {
            writing.writeBoolean(true);
            writing.writeByte((byte) 1);
            writing.writeChar('c');
            writing.writeShort((short) 2);
            writing.writeInteger(1);
            writing.writeFloat(5.1f);
            writing.writeLong(6L);
            writing.writeDouble(7.1);
        }
        catch (final IOException exception)
        {
            Assert.fail(exception.getMessage());
        }
    }

    void xmlNode()
    {
        final XmlNode node = File.createXmlNode("node");
        node.writeBoolean("value", true);
    }

    void xmlParser() throws XmlNodeNotFoundException
    {
        // Create a tree and its nodes
        final XmlNode node1 = File.createXmlNode("root");
        final XmlNode node2 = File.createXmlNode("foo");
        node2.writeBoolean("myBoolean", true);
        node1.add(node2);

        // Save the tree
        final Media file = Core.MEDIA.create("file.xml");
        final XmlParser parser = File.createXmlParser();
        parser.save(node1, file);

        // Load and read the tree
        final XmlNode root = parser.load(file);
        final XmlNode foo = root.getChild("foo");
        Assert.assertTrue(foo.readBoolean("myBoolean"));
    }

    void image()
    {
        // Load
        final Image image = Drawable.loadImage(Core.MEDIA.create("image.png"));

        // Render
        image.render(g, 0, 0);
    }

    void sprite()
    {
        // Load
        final Sprite sprite = Drawable.loadSprite(Core.MEDIA.create("sprite.png"));
        sprite.load(false);

        // Render
        sprite.render(g, 64, 280);
    }

    void spriteTiled()
    {
        // Load
        final SpriteTiled tilesheet = Drawable.loadSpriteTiled(Core.MEDIA.create("tilesheet.png"), 16, 16);
        tilesheet.load(false);

        // Render
        tilesheet.render(g, 1, 300, 300);
        tilesheet.render(g, 350, 300);
    }

    void spriteAnimated()
    {
        // Load
        final SpriteAnimated animation = Drawable.loadSpriteAnimated(Core.MEDIA.create("animation.png"), 7, 1);
        animation.load(false);
        final Animation anim = Anim.createAnimation(4, 6, 0.125, false, true);
        animation.play(anim);

        // Update
        animation.updateAnimation(extrp);

        // Render
        animation.setMirror(false);
        animation.render(g, 160, 300);
        animation.setMirror(true);
        animation.render(g, 200, 300);
    }
}
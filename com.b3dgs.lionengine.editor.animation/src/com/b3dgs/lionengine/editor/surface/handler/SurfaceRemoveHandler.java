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
package com.b3dgs.lionengine.editor.surface.handler;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.b3dgs.lionengine.editor.properties.PropertiesPart;
import com.b3dgs.lionengine.editor.utility.UtilPart;
import com.b3dgs.lionengine.game.feature.Configurer;
import com.b3dgs.lionengine.game.feature.FramesConfig;
import com.b3dgs.lionengine.game.feature.SurfaceConfig;
import com.b3dgs.lionengine.game.state.AnimationConfig;
import com.b3dgs.lionengine.stream.XmlNode;

/**
 * Remove surface handler.
 */
public final class SurfaceRemoveHandler
{
    /**
     * Create handler.
     */
    public SurfaceRemoveHandler()
    {
        super();
    }

    /**
     * Execute the handler.
     */
    @Execute
    public void execute()
    {
        final PropertiesPart part = UtilPart.getPart(PropertiesPart.ID, PropertiesPart.class);
        final Tree properties = part.getTree();
        final Configurer configurer = (Configurer) properties.getData();
        final XmlNode root = configurer.getRoot();
        root.removeChild(SurfaceConfig.NODE_SURFACE);
        root.removeChildren(AnimationConfig.ANIMATION);
        configurer.save();
        for (final TreeItem item : properties.getItems())
        {
            final Object data = item.getData();
            if (SurfaceConfig.ATT_IMAGE.equals(data)
                || SurfaceConfig.ATT_ICON.equals(data)
                || FramesConfig.NODE_FRAMES.equals(data)
                || AnimationConfig.ANIMATION.equals(data))
            {
                part.clear(item);
            }
        }
    }
}

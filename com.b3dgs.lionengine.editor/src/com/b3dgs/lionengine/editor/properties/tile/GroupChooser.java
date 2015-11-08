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
package com.b3dgs.lionengine.editor.properties.tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.b3dgs.lionengine.editor.dialog.AbstractDialog;
import com.b3dgs.lionengine.editor.project.dialog.group.GroupsEditDialog;
import com.b3dgs.lionengine.editor.utility.UtilButton;
import com.b3dgs.lionengine.editor.utility.UtilCombo;
import com.b3dgs.lionengine.editor.world.WorldModel;
import com.b3dgs.lionengine.editor.world.updater.WorldInteractionTile;
import com.b3dgs.lionengine.game.map.MapTile;
import com.b3dgs.lionengine.game.tile.ConfigTileGroups;
import com.b3dgs.lionengine.game.tile.Tile;
import com.b3dgs.lionengine.game.tile.TileGroup;

/**
 * Represents the tile group chooser.
 */
public class GroupChooser extends AbstractDialog
{
    /** Groups values. */
    private final String[] groups;
    /** Combo box. */
    private Combo combo;
    /** Choice value. */
    private String choice;

    /**
     * Create the group chooser.
     * 
     * @param parent The parent reference.
     * @param groups The groups values.
     */
    public GroupChooser(Shell parent, Collection<String> groups)
    {
        super(parent,
              Messages.GroupChooser_Title,
              Messages.GroupChooser_HeaderTitle,
              Messages.GroupChooser_HeaderDesc,
              GroupsEditDialog.ICON);
        this.groups = groups.toArray(new String[groups.size()]);
        createDialog();
        dialog.setMinimumSize(256, 64);
        finish.setEnabled(true);
    }

    /**
     * Get the current choice.
     * 
     * @return The choice value.
     */
    public String getChoice()
    {
        return choice;
    }

    /**
     * Load groups.
     * 
     * @param groups The groups to load.
     */
    private void loadGroups(String[] groups)
    {
        UtilCombo.registerDirty(combo, false);
        Arrays.sort(groups);
        combo.setItems(groups);

        final WorldInteractionTile interaction = WorldModel.INSTANCE.getServices().get(WorldInteractionTile.class);
        final Tile tile = interaction.getSelection();
        if (tile != null && tile.getGroup() != null)
        {
            combo.setText(tile.getGroup());
        }
        else if (groups.length > 0)
        {
            combo.setText(groups[0]);
        }
        UtilCombo.registerDirty(combo, true);
    }

    /**
     * Add group action.
     * 
     * @param shell The shell reference.
     */
    private void addGroup(Shell shell)
    {
        final MapTile map = WorldModel.INSTANCE.getMap();
        final GroupsEditDialog dialog = new GroupsEditDialog(shell, map.getGroupsConfig());
        dialog.open();

        final Collection<String> values = new ArrayList<>();
        for (final TileGroup group : map.getGroups())
        {
            values.add(group.getName());
        }
        if (!values.contains(ConfigTileGroups.REMOVE_GROUP_NAME))
        {
            values.add(ConfigTileGroups.REMOVE_GROUP_NAME);
        }
        loadGroups(values.toArray(new String[values.size()]));
    }

    /*
     * AbstractDialog
     */

    @Override
    protected void createContent(Composite content)
    {
        final Composite composite = new Composite(content, SWT.NONE);
        composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        composite.setLayout(new GridLayout(3, false));

        final Label label = new Label(composite, SWT.NONE);
        label.setText(Messages.GroupChooser_Choice);

        combo = new Combo(composite, SWT.SINGLE | SWT.READ_ONLY);
        loadGroups(groups);

        final Button add = new Button(composite, SWT.PUSH);
        add.setText(Messages.GroupChooser_Add);
        UtilButton.setAction(add, () -> addGroup(add.getShell()));
    }

    @Override
    protected void onFinish()
    {
        choice = combo.getText();
    }
}

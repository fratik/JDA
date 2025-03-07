/*
 * Copyright 2015 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.dv8tion.jda.internal.interactions;

import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenu;
import net.dv8tion.jda.api.interactions.components.selections.SelectionMenuInteraction;
import net.dv8tion.jda.api.utils.data.DataArray;
import net.dv8tion.jda.api.utils.data.DataObject;
import net.dv8tion.jda.internal.JDAImpl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SelectionMenuInteractionImpl extends ComponentInteractionImpl implements SelectionMenuInteraction
{
    private final List<String> values;
    private final SelectionMenu menu;

    public SelectionMenuInteractionImpl(JDAImpl jda, DataObject data)
    {
        super(jda, data);
        values = Collections.unmodifiableList(data.getObject("data").optArray("values")
                .map(it -> it.stream(DataArray::getString).collect(Collectors.toList()))
                .orElse(Collections.emptyList()));
        if (message != null)
        {
            menu = (SelectionMenu) message.getActionRows()
                    .stream()
                    .flatMap(row -> row.getComponents().stream())
                    .filter(c -> c.getType() == Component.Type.SELECTION_MENU && customId.equals(c.getId()))
                    .findFirst()
                    .orElse(null);
        }
        else
        {
            menu = null;
        }
    }

    @Nullable
    @Override
    public SelectionMenu getComponent()
    {
        return menu;
    }

    @Nonnull
    @Override
    public Component.Type getComponentType()
    {
        return Component.Type.SELECTION_MENU;
    }

    @Nonnull
    @Override
    public List<String> getValues()
    {
        return values;
    }
}

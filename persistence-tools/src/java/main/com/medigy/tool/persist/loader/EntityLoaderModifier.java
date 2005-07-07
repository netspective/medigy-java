package com.medigy.tool.persist.loader;

import java.util.List;

import com.medigy.persist.util.DelimitedValuesReader;

public interface EntityLoaderModifier
{
    /**
     * Given an entity that has already been initialized with the data from originalDataFromSource, take the opportunity
     * to modify the properties in the entity.
     * @param loader The loader that is doing the loading
     * @param reader The reader that is parsing the source
     * @param entity The entity that was created and has already been initialized and ready to save
     * @param originalDataFromSource The fields that were parsed for the current line from the data source
     */
    public void modifyEntity(final EntityLoader loader, final DelimitedValuesReader reader, final Object entity,
                             final List<String> originalDataFromSource);
}

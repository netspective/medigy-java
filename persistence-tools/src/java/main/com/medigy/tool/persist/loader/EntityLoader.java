package com.medigy.tool.persist.loader;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.BeanUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.medigy.persist.util.DelimitedValuesReader;
import com.medigy.persist.util.DelimitedValuesReaderException;

/**
 * Load records from a delimited (CSV, text, etc) file into the database using a hibernate entity.
 */ 
public class EntityLoader implements DelimitedValuesReader.LineHandler
{
    public final Pattern ENTITY_AND_FIELDNAME_PATTERN = Pattern.compile("(.*)\\.(.+)$");

    public interface EventHandler
    {
        public void log(final String message);
        public void onCommit(final EntityLoader loader, final DelimitedValuesReader dvReader);
        public int getCommitRowsInterval();
    }

    private final Session session;
    private final EventHandler eventHandler;
    private final int commitRowsInterval;
    private final EntityLoaderModifier modifier;

    private Transaction activeTransaction;
    private Class entity;
    private String[] propertyNames;

    /**
     * Use the given reader to load records from a delimited file into the database pointed to by the given Session.
     * The entity name and property names that need to be written will be obtained from the first line in the file.
     * @param modifier In case each row needs to be modified somehow after an entity has been intialized this modifier will be called
     * @param session The hibernate session
     */
    public EntityLoader(final EntityLoaderModifier modifier, final Session session,
                        final EventHandler eventHandler)
    {
        this.modifier = modifier;
        this.eventHandler = eventHandler;
        this.commitRowsInterval = eventHandler == null ? 0 : eventHandler.getCommitRowsInterval();
        this.session = session;

        activeTransaction = session.beginTransaction();
    }

    public Session getSession()
    {
        return session;
    }

    public Transaction getActiveTransaction()
    {
        return activeTransaction;
    }

    public Class getEntity()
    {
        return entity;
    }

    public String[] getPropertyNames()
    {
        return propertyNames;
    }

    /**
     * If the entity is to be provided programmtically instead of being read from the first line of the delimited file
     * then use this constructor.
     * @param modifier In case each row needs to be modified somehow after an entity has been intialized this modifier will be called
     * @param session The hibernate session
     * @param entity The database entity
     * @param propertyNames The property names to set on the entity when saving
     */
    public EntityLoader(final EntityLoaderModifier modifier, final Session session,
                        final EventHandler eventHandler,
                        final Class entity, final String[] propertyNames)
    {
        this(modifier, session, eventHandler);
        this.entity = entity;
        this.propertyNames = propertyNames;
    }

    /**
     * If there is a header line then this method will be called by the reader. The header in this case will be of the
     * format [Entity].[fieldName],[fieldName2],[fieldName3]. Basically the fields will indicate what columns will be
     * assigned in the Entity.
     * @param dvReader
     * @param line
     */
    public void handleHeaderLine(final DelimitedValuesReader dvReader, final List<String> line)
    {
        propertyNames = new String[line.size()];
        for(int i = 0; i < line.size(); i++)
        {
            final String fieldName = line.get(i);
            final Matcher matcher = ENTITY_AND_FIELDNAME_PATTERN.matcher(fieldName);
            if(matcher.matches())
            {
                final String entityName = matcher.group(1);
                final String entityFieldName = matcher.group(2);

                if(entity == null)
                {
                    try
                    {
                        if(eventHandler != null)
                            eventHandler.log("Using entity " + entityName + " from line " + dvReader.getReader().getLineNumber() + " field " + i);

                        entity = Class.forName(entityName);
                    }
                    catch (ClassNotFoundException e)
                    {
                        throw new DelimitedValuesReaderException(e);
                    }
                }
                else if(! entity.getName().equals(entityName))
                {
                    throw new DelimitedValuesReaderException("Two different fields may not specify different entity names: " + entityFieldName + " vs. " + entityFieldName.getClass().getName() + " in " + fieldName);
                }

                propertyNames[i] = entityFieldName;
            }
            else
                propertyNames[i] = fieldName;
        }
    }

    public void handleLine(final DelimitedValuesReader dvReader, final List<String> line)
    {
        final Object instance;
        try
        {
            instance = entity.newInstance();
        }
        catch (InstantiationException e)
        {
            throw new DelimitedValuesReaderException(e);
        }
        catch (IllegalAccessException e)
        {
            throw new DelimitedValuesReaderException(e);
        }

        for(int i = 0; i < propertyNames.length; i++)
        {
            final String fieldName = propertyNames[i];

            // skips field names with zero length or name _SKIP_
            if(fieldName == null || fieldName.equals("_SKIP_") || fieldName.trim().length() == 0)
                continue;

            final String value = line.get(i);

            try
            {
                BeanUtils.setProperty(instance, fieldName, value);
            }
            catch (IllegalAccessException e)
            {
                throw new DelimitedValuesReaderException(e);
            }
            catch (InvocationTargetException e)
            {
                throw new DelimitedValuesReaderException(e);
            }
        }

        if(modifier != null)
            modifier.modifyEntity(this, dvReader, entity, line);

        session.save(instance);

        // to keep the database buffers from overflowing, commit every X number of rows
        if(commitRowsInterval > 0)
        {
            if((dvReader.getReader().getLineNumber() % commitRowsInterval) == 0)
            {
                activeTransaction.commit();
                activeTransaction = session.beginTransaction();
                if(eventHandler != null)
                    eventHandler.onCommit(this, dvReader);
            }
        }
    }
}

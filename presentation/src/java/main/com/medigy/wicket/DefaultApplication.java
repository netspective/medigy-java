/*
 * Copyright (c) 2000-2003 Netspective Communications LLC. All rights reserved.
 *
 * Netspective Communications LLC ("Netspective") permits redistribution, modification and use of this file in source
 * and binary form ("The Software") under the Netspective Source License ("NSL" or "The License"). The following
 * conditions are provided as a summary of the NSL but the NSL remains the canonical license and must be accepted
 * before using The Software. Any use of The Software indicates agreement with the NSL.
 *
 * 1. Each copy or derived work of The Software must preserve the copyright notice and this notice unmodified.
 *
 * 2. Redistribution of The Software is allowed in object code form only (as Java .class files or a .jar file
 *    containing the .class files) and only as part of an application that uses The Software as part of its primary
 *    functionality. No distribution of the package is allowed as part of a software development kit, other library,
 *    or development tool without written consent of Netspective. Any modified form of The Software is bound by these
 *    same restrictions.
 *
 * 3. Redistributions of The Software in any form must include an unmodified copy of The License, normally in a plain
 *    ASCII text file unless otherwise agreed to, in writing, by Netspective.
 *
 * 4. The names "Netspective", "Axiom", "Commons", "Junxion", and "Sparx" are trademarks of Netspective and may not be
 *    used to endorse products derived from The Software without without written consent of Netspective. "Netspective",
 *    "Axiom", "Commons", "Junxion", and "Sparx" may not appear in the names of products derived from The Software
 *    without written consent of Netspective.
 *
 * 5. Please attribute functionality where possible. We suggest using the "powered by Netspective" button or creating
 *    a "powered by Netspective(tm)" link to http://www.netspective.com for each application using The Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT,
 * ARE HEREBY DISCLAIMED.
 *
 * NETSPECTIVE AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE OR ANY THIRD PARTY AS A
 * RESULT OF USING OR DISTRIBUTING THE SOFTWARE. IN NO EVENT WILL NETSPECTIVE OR ITS LICENSORS BE LIABLE FOR ANY LOST
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THE SOFTWARE, EVEN
 * IF HE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * @author Shahid N. Shah
 */

/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package com.medigy.wicket;

import com.medigy.wicket.menu.Menu;
import com.medigy.wicket.session.AuthenticatedSession;
import wicket.ApplicationSettings;
import wicket.ISessionFactory;
import wicket.Session;
import wicket.contrib.spring.SpringApplication;
import wicket.util.file.File;
import wicket.util.file.Folder;
import wicket.util.file.Path;
import wicket.util.time.Duration;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.BeansException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public abstract class DefaultApplication extends SpringApplication implements ApplicationContextAware
{
    private static final Log log = LogFactory.getLog(DefaultApplication.class);

    public static final String DEFAULT_DEVL_ENV_PROJECT_HOME = "c:\\Projects\\Medigy";
    private static final String DEFAULT_THEME = "default";
    private boolean devlEnvironment;
    private File devlEnvHome;

    private ApplicationContext applicationContext;

    public DefaultApplication()
    {
        super();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext= applicationContext;
    }

    public ApplicationContext getApplicationContext()
    {
        return applicationContext;
    }

    public Object getService(final Class serviceClass)
    {
        final Map beanMap =  getApplicationContext().getBeansOfType(serviceClass);
        if (beanMap.size() == 0)
        {            
            log.warn("Failed to find any beans of type: " + serviceClass);
            return null;
        }

        return beanMap.values().iterator().next();
    }

    public void afterPropertiesSet()
    {
        super.afterPropertiesSet();
        final ApplicationSettings settings = getSettings();
        settings.setStripComments(true);
        settings.setStripWicketTags(true);

        devlEnvHome = getDevelopmentEnvironmentProjectHome();
        if(devlEnvHome != null && devlEnvHome.exists())
        {
            getSettings().setResourcePollFrequency(Duration.ONE_SECOND);
            getSettings().setResourceFinder(new Path(new Folder[] {
                new Folder(devlEnvHome, "pbs/src/java/main"),
                new Folder(devlEnvHome, "presentation/src/java/main"),
            }));

            System.out.println("Initialized development environment mode in folder " + devlEnvHome);
            devlEnvironment = true;
        }
        else
        {
            devlEnvironment = false;
        }
    }

    public boolean isDevlEnvironment()
    {
        return devlEnvironment;
    }

    public File getDevlEnvHome()
    {
        return devlEnvHome;
    }

    public ISessionFactory getSessionFactory()
    {
        return new ISessionFactory()
        {
            public Session newSession()
            {
                return new AuthenticatedSession(DefaultApplication.this, DEFAULT_THEME);
            }
        };
    }

    public File getDevelopmentEnvironmentProjectHome()
    {
        final String projectDirNamePropValue = System.getProperty("medigy.project.home", DEFAULT_DEVL_ENV_PROJECT_HOME);
        return new File(projectDirNamePropValue);
    }

    public abstract Menu getMainMenu();

/*
    TODO: add secure URLs to prevent snooping
    
	protected WebRequest newWebRequest(HttpServletRequest servletRequest)
	{
		return new WebRequestWithCryptedUrl(servletRequest);
	}

	protected WebResponse newWebResponse(HttpServletResponse servletResponse) throws IOException
	{
		return new WebResponseWithCryptedUrl(servletResponse);
	}
*/
}

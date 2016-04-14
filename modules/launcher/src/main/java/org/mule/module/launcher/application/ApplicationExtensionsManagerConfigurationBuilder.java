/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.launcher.application;

import static org.mule.api.lifecycle.LifecycleUtils.initialiseIfNeeded;
import static org.mule.module.extension.internal.ExtensionProperties.EXTENSION_MANIFEST_FILE_NAME;
import org.mule.DefaultMuleContext;
import org.mule.api.MuleContext;
import org.mule.api.lifecycle.InitialisationException;
import org.mule.config.builders.AbstractConfigurationBuilder;
import org.mule.extension.api.ExtensionManager;
import org.mule.extension.api.manifest.ExtensionManifest;
import org.mule.extension.api.persistence.manifest.ExtensionManifestXmlSerializer;
import org.mule.module.extension.internal.manager.DefaultExtensionManager;
import org.mule.module.extension.internal.manager.ExtensionManagerAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;

/**
 * Implementation of {@link org.mule.api.config.ConfigurationBuilder}
 * that register a {@link ExtensionManager} if it's present in the classpath
 *
 * @since 4.0
 */
public class ApplicationExtensionsManagerConfigurationBuilder extends AbstractConfigurationBuilder
{

    private static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ApplicationExtensionsManagerConfigurationBuilder.class);

    private final List<ApplicationPlugin> applicationPlugins;

    public ApplicationExtensionsManagerConfigurationBuilder(List<ApplicationPlugin> applicationPlugins)
    {
        this.applicationPlugins = applicationPlugins;
    }

    @Override
    protected void doConfigure(MuleContext muleContext) throws Exception
    {
        final ExtensionManagerAdapter extensionManager = createExtensionManager(muleContext);

        for (ApplicationPlugin applicationPlugin : applicationPlugins)
        {
            URL manifestUrl = applicationPlugin.getArtifactClassLoader().findResource("/META-INF/" + EXTENSION_MANIFEST_FILE_NAME);
            if (manifestUrl == null)
            {
                continue;
            }

            ExtensionManifest extensionManifest = readManifest(manifestUrl);
            extensionManager.registerExtension(extensionManifest, applicationPlugin.getArtifactClassLoader().getClassLoader());

            if (LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Discovered extension  " + extensionManifest.getName());
            }
        }
    }

    private ExtensionManifest readManifest(URL manifestUrl) throws IOException
    {
        try (InputStream manifestStream = manifestUrl.openStream())
        {
            return new ExtensionManifestXmlSerializer().deserialize(IOUtils.toString(manifestStream));
        }
        catch (IOException e)
        {
            throw new IOException("Could not read extension manifest on plugin " + manifestUrl.toString(), e);
        }
    }

    private ExtensionManagerAdapter createExtensionManager(MuleContext muleContext) throws InitialisationException
    {
        ExtensionManagerAdapter extensionManager = new DefaultExtensionManager();
        ((DefaultMuleContext) muleContext).setExtensionManager(extensionManager);
        initialiseIfNeeded(extensionManager, muleContext);

        return extensionManager;
    }
}

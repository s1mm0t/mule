/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal.manager;

import org.mule.extension.api.introspection.declaration.spi.Describer;
import org.mule.extension.api.manifest.ExtensionManifest;
import org.mule.module.extension.internal.introspection.describer.AnnotationsBasedDescriber;
import org.mule.module.extension.internal.introspection.version.StaticVersionResolver;
import org.mule.util.ClassUtils;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

final class DescriberResolver
{

    private final Map<String, DescriberResolverDelegate> delegates;

    DescriberResolver()
    {
        delegates = ImmutableMap.<String, DescriberResolverDelegate>builder()
                .put(AnnotationsBasedDescriber.DESCRIBER_ID, createAnnotationDescriberDelegate())
                .build();
    }

    Describer resolve(ExtensionManifest manifest, ClassLoader classLoader)
    {
        DescriberResolverDelegate delegate = delegates.get(manifest.getDescriberManifest().getId());
        if (delegate == null)
        {
            throw new IllegalArgumentException();
        }

        return delegate.resolve(manifest, classLoader);
    }

    private DescriberResolverDelegate createAnnotationDescriberDelegate()
    {
        return (manifest, classLoader) -> {

            String type = manifest.getDescriberManifest().getProperties().get(AnnotationsBasedDescriber.TYPE_PROPERTY_NAME);
            if (StringUtils.isBlank(type))
            {
                throw new IllegalArgumentException();
            }

            Class<?> extensionType;
            try
            {
                extensionType = ClassUtils.loadClass(type, classLoader);
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException();
            }

            return new AnnotationsBasedDescriber(extensionType, new StaticVersionResolver(manifest.getVersion()));
        };
    }

    @FunctionalInterface
    private interface DescriberResolverDelegate
    {

        Describer resolve(ExtensionManifest manifest, ClassLoader classLoader);
    }
}

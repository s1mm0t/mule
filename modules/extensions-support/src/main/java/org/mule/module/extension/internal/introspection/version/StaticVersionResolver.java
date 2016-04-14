/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal.introspection.version;

import org.mule.extension.api.annotation.Extension;

public final class StaticVersionResolver implements VersionResolver
{

    private final String version;

    public StaticVersionResolver(String version)
    {
        this.version = version;
    }

    @Override
    public String resolveVersion(Extension extension)
    {
        return version;
    }
}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal.capability.xml.schema;

import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.property.XmlModelProperty;
import org.mule.extension.api.resources.GeneratedResource;
import org.mule.module.extension.internal.config.ExtensionNamespaceHandler;

public class SpringHandlerBundleResourceFactory extends AbstractXmlResourceFactory
{

    @Override
    protected GeneratedResource generateXmlResource(ExtensionModel extensionModel, XmlModelProperty xmlModelProperty)
    {
        String content = String.format("%s=%s", xmlModelProperty.getSchemaLocation(), ExtensionNamespaceHandler.class.getName());
        return new GeneratedResource("spring.handlers", escape(content).getBytes());
    }
}

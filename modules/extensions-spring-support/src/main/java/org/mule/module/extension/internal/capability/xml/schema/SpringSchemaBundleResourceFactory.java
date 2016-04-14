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

public class SpringSchemaBundleResourceFactory extends AbstractXmlResourceFactory
{

    @Override
    protected GeneratedResource generateXmlResource(ExtensionModel extensionModel, XmlModelProperty xmlModelProperty)
    {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(getSpringSchemaBundle(xmlModelProperty, xmlModelProperty.getSchemaVersion()));
        contentBuilder.append(getSpringSchemaBundle(xmlModelProperty, "current"));

        return new GeneratedResource("spring.schemas", contentBuilder.toString().getBytes());
    }

    private String getSpringSchemaBundle(XmlModelProperty xmlProperty, String version)
    {
        String filename = getXsdFileName(xmlProperty);
        return escape(String.format("%s/%s/%s=META-INF/%s\n", xmlProperty.getSchemaLocation(), version, filename, filename));
    }
}

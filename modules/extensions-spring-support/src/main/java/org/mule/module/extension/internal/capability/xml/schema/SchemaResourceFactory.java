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

public class SchemaResourceFactory extends AbstractXmlResourceFactory
{

    @Override
    protected GeneratedResource generateXmlResource(ExtensionModel extensionModel, XmlModelProperty xmlModelProperty)
    {
        String schema = new SchemaGenerator().generate(extensionModel, xmlModelProperty);
        return new GeneratedResource(getXsdFileName(xmlModelProperty), schema.getBytes());
    }
}

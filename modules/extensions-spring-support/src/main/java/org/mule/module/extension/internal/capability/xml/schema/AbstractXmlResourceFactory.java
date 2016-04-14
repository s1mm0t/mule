/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal.capability.xml.schema;

import static org.mule.module.extension.internal.capability.xml.schema.model.SchemaConstants.XSD_EXTENSION;
import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.property.XmlModelProperty;
import org.mule.extension.api.resources.GeneratedResource;
import org.mule.extension.api.resources.spi.GeneratedResourceFactory;

import java.util.Optional;
import java.util.Properties;

abstract class AbstractXmlResourceFactory implements GeneratedResourceFactory
{

    @Override
    public Optional<GeneratedResource> generateResource(ExtensionModel extensionModel)
    {
        XmlModelProperty xmlProperty = extensionModel.getModelProperty(XmlModelProperty.class).orElse(null);

        return xmlProperty == null
               ? Optional.empty()
               : Optional.of(generateXmlResource(extensionModel, xmlProperty));
    }

    protected abstract GeneratedResource generateXmlResource(ExtensionModel extensionModel, XmlModelProperty xmlModelProperty);

    /**
     * Colon is a special character for the {@link Properties} class
     * that spring uses to parse the bundle. Thus, such character needs to be escaped
     * with a backslash
     *
     * @param content the content to be escaped
     * @return the escaped content
     */
    protected String escape(String content)
    {
        return content.replaceAll(":", "\\\\:");
    }

    protected String getXsdFileName(XmlModelProperty xmlModelProperty)
    {
        return String.format("mule-%s%s", xmlModelProperty.getNamespace(), XSD_EXTENSION);
    }
}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal.resources;

import static org.mule.module.extension.internal.ExtensionProperties.EXTENSION_MANIFEST_FILE_NAME;
import static org.mule.module.extension.internal.introspection.describer.AnnotationsBasedDescriber.DESCRIBER_ID;
import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.manifest.ExtensionManifestBuilder;
import org.mule.extension.api.persistence.manifest.ExtensionManifestSerializer;
import org.mule.extension.api.resources.ResourcesGenerator;
import org.mule.extension.api.resources.spi.GeneratedResourceContributor;
import org.mule.module.extension.internal.introspection.describer.AnnotationsBasedDescriber;
import org.mule.module.extension.internal.model.property.ImplementingTypeModelProperty;

import java.util.Optional;

public class ExtensionManifestGenerator implements GeneratedResourceContributor
{

    @Override
    public void contribute(ExtensionModel extensionModel, ResourcesGenerator resourcesGenerator)
    {
        Optional<ImplementingTypeModelProperty> typeProperty = extensionModel.getModelProperty(ImplementingTypeModelProperty.class);

        if (!typeProperty.isPresent())
        {
            return;
        }

        ExtensionManifestBuilder builder = new ExtensionManifestBuilder();
        builder.setName(extensionModel.getName())
                .setDescription(extensionModel.getDescription())
                .setVersion(extensionModel.getVersion())
                .withDescriber()
                .setId(DESCRIBER_ID)
                .addProperty(AnnotationsBasedDescriber.TYPE_PROPERTY_NAME, typeProperty.get().getType().getName());

        String manifestXml = new ExtensionManifestSerializer().serialize(builder.build());
        resourcesGenerator.get(EXTENSION_MANIFEST_FILE_NAME).getContentBuilder().append(manifestXml);
    }
}

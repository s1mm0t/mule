/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal.resources;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.when;
import org.mule.api.registry.ServiceRegistry;
import org.mule.extension.api.introspection.ExtensionModel;
import org.mule.extension.api.introspection.property.SubTypesModelProperty;
import org.mule.extension.api.introspection.property.XmlModelProperty;
import org.mule.extension.api.resources.GeneratedResource;
import org.mule.extension.api.resources.ResourcesGenerator;
import org.mule.module.extension.internal.capability.xml.schema.SchemaResourceFactory;
import org.mule.module.extension.internal.capability.xml.schema.SpringHandlerBundleResourceFactory;
import org.mule.module.extension.internal.capability.xml.schema.SpringSchemaBundleResourceFactory;
import org.mule.module.extension.internal.config.ExtensionNamespaceHandler;
import org.mule.tck.junit4.AbstractMuleTestCase;
import org.mule.tck.size.SmallTest;

import java.util.Optional;

import javax.annotation.processing.ProcessingEnvironment;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@SmallTest
@RunWith(MockitoJUnitRunner.class)
public class XmlGeneratedResourcesTestCase extends AbstractMuleTestCase
{

    private static final String EXTENSION_NAME = "extension";
    private static final String EXTENSION_VERSION = "version";
    private static final String SCHEMA_LOCATION = "mulesoft.com/extension";
    private static final String UNESCAPED_LOCATION_PREFIX = "http://";
    private static final String ESCAPED_LOCATION_PREFIX = "http\\://";
    private static final String SCHEMA_NAME = "mule-extension.xsd";

    @Mock
    private ExtensionModel extensionModel;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private ServiceRegistry serviceRegistry;

    @Mock
    private ProcessingEnvironment processingEnvironment;

    private ResourcesGenerator generator;

    private XmlModelProperty xmlModelProperty;

    private SpringHandlerBundleResourceFactory springHandlerFactory = new SpringHandlerBundleResourceFactory();
    private SpringSchemaBundleResourceFactory springSchemaBundleResourceFactory = new SpringSchemaBundleResourceFactory();
    private SchemaResourceFactory schemaResourceFactory = new SchemaResourceFactory();

    @Before
    public void before()
    {
        xmlModelProperty = new XmlModelProperty(EXTENSION_VERSION, EXTENSION_NAME, UNESCAPED_LOCATION_PREFIX + SCHEMA_LOCATION);
        when(extensionModel.getModelProperty(XmlModelProperty.class)).thenReturn(Optional.of(xmlModelProperty));
        when(extensionModel.getModelProperty(SubTypesModelProperty.class)).thenReturn(Optional.empty());

        generator = new AnnotationProcessorResourceGenerator(asList(springHandlerFactory, springSchemaBundleResourceFactory, schemaResourceFactory), processingEnvironment);

        when(extensionModel.getName()).thenReturn(EXTENSION_NAME);
        when(extensionModel.getVersion()).thenReturn(EXTENSION_VERSION);
    }

    @Test
    public void generateSchema() throws Exception
    {
        GeneratedResource resource = schemaResourceFactory.generateResource(extensionModel).get();
        assertThat(StringUtils.isBlank(new String(resource.getContent())), is(false));
    }

    @Test
    public void springHandlers() throws Exception
    {
        GeneratedResource resource = springHandlerFactory.generateResource(extensionModel).get();

        assertThat(resource.getPath(), equalTo("spring.handlers"));
        assertThat(String.format("%s=%s", ESCAPED_LOCATION_PREFIX + SCHEMA_LOCATION, ExtensionNamespaceHandler.class.getName()), equalTo(new String(resource.getContent())));
    }

    @Test
    public void springSchemas() throws Exception
    {
        GeneratedResource resource = springSchemaBundleResourceFactory.generateResource(extensionModel).get();
        assertThat(resource.getPath(), equalTo("spring.schemas"));

        StringBuilder expected = new StringBuilder();
        expected.append(String.format("%s/%s/%s=META-INF/%s\n", ESCAPED_LOCATION_PREFIX + SCHEMA_LOCATION, EXTENSION_VERSION, SCHEMA_NAME, SCHEMA_NAME));
        expected.append(String.format("%s/current/%s=META-INF/%s\n", ESCAPED_LOCATION_PREFIX + SCHEMA_LOCATION, SCHEMA_NAME, SCHEMA_NAME));


        assertThat(expected.toString(), equalTo(new String(resource.getContent())));
    }
}

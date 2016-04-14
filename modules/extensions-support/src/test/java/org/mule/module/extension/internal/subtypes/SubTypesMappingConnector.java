/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.internal.subtypes;

import org.mule.extension.api.annotation.Extension;
import org.mule.extension.api.annotation.ImportType;
import org.mule.extension.api.annotation.Operations;
import org.mule.extension.api.annotation.Parameter;
import org.mule.extension.api.annotation.SubTypeMapping;
import org.mule.extension.api.annotation.capability.Xml;
import org.mule.extension.api.annotation.connector.Providers;
import org.mule.module.extension.HeisenbergExtension;
import org.mule.module.extension.model.Ricin;
import org.mule.module.extension.vegan.ApplesBasket;
import org.mule.module.extension.vegan.VeganExtension;

@Extension(name = "SubtypesConnector", description = "Test connector for pojo subtype mapping")
@Operations(TestOperationsWithSubTypeMapping.class)
@Providers(SubTypesConnectionProvider.class)
@SubTypeMapping(baseType = Shape.class, subTypes = {Square.class, Triangle.class})
@SubTypeMapping(baseType = Door.class, subTypes = {HouseDoor.class, CarDoor.class})
@ImportType(type = Ricin.class, from = HeisenbergExtension.class)
@ImportType(type = ApplesBasket.class, from = VeganExtension.class)
@Xml(namespace = "subtypes", namespaceLocation = "http://www.mulesoft.org/schema/mule/subtypes")
public class SubTypesMappingConnector
{

    @Parameter
    private Ricin ricin;

    @Parameter
    private Shape abstractShape;

    @Parameter
    private Door doorInterface;

    @Parameter
    private Square explicitSquare;

    @Parameter
    private FinalPojo finalPojo;

    public Shape getAbstractShape()
    {
        return abstractShape;
    }

    public Door getDoorInterface()
    {
        return doorInterface;
    }

    public Square getExplicitSquare()
    {
        return explicitSquare;
    }

    public FinalPojo getFinalPojo()
    {
        return finalPojo;
    }
}

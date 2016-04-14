/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.extension.vegan;

import org.mule.tck.testmodels.fruit.Apple;

import java.util.List;

public class ApplesBasket
{

    private List<Apple> apples;

    public List<Apple> getApples()
    {
        return apples;
    }

    public void setApples(List<Apple> apples)
    {
        this.apples = apples;
    }
}

/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.validation.internal.validator;

import org.mule.extension.validation.internal.ValidationContext;

import java.util.Locale;

/**
 * A {@link NumberValidator} which tests {Short} numbers
 *
 * @since 3.7.0
 */
public class ShortValidator extends NumberValidator
{

    public ShortValidator(NumberValidationOptions options, ValidationContext validationContext)
    {
        super(options, validationContext);
    }

    @Override
    protected Number validateWithPattern(String value, String pattern, Locale locale)
    {
        return org.apache.commons.validator.routines.ShortValidator.getInstance().validate(value, pattern, locale);
    }

    @Override
    protected Number validateWithoutPattern(String value, Locale locale)
    {
        return org.apache.commons.validator.routines.ShortValidator.getInstance().validate(value, locale);
    }

    /**
     * @return the {@link Short} class
     */
    @Override
    protected Class<? extends Number> getNumberType()
    {
        return Short.class;
    }
}
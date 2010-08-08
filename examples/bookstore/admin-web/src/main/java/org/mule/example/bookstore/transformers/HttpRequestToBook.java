/*
 * $Id$
 * --------------------------------------------------------------------------------------
 * Copyright (c) MuleSource, Inc.  All rights reserved.  http://www.mulesource.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.example.bookstore.transformers;

import org.mule.api.MuleEvent;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerMessagingException;
import org.mule.example.bookstore.Book;
import org.mule.example.bookstore.BookstoreAdminMessages;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.DataTypeFactory;
import org.mule.util.StringUtils;

/**
 * Transforms a Map of HttpRequest parameters into a Book object.  
 * The request parameters are always strings (they come from the HTML form), 
 * so we need to parse and convert them to their appropriate types.
 */
public class HttpRequestToBook extends AbstractMessageTransformer
{
    public HttpRequestToBook()
    {
        super();
        registerSourceType(Object.class);
        setReturnDataType(DataTypeFactory.create(Book.class));
    }

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding, MuleEvent event) throws TransformerMessagingException
    {
        String author = message.getOutboundProperty("author");
        String title = message.getOutboundProperty("title");
        String price = message.getOutboundProperty("price");

        if (StringUtils.isBlank(author))
        {
            throw new TransformerMessagingException(BookstoreAdminMessages.missingAuthor(), event, this);
        }
        if (StringUtils.isBlank(title))
        {
            throw new TransformerMessagingException(BookstoreAdminMessages.missingTitle(), event, this);
        }
        if (StringUtils.isBlank(price))
        {
            throw new TransformerMessagingException(BookstoreAdminMessages.missingPrice(), event, this);
        }

        return new Book(author, title, Double.parseDouble(price));
    }
}

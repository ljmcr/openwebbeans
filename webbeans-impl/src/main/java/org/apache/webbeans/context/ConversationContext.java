/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.webbeans.context;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.spi.Contextual;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.context.creational.BeanInstanceBag;
import org.apache.webbeans.conversation.ConversationImpl;

/**
 * Conversation context implementation.
 * This reflects THE current Conversation (there can only be one active at a time for a thread).
 * It should not be confused with the Map of conversationId -> Conversation
 * which we internally store in the SessionContext.
 */
public class ConversationContext extends PassivatingContext implements Serializable
{
    private static final long serialVersionUID = 2L;

    private ConversationImpl conversation;

    // for serialisation
    public ConversationContext()
    {
        this(WebBeansContext.currentInstance());
    }

    /**
     * Constructor
     */
    public ConversationContext(WebBeansContext webBeansContext)
    {
        super(ConversationScoped.class);
        this.conversation = new ConversationImpl(webBeansContext);
    }

    @Override
    public void setComponentInstanceMap()
    {
        componentInstanceMap = new ConcurrentHashMap<Contextual<?>, BeanInstanceBag<?>>();
    }

    public ConversationImpl getConversation()
    {
        return conversation;
    }

}

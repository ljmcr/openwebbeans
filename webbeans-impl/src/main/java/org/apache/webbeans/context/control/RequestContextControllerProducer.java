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

package org.apache.webbeans.context.control;

import org.apache.webbeans.config.WebBeansContext;
import org.apache.webbeans.context.creational.CreationalContextImpl;
import org.apache.webbeans.portable.AbstractProducer;

import javax.enterprise.context.control.RequestContextController;
import javax.enterprise.inject.spi.Interceptor;
import java.util.Map;

public class RequestContextControllerProducer extends AbstractProducer<RequestContextController>
{
    private final WebBeansContext webBeansContext;

    public RequestContextControllerProducer(WebBeansContext webBeansContext)
    {
        this.webBeansContext = webBeansContext;
    }

    @Override
    protected RequestContextController produce(Map<Interceptor<?>, ?> interceptorInstances,
                                               CreationalContextImpl<RequestContextController> creationalContext)
    {
        return new OwbRequestContextController(webBeansContext);
    }
}

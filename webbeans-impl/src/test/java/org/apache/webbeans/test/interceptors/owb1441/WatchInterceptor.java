/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.webbeans.test.interceptors.owb1441;

import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.InvocationContext;

public class WatchInterceptor {

  public WatchInterceptor(String totallyUselessParamJustToNotHaveADefaultCt) {

  }

  private static boolean observed = false;

  @AroundInvoke
  public Object invoke(InvocationContext context) throws Exception
  {
    System.out.println("I am watching you " + context.getMethod());
    observed = true;

    return context.proceed();
  }

  public static boolean isObserved()
  {
    boolean wasObserved = observed;
    observed = false;
    return wasObserved;
  }

}

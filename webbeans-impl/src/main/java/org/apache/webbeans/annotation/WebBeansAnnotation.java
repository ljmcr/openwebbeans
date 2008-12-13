/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.webbeans.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javassist.util.proxy.MethodHandler;

import javax.webbeans.ExecutionException;

import org.apache.webbeans.util.Asserts;

/**
 * Defines the {@link Annotation} implementation. It is used
 * for creating annotations from the XML configuration files.
 * 
 * @author <a href="mailto:gurkanerdogdu@yahoo.com">Gurkan Erdogdu</a>
 * @since 1.0
 */
public class WebBeansAnnotation implements Annotation, MethodHandler
{	
	/**Annotation class type for this annotation*/
	private Class<? extends Annotation> annotationType = null;
	
	/**Annotation members map values*/
	private Map<String, Object> annotationMembersValueMap = new HashMap<String, Object>();
	
	/**Annotation type class methods members*/
	private Method[] members = null;
	
	/**
	 * Creates new annotation type object from given annotation type
	 * @param annotationType annotation class type
	 */
	public WebBeansAnnotation(Class<? extends Annotation> annotationType)
	{
		this.annotationType = annotationType;
		this.members = annotationType.getDeclaredMethods();
		
		/*Set default method values*/
		setDefaultValues();
	}
	
	/**
	 * Javassist proxy object method handler. It is used
	 * in equality comparison with {@link Annotation} types.
	 * 
	 * <p>
	 * If method is not implemented by this proxy, then proceed method is null.
	 * So, if the method is in the implemented annotation member method, then it is
	 * taken from the map values of this annotation member values.
	 * </p>
	 * 
	 * @param self proxy object
	 * @param m method invoked method
	 * @param proceed actual method on this proxy object self
	 * @param args method arguments
	 * 
	 * @throws Throwable if any exception occurs
	 */
	public Object invoke(Object self, Method m, Method proceed, Object[] args) throws Throwable
	{
		WebBeansAnnotation anno = (WebBeansAnnotation)self;
		
		if(isMethodExist(m) && proceed == null)
		{
			return anno.getAnnotationMembersValueMap().get(m.getName());
		}
		
		return proceed.invoke(self, args);
	}	
	
	/**
	 * Gets 
	 * 
	 * @return the unmodifiable map
	 */
	public Map<String, Object> getAnnotationMembersValueMap()
	{
		return Collections.unmodifiableMap(this.annotationMembersValueMap);
	}
	
	public Class<? extends Annotation> annotationType()
	{
		return annotationType;
	}
	
	public void setMemberValue(String memberName, Object memberValue)
	{
		Asserts.assertNotNull(memberValue,"memberName parameter can not be null");
		Asserts.assertNotNull(memberValue,"memberValue parameter can not be null");
		
		this.annotationMembersValueMap.put(memberName, memberValue);
	}

	@Override
	public String toString()
	{
		
		String string = "@" + annotationType().getName() + "(";
		for (int i = 0; i < members.length; i++)
		{
			string += members[i].getName() + "=";
			string += this.invoke(members[i]);
			if (i < members.length - 1)
			{
				string += ",";
			}
		}
		return string + ")";
	}

	@Override
	public boolean equals(Object other)
	{
		if (other instanceof Annotation)
		{
			Annotation that = (Annotation) other;
			if (this.annotationType().equals(that.annotationType()))
			{
				for (Method member : members)
				{
					Object thisValue = this.invoke(member);
					Object thatValue = invoke(member,that);
					if (!thisValue.equals(thatValue))
					{
						return false;
					}
				}
				return true;
			}
		}
		else if(other instanceof WebBeansAnnotation)
		{
			WebBeansAnnotation that = (WebBeansAnnotation) other;
			if (this.annotationType().equals(that.annotationType()))
			{
				for (Method member : members)
				{
					Object thisValue = this.invoke(member);
					Object thatValue = that.invoke(member);
					if (!thisValue.equals(thatValue))
					{
						return false;
					}
				}
				return true;
			}
			
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		int hashCode = 0;
		for (Method member : members)
		{
			int memberNameHashCode = 127 * member.getName().hashCode();
			int memberValueHashCode = invoke(member).hashCode();
			hashCode += memberNameHashCode ^ memberValueHashCode;
		}
		return hashCode;
	}

	private  Object invoke(Method method)
	{
		String memberName = method.getName();
		
		return this.annotationMembersValueMap.get(memberName);
	}	
	
	private static Object invoke(Method method, Object instance)
	{
		try
		{
			return method.invoke(instance);
			
		} catch (IllegalArgumentException e)
		{
			throw new ExecutionException("Error checking value of member method " + method.getName() + " on " + method.getDeclaringClass(), e);
		} catch (IllegalAccessException e)
		{
			throw new ExecutionException("Error checking value of member method " + method.getName() + " on " + method.getDeclaringClass(), e);
		} catch (InvocationTargetException e)
		{
			throw new ExecutionException("Error checking value of member method " + method.getName() + " on " + method.getDeclaringClass(), e);
		}
	}

	private boolean isMethodExist(Method method)
	{
		for(Method m : members)
		{
			if(m.equals(method))
			{
				return true;
			}
		}
		
		return false;
	}
	
	private void setDefaultValues()
	{
		for(Method m : members)
		{
			Object defValue = m.getDefaultValue();
			if(defValue != null)
			{
				this.annotationMembersValueMap.put(m.getName(), defValue);
			}
		}
	}
		

}
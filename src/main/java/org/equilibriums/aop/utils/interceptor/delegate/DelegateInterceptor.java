/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.equilibriums.aop.utils.interceptor.delegate;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.beanutils.PropertyUtils;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * <p>Selects delegate from specified delegate map with a key fetched from method arguments using specified argument index and property path.</p>
 * 
 * <p>This interceptor uses reflection to lookup delegate key and might introduce performance overhead of about 2 times. 
 * 
 * <p><u>For more on how to use this interceptor see Overview section of this Javadoc.</u></p>
 * 
 * @author Tsolak Petrosian
 */
public class DelegateInterceptor implements MethodInterceptor {
	
	private boolean proceed = false; 
	
	private int argumentIndex = 0;
	private String propertyPath = null;
	private Map< Object, Object > delegateMap = null;
	
	public boolean isProceed() {
		return proceed;
	}

	public void setProceed(boolean proceed) {
		this.proceed = proceed;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	public void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
	}

	public String getPropertyPath() {
		return propertyPath;
	}

	public void setPropertyPath(String propertyPath) {
		this.propertyPath = propertyPath;
	}

	public Map<Object, Object> getDelegateMap() {
		return delegateMap;
	}

	public void setDelegateMap(Map<Object, Object> delegateMap) {
		this.delegateMap = delegateMap;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {		
		Object[] arguments = invocation.getArguments();
		if (arguments.length == 0) return null;		
		Object key = getArgumentPropertyValue(arguments, argumentIndex, propertyPath);
		Object delegate = delegateMap.get(key);
		if (delegate == null) return proceed ? invocation.proceed() : null;
		return invocation.getMethod().invoke(delegate, arguments);
	}
	
	private Object getArgumentPropertyValue(Object[] arguments, Integer argumentIndex, String propertyPath){
		if ( StringUtils.isBlank( propertyPath ) ) return arguments[argumentIndex];
		
		Object fromPropertyValue = null;
		try{ fromPropertyValue = PropertyUtils.getProperty( arguments[argumentIndex], propertyPath ); }
		catch(Exception e){throw new RuntimeException(e);}		
		
		return fromPropertyValue;
   } 
}

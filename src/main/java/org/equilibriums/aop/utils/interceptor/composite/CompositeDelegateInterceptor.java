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
package org.equilibriums.aop.utils.interceptor.composite;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * <p>Will delegate method calls this interceptor is configured for to specified list of delegates.</p>
 *  
 * <p>Return value from delegate calls are handled by first {@link ReturnValueHandler} that will return true from its 
 * {@link ReturnValueHandler#supports} method.
 * If no {@link ReturnValueHandler} are specified will return null or result of invocation.proceed() if proceed property is true (false by default).</p>
 * 
 * <p><u>For more on how to use this interceptor see Overview section of this Javadoc.</u></p>
 * 
 * @author Tsolak Petrosian
 */
public class CompositeDelegateInterceptor implements MethodInterceptor {

	private boolean proceed = false; 
	
	private Object[] delegates = null;
	
	private ReturnValueHandler[] delegateReturnValueHandlers = null;

	
	public boolean isProceed() {
		return proceed;
	}

	public void setProceed(boolean proceed) {
		this.proceed = proceed;
	}

	public Object[] getDelegates() {
		return delegates;
	}

	public void setDelegates(Object[] delegates) {
		this.delegates = delegates;
	}

	public ReturnValueHandler[] getDelegateReturnValueHandlers() {
		return delegateReturnValueHandlers;
	}

	public void setDelegateReturnValueHandlers(ReturnValueHandler[] delegateReturnValueHandlers) {
		this.delegateReturnValueHandlers = delegateReturnValueHandlers;
	}

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		if ( delegates == null || delegates.length == 0 ) throw new IllegalArgumentException(
		"delegates property needs to be specified and cannot be empty" );

		Method invocationMethod = invocation.getMethod();
		Object[] invocationMethodArguments = invocation.getArguments();
			
		Object[] returnValues = new Object[ delegates.length ];
		for ( int i = 0; i < delegates.length; i++ ) returnValues[i] = invocationMethod.invoke(
		delegates[i], invocationMethodArguments );
				
		Class<?> returnType = invocationMethod.getReturnType();
		
		if ( returnType.equals(Void.TYPE) ) return null;
		
		if ( delegateReturnValueHandlers != null ) for ( int i = 0; i < delegateReturnValueHandlers.length; i++ ) if ( 
		delegateReturnValueHandlers[i].supports( returnType, returnValues ) ) return delegateReturnValueHandlers[i].getReturnValue( 
		returnType, returnValues );
		
		return proceed ? invocation.proceed() : null;
	}
}

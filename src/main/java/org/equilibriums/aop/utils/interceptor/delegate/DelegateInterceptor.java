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
import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * <p>Will delegate method calls this interceptor is configured for to specified delegates.</p>
 * 
 * <p>You can optionally override delegate's method to be called by specifying delegateMethodMap.</p>
 * 
 * <p>Return value from delegate calls are handled by first {@link DelegateReturnValueHandler} that will return true from its 
 * {@link DelegateReturnValueHandler#supports} method.
 * If no {@link DelegateReturnValueHandler} are specified will return null.</p>
 * 
 * <p>Here are some Design Pattern use cases that can be achieved by this interceptor without declaring any new type.</p> 
 * 
 * 
 * <h5>Delegate Pattern:</h5>
 * <p>
 * <ul>
 * <li>Specify single delegate that has same method this interceptor is configured for.</li>
 * <li>Configure proxy that is going to use this interceptor as described in Overview section of this Javadoc.</li>
 * </ul>
 * </p>
 * 
 * <h5>Composite Pattern:</h5>
 * <p>
 * <ul>
 * <li>Specifiy one or more delegates that have same method this interceptor is configured for.</li>
 * <li>Select appropriate ReturnValueHandler handlers depending on return value of the method you are intercepting. 
 *  For void method you leave the handlers empty .</li>
 * <li>Configure proxy that is going to use this interceptor as described in Overview section of this Javadoc.</li>
 * </ul>
 * </p>
 * 
 * <p><u>For more on how to use this interceptor see Overview section of this Javadoc.</u></p>
 * 
 * @author Tsolak Petrosian
 */
public class DelegateInterceptor implements MethodInterceptor {

	private List< Object > delegates = null;
	private Map< Object, Method > delegateMethodMap = null;
	
	private List<DelegateReturnValueHandler> delegateReturnValueHandlers = null;

	public List< Object > getDelegates() {
    	return delegates;
    }

	public void setDelegates( List< Object > delegates ) {
    	this.delegates = delegates;
    }

	/**
	 * Map where key is delegate and value is the method to be called
	 * by this interceptor instead of using invocation.getMethod() method.
	 * 
	 * @return
	 */
	public Map< Object, Method > getDelegateMethodMap() {
    	return delegateMethodMap;
    }

	public void setDelegateMethodMap( Map< Object, Method > delegateMethodMap ) {
    	this.delegateMethodMap = delegateMethodMap;
    }

	public List< DelegateReturnValueHandler > getDelegateReturnValueHandlers() {
    	return delegateReturnValueHandlers;
    }

	public void setDelegateReturnValueHandlers( List< DelegateReturnValueHandler > delegateReturnValueHandlers ) {
    	this.delegateReturnValueHandlers = delegateReturnValueHandlers;
    }

	@Override
	public Object invoke( MethodInvocation invocation ) throws Throwable {
		if ( delegates == null || delegates.size() == 0 ) throw new IllegalArgumentException( "delegates property needs to specified and cannot be empty" );

		Method invocationMethod = invocation.getMethod();
		Object[] invocationMethodArguments = invocation.getArguments();
			
		List< Object > returnValues = new ArrayList< Object >();
		for ( Object delegate : delegates )	returnValues.add( resolveDelegateMethod(invocationMethod, delegate).invoke(
		delegate, invocationMethodArguments ) );
				
		Class<?> returnType = invocationMethod.getReturnType();
		
		if ( returnType.equals(Void.TYPE) ) return null;
		
		if ( delegateReturnValueHandlers != null ) for (DelegateReturnValueHandler delegateReturnValueHandler:delegateReturnValueHandlers) if ( 
		delegateReturnValueHandler.supports( returnType, returnValues ) ) return delegateReturnValueHandler.getReturnValue( 
		returnType, returnValues );
		
		return null;
	}
	
	private Method resolveDelegateMethod(Method invocationMethod, Object delegate){
		if (delegateMethodMap == null) return invocationMethod;
		Method delegateMethod = delegateMethodMap.get(delegate);
		if (delegateMethod == null) return invocationMethod;
		else return delegateMethod;
	}
}

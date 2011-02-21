/* Copyright 2004, 2005, 2006 Acegi Technology Pty Limited
 *
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
package org.equilibriums.aop.utils.interceptor.stub;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * 
 * @author Tsolak Petrosian
 * 
 */
public class StubWithThrowableInterceptor implements MethodInterceptor {

	private Throwable throwable = null;

	public Throwable getThrowable() {
    	return throwable;
    }

	public void setThrowable( Throwable throwable ) {
    	this.throwable = throwable;
    }

    @Override
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public Object invoke( MethodInvocation invocation ) throws Throwable {
		Method method = invocation.getMethod();
        
		if ( throwable == null ) throw new IllegalArgumentException("Throwable property cannot be null");

		//dont check is method declares this exception when throwable is a type of RuntimeException
		if (throwable instanceof RuntimeException) throw throwable;
				
		//if we have got here, it means that throwable is checked exception and user specified to check it against declared checked exceptions
		Class[] exceptions = method.getExceptionTypes();
		if (exceptions.length == 0) throw new IllegalArgumentException( String.format(
		"The method %s does not declare any checked axception and throwable %s is checked exception", method.getName(), throwable ) );
		
		for (Class exClass:exceptions) if ( exClass.isAssignableFrom( throwable.getClass() ) ) throw throwable;
		
		throw new IllegalArgumentException( String.format(
		"Throwable is not assignable from any of methods declared exceptions for method %s and throwable %s", method.getName(), throwable ) );
	}
}

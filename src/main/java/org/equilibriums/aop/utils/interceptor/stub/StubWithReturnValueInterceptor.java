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
 * <p>
 * Will intercept a method call and follow rules defined below without calling
 * target method.
 * </p>
 * 
 * <p>
 * <ul>
 * <li>
 * 1. Given method "void method1(...)", will do nothing no matter what
 * returnValue is set to.</li>
 * <li>
 * 2. Given method "String method2(...)" and returnValue is set to
 * "Hello World", will return "Hellow World" no matter what checkReturnValueType
 * is set to.</li>
 * <li>
 * 3. Given method "String method3(...)" and returnValue is set to "new Object",
 * will throw IllegalStateException if checkReturnValueType is true otherwise
 * will return "new Object".</li>
 * <li>
 * 4. Given method "Object method4(...)" and returnValue is set to
 * "Hello World", will return "Hellow World" no matter what checkReturnValueType
 * is set to.</li>
 * </ul>
 * </p>
 * 
 * <p>
 * <b>Note:</b> that this needs to be configured with some type of AOP pointcut
 * or proxy bean such as
 * <b>org.springframework.aop.framework.ProxyFactoryBean</b>.
 * </p>
 * 
 * <p>
 * Here is an example configuration with ProxyFactoryBean that will intercept
 * all interface method calls without actual implementation of the interface.
 * </p>
 * <p>
 * <b>Note:</b> All methods need to be intercepted otherwise
 * NullPointerException will be thrown from ProxyFactoryBean.
 * </p>
 * 
 * <pre>
 * {@code
 * 
 * <bean id="target1" class="org.springframework.aop.framework.ProxyFactoryBean">
 *     <property name="interfaces"><list><value>org.equilibriums.aop.utils.interceptors.NoOpWithReturnValueInterceptorTest$TargetClass</value></list></property>
 *     <property name="targetSource">
 *         <util:constant static-field="org.springframework.aop.target.EmptyTargetSource.INSTANCE"/>
 *     </property>
 *     <property name="interceptorNames">
 *         <list>
 *             <value>interceptors</value>
 *         </list>
 *     </property>
 * </bean>
 * 
 * <bean id="interceptors" class="org.springframework.aop.support.DefaultPointcutAdvisor">
 *     <property name="pointcut">
 *         <!-- You are free to use any other Pointcut implementation that suits your needs -->
 *         <bean id="customerPointcut" class="org.springframework.aop.support.NameMatchMethodPointcut">
 *             <property name="mappedName" value="*" />
 *         </bean>
 *     </property>
 *     <property name="advice">
 *         <bean class="org.equilibriums.aop.utils.interceptors.NoOpWithReturnValueInterceptor">
 *             <property name="returnValue" value="can be anything from string to bean reference"/>
 *         </bean>
 *     </property>
 * </bean>
 * }
 * </pre>
 * <p>
 * Here is an example configuration with ProxyFactoryBean that will intercept
 * some of class method calls on concrete class instance.
 * </p>
 * 
 * <pre>
 * {@code
 *  
 * <bean id="target1" class="org.springframework.aop.framework.ProxyFactoryBean">
 *     <property name="target">
 *         <bean class="someBeanClassThatNeedsMethod1ToBeIntercepted">
 *         </bean>
 *     </property>
 *     <property name="interceptorNames">
 *         <list>
 *             <value>interceptors</value>
 *         </list>
 *     </property>
 * </bean>
 * 
 * <bean id="interceptors" class="org.springframework.aop.support.DefaultPointcutAdvisor">
 *     <property name="pointcut">
 *         <!-- You are free to use any other Pointcut implementation that suits your needs -->
 *         <bean id="customerPointcut" class="org.springframework.aop.support.NameMatchMethodPointcut">
 *             <property name="mappedName" value="method1" />
 *         </bean>
 *     </property>
 *     <property name="advice">
 *         <bean class="org.equilibriums.aop.utils.interceptors.NoOpWithReturnValueInterceptor">
 *             <property name="returnValue" value="can be anything from string to bean reference"/>
 *         </bean>
 *     </property>
 * </bean>
 * }
 * </pre>
 * 
 * @author Tsolak Petrosian
 * 
 */
public class StubWithReturnValueInterceptor implements MethodInterceptor {

	private Object returnValue = null;
	private boolean checkReturnValueType = true;

	public Object getReturnValue() {
		return returnValue;
	}

	public void setReturnValue( Object returnValue ) {
		this.returnValue = returnValue;
	}

	public boolean isCheckReturnValueType() {
		return checkReturnValueType;
	}

	public void setCheckReturnValueType( boolean checkReturnValueType ) {
		this.checkReturnValueType = checkReturnValueType;
	}

	@Override
	public Object invoke( MethodInvocation invocation ) throws Throwable {
		Method method = invocation.getMethod();
        		
		// Always return null for void methods even though specified return
		// value is not null.
		// This is necessary when there is method overloading where one of the
		// methods happens to be void
		if ( method.getReturnType() == void.class ) return null;

		// Always return null if returnValue is null since type checking wont
		// make sense in this case
		if ( returnValue == null ) return null;

		if ( checkReturnValueType && !method.getReturnType().isAssignableFrom( returnValue.getClass() ) ) throw new IllegalArgumentException( String.format(
		"Return value specified is not assignable from method %s with return type of %s", method.getName(), method.getReturnType() ) );

		return returnValue;
	}
}

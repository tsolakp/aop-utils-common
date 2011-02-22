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
package org.equilibriums.aop.utils.interceptor.stub;

import org.junit.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.aopalliance.intercept.MethodInvocation;

public class StubWithReturnValueInterceptorTest {
	
    private static interface TypeWithVoidReturnTypeMethod {
		
		public void method1(String arg1);		
		
	}
    
    private static interface TypeWithStringReturnTypeMethod {		

    	public String method2(String arg1);
		
	}
    
    private static interface TypeWithObjectReturnTypeMethod {		

    	public Object method3(String arg1);
		
	}
	
	private StubWithReturnValueInterceptor noOpWithReturnValueInterceptor = null;
		
	@Before
	public void initialize(){
		noOpWithReturnValueInterceptor = new StubWithReturnValueInterceptor();
	}
	
	@Test
	public void testWith_TypeWithVoidReturnTypeMethod_NullReturnValue() throws Throwable {			
		noOpWithReturnValueInterceptor.setReturnValue(null);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithVoidReturnTypeMethod.class.getMethod("method1", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertNull( noOpWithReturnValueInterceptor.invoke(methodInvocation) );		
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_TypeWithVoidReturnTypeMethod_NonNullReturnValue() throws Throwable {		
		noOpWithReturnValueInterceptor.setReturnValue("Hello World");
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithVoidReturnTypeMethod.class.getMethod("method1", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertNull( noOpWithReturnValueInterceptor.invoke(methodInvocation) );		
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_TypeWithVoidReturnTypeMethod_NonNullReturnValue_CheckReturnValueTypeFalse() throws Throwable {		
		noOpWithReturnValueInterceptor.setReturnValue("Hello World");
		noOpWithReturnValueInterceptor.setCheckReturnValueType(false);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithVoidReturnTypeMethod.class.getMethod("method1", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertNull( noOpWithReturnValueInterceptor.invoke(methodInvocation) );		
		verify(methodInvocation);
	}
	
	
	@Test
	public void testWith_TypeWithStringReturnTypeMethod_NullReturnValue() throws Throwable {		
		noOpWithReturnValueInterceptor.setReturnValue(null);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithStringReturnTypeMethod.class.getMethod("method2", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertNull( noOpWithReturnValueInterceptor.invoke(methodInvocation) );		
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_TypeWithStringReturnTypeMethod_NullReturnValue_CheckReturnValueTypeFalse() throws Throwable {		
		noOpWithReturnValueInterceptor.setReturnValue(null);
		noOpWithReturnValueInterceptor.setCheckReturnValueType(false);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithStringReturnTypeMethod.class.getMethod("method2", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertNull( noOpWithReturnValueInterceptor.invoke(methodInvocation) );		
		verify(methodInvocation);
	}	
	
	@Test
	public void testWith_TypeWithStringReturnTypeMethod_ObjectReturnValue() throws Throwable {	
		Object returnValue = new Object();
		noOpWithReturnValueInterceptor.setReturnValue(returnValue);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithStringReturnTypeMethod.class.getMethod("method2", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		try{ noOpWithReturnValueInterceptor.invoke(methodInvocation); fail(); }
		catch(IllegalArgumentException e){ assertEquals( e.getMessage(), "Return value specified is not assignable from method method2 with return type of class java.lang.String" ); }
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_TypeWithStringReturnTypeMethod_ObjectReturnValue_CheckReturnValueTypeFalse() throws Throwable {	
		Object returnValue = new Object();
		noOpWithReturnValueInterceptor.setReturnValue(returnValue);
		noOpWithReturnValueInterceptor.setCheckReturnValueType(false);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithStringReturnTypeMethod.class.getMethod("method2", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertEquals( noOpWithReturnValueInterceptor.invoke(methodInvocation), returnValue );		
		verify(methodInvocation);
	}
	
	
	@Test
	public void testWith_TypeWithObjectReturnTypeMethod_StringReturnValue() throws Throwable {	
		noOpWithReturnValueInterceptor.setReturnValue("Hello World");
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithObjectReturnTypeMethod.class.getMethod("method3", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertEquals( noOpWithReturnValueInterceptor.invoke(methodInvocation), "Hello World" );		
		verify(methodInvocation);
	}
	
	@Test
	public void testWith_TypeWithObjectReturnTypeMethod_StringReturnValue_CheckReturnValueTypeFalse() throws Throwable {	
		noOpWithReturnValueInterceptor.setReturnValue("Hello World");
		noOpWithReturnValueInterceptor.setCheckReturnValueType(false);
		
		MethodInvocation methodInvocation = createMock(MethodInvocation.class);
		
		expect( methodInvocation.getMethod() ).andReturn( TypeWithObjectReturnTypeMethod.class.getMethod("method3", new Class[]{String.class} ) );
		
		replay(methodInvocation);
		assertEquals( noOpWithReturnValueInterceptor.invoke(methodInvocation), "Hello World" );		
		verify(methodInvocation);
	}
}

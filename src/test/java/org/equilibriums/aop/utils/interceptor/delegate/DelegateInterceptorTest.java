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

import java.util.List;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.reflect.Method;

import org.junit.*;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.aopalliance.intercept.MethodInvocation;

public class DelegateInterceptorTest {
	
	private DelegateInterceptor delegateInterceptor = null;
	
    private static interface DelegateWithVoidReturnTypeMethod {
				
		public void method1(String arg1);		
		
	}
    
    private static interface DelegateWithListReturnTypeMethod {		

    	public List<Object> method2(String arg1);
		
	}	
		
	@Before
	public void initialize(){
		delegateInterceptor = new DelegateInterceptor();
	}
	
	//null or empty delegate
	
	@Test
	public void testWithNullDelegateList() throws Throwable {			
		try{ delegateInterceptor.invoke( null ); }
		catch(IllegalArgumentException e){ assertEquals( e.getMessage(), "delegates property needs to specified and cannot be empty" ); }		
	}
	
	@Test
	public void testWithEmptyDelegateList() throws Throwable {		
		delegateInterceptor.setDelegates(  new ArrayList<Object>() );
		try{ delegateInterceptor.invoke( null ); }
		catch(IllegalArgumentException e){ assertEquals( e.getMessage(), "delegates property needs to specified and cannot be empty" ); }		
	}
	
	// WithNullDelegateReturnValueHandlerListAndOneDelegate
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		// even though we are calling void method other interceptors down the
		// chain can still return non null value
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		delegate1.method1("a1");
		expectLastCall();

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		delegate1.method1("a1");
		expectLastCall();

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod_NoDelegateMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );
		
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		
		//with error we should not even call on delegate
		//delegate1.method1("a1");
		//expectLastCall();

		replay( delegate1, methodInvocation );
		try{ delegateInterceptor.invoke( methodInvocation ); fail(); }
		catch(Exception e){}
		verify( delegate1, methodInvocation );
	}
	
	
    // WithNullDelegateReturnValueHandlerListAndTwoDelegate
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_2DelegateWithVoidReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		DelegateWithVoidReturnTypeMethod delegate2 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		delegate1.method1("a1");
		expectLastCall();
		
		delegate2.method1("a1");
		expectLastCall();

		replay( delegate1, delegate2, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_2DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithListReturnTypeMethod delegate2 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);
		
		ArrayList<Object> result2 = new ArrayList<Object>();
		expect( delegate2.method2("a1") ).andReturn(result2);

		replay( delegate1, delegate2, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_1DelegateWithListReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		DelegateWithListReturnTypeMethod delegate2 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		delegateMethodMap.put( delegate2, DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		delegate1.method1("a1");
		expectLastCall();
		
		ArrayList<Object> result2 = new ArrayList<Object>();
		expect( delegate2.method2("a1") ).andReturn(result2);

		replay( delegate1, delegate2, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod_NoDelegateMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithVoidReturnTypeMethod delegate2 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );
		
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result2 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result2);
		
		//with error we should not even call on delegate
		//delegate2.method1("a1");
		//expectLastCall();

		replay( delegate1, methodInvocation );
		try{ delegateInterceptor.invoke( methodInvocation ); fail(); }
		catch(Exception e){}
		verify( delegate1, methodInvocation );
	}
    
	// WithDelegateReturnValueHandlerListAndOneDelegate
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
				
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1 ) );
		//with void we should not call DelegateReturnValueHandler, so dont do any easymock expect(..)
		//expect( delegateReturnValueHandler1.supports( Void.TYPE, returnValues ) ).andReturn(true);
		
		delegate1.method1("a1");
		expectLastCall();

		replay( delegate1, methodInvocation, delegateReturnValueHandler1 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation, delegateReturnValueHandler1 );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );
		
		ArrayList<Object> result1 = new ArrayList<Object>();
		result1.add("l1");
		expect( delegate1.method2("a1") ).andReturn(result1);
		
		ArrayList<Object> returnValues = new ArrayList<Object>();
		returnValues.add(result1);		
		expect( delegateReturnValueHandler1.supports( List.class, returnValues ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues ) ).andReturn(true);
		//we should not call delegateReturnValueHandler3.supports since delegateReturnValueHandler2.supports returns true
		//expect( delegateReturnValueHandler3.supports( List.class, returnValues ) ).andReturn(true);
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues ) ).andReturn(handledReturnValue);
		
		replay( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		
		assertSame( result, handledReturnValue );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);
		
		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );
		
		//with void we should not call DelegateReturnValueHandler, so dont do any easymock expect(..)
		//ArrayList<Object> returnValues = new ArrayList<Object>();
		//returnValues.add(result1);		
		//expect( delegateReturnValueHandler1.supports( List.class, returnValues ) ).andReturn(false);
		//expect( delegateReturnValueHandler2.supports( List.class, returnValues ) ).andReturn(true);
		
		//Object handledReturnValue = new Object();
		//expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues ) ).andReturn(handledReturnValue);
		
		replay( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		delegate1.method1("a1");
		expectLastCall();
		
		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );

		ArrayList<Object> returnValues = new ArrayList<Object>();
		returnValues.add(null);	
	    expect( delegateReturnValueHandler1.supports( List.class, returnValues ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues ) ).andReturn(true);
		//we should not call delegateReturnValueHandler3.supports since delegateReturnValueHandler2.supports returns true
		//expect( delegateReturnValueHandler3.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues ) ).andReturn(handledReturnValue);
		
		replay( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		
		assertSame( result, handledReturnValue );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod_NoDelegateMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );
		
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		
		//with error we should not even call on delegate
		//delegate1.method1("a1");
		//expectLastCall();

		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );
		
		//with error we should not even call on DelegateReturnValueHandlers
		//ArrayList<Object> returnValues = new ArrayList<Object>();
		//returnValues.add(null);	
	    //expect( delegateReturnValueHandler1.supports( List.class, returnValues ) ).andReturn(false);
		//expect( delegateReturnValueHandler2.supports( List.class, returnValues ) ).andReturn(true);
		
		//Object handledReturnValue = new Object();
		//expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues ) ).andReturn(handledReturnValue);
		
		replay( delegate1, methodInvocation );
		try{ delegateInterceptor.invoke( methodInvocation ); fail(); }
		catch(Exception e){}
		verify( delegate1, methodInvocation );
	}
	
   // WithDelegateReturnValueHandlerListAndTwoDelegate
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_2DelegateWithVoidReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		DelegateWithVoidReturnTypeMethod delegate2 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1 ) );
		//with void we should not call DelegateReturnValueHandler, so dont do any easymock expect(..)
		//expect( delegateReturnValueHandler1.supports( Void.TYPE, returnValues ) ).andReturn(true);
		
		delegate1.method1("a1");
		expectLastCall();
		
		delegate2.method1("a1");
		expectLastCall();

		replay( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1 );
		
		assertNull( result );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_2DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithListReturnTypeMethod delegate2 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);
		
		ArrayList<Object> result2 = new ArrayList<Object>();
		expect( delegate2.method2("a1") ).andReturn(result2);

		ArrayList<Object> returnValues = new ArrayList<Object>();
		returnValues.add(result1);	
		returnValues.add(result2);	
		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );
		
		expect( delegateReturnValueHandler1.supports( List.class, returnValues ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues ) ).andReturn(true);
		//we should not call delegateReturnValueHandler3.supports since delegateReturnValueHandler2.supports returns true
		//expect( delegateReturnValueHandler3.supports( List.class, returnValues ) ).andReturn(true)
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues ) ).andReturn(handledReturnValue);
		
		replay( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		
		assertSame( result, handledReturnValue );
	}	
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_1DelegateWithVoidReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithVoidReturnTypeMethod delegate2 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		delegateMethodMap.put( delegate2, DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);
		
		delegate2.method1("a1");
		expectLastCall();
		
		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );
		
		//since ethodInvocation.getMethod() returns void method we should not call DelegateReturnValueHandler, so dont do any easymock expect(..)
		//ArrayList<Object> returnValues = new ArrayList<Object>();
		//returnValues.add(result1);	
		//returnValues.add(null);	
		//expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		//expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		//Object handledReturnValue = new Object();
		//expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
		replay( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		
		assertNull( result );
	}	
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithVoidReturnTypeMethod delegate2 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		delegateMethodMap.put( delegate2, DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);
		
		delegate2.method1("a1");
		expectLastCall();
		
		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );
		
		ArrayList<Object> returnValues = new ArrayList<Object>();
		returnValues.add(result1);	
		returnValues.add(null);	
		expect( delegateReturnValueHandler1.supports( List.class, returnValues ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues ) ).andReturn(true);
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues ) ).andReturn(handledReturnValue);
		
		replay( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		
		assertSame( result, handledReturnValue );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod_NoDelegateMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithVoidReturnTypeMethod delegate2 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );
		
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );

		ArrayList<Object> result2 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result2);
		
		//with error we should not even call on delegate
		//delegate2.method1("a1");
		//expectLastCall();

		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );
		
		//with there is an error we should not even call DelegateReturnValueHandlers
		//ArrayList<Object> returnValues = new ArrayList<Object>();
		//returnValues.add(result1);	
		//returnValues.add(null);	
		//expect( delegateReturnValueHandler1.supports( List.class, returnValues ) ).andReturn(false);
		//expect( delegateReturnValueHandler2.supports( List.class, returnValues ) ).andReturn(true);
		
		//Object handledReturnValue = new Object();
		//expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues  ) ).andReturn(handledReturnValue);
		
		replay( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		try{ delegateInterceptor.invoke( methodInvocation ); fail(); }
		catch(Exception e){}
		verify( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
	}
}

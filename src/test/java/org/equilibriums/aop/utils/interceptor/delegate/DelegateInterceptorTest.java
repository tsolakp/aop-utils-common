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
	
    private static interface EventHandler {
		
		public String getName();		
				
		public void handleEvent(String event);
	}
	
	@Test
	public void test(){
		EventHandler delegate1 = createMock( EventHandler.class );
		EventHandler delegate2 = createMock( EventHandler.class );
						    
		DelegateInterceptor delegateInterceptor = new DelegateInterceptor();
		delegateInterceptor.setDelegates( Arrays.<Object>asList( delegate1, delegate2 ) );
				
		org.springframework.aop.framework.ProxyFactory factory = new org.springframework.aop.framework.ProxyFactory( 
	    new Class[]{EventHandler.class} );
		//use this as placeholder for real target that implements SomeInterface.		
		factory.setTargetSource( org.springframework.aop.target.EmptyTargetSource.INSTANCE );
		
		org.equilibriums.aop.utils.interceptor.stub.StubWithReturnValueInterceptor getNameStubInterceptor = 
		new org.equilibriums.aop.utils.interceptor.stub.StubWithReturnValueInterceptor();		
		getNameStubInterceptor.setReturnValue( "Proxy Name" );
				
		org.equilibriums.aop.utils.interceptor.stub.StubWithReturnValueInterceptor nullStubInterceptor = 
		new org.equilibriums.aop.utils.interceptor.stub.StubWithReturnValueInterceptor();		
		nullStubInterceptor.setReturnValue(null);
				
		org.springframework.aop.support.NameMatchMethodPointcut nameMatchMethodPointcut1 = new org.springframework.aop.support.NameMatchMethodPointcut();	
		nameMatchMethodPointcut1.setMappedName( "getName" );		
		factory.addAdvisor(  new org.springframework.aop.support.DefaultPointcutAdvisor( nameMatchMethodPointcut1, getNameStubInterceptor ) );
		
		org.springframework.aop.support.NameMatchMethodPointcut nameMatchMethodPointcut2 = new org.springframework.aop.support.NameMatchMethodPointcut();	
		nameMatchMethodPointcut2.setMappedName( "handleEvent" );		
		factory.addAdvisor(  new org.springframework.aop.support.DefaultPointcutAdvisor( nameMatchMethodPointcut2, delegateInterceptor ) );
		factory.addAdvisor(  new org.springframework.aop.support.DefaultPointcutAdvisor( nameMatchMethodPointcut2, nullStubInterceptor ) );
		
		EventHandler eventHandlerTarget = (EventHandler)factory.getProxy();
	    
		delegate1.handleEvent( "event1" );
		expectLastCall();
		
		delegate2.handleEvent( "event1" );
		expectLastCall();
		
	    replay(delegate1);
	    assertEquals( eventHandlerTarget.getName(), "Proxy Name" );
	    eventHandlerTarget.handleEvent( "event1" );
	    verify(delegate1);
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
		Object proceededReturnValue = new Object();
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		delegate1.method1("a1");
		expectLastCall();

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		Object proceededReturnValue = new Object();
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_VoidReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		Object proceededReturnValue = new Object();
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		Object proceededReturnValue = new Object();
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		delegate1.method1("a1");
		expectLastCall();

		replay( delegate1, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod_NoDelegateMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );
		
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		//with error we should not even proceeed
		//expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
		
		// even though we are calling void method other interceptors down the
		// chain can still return non null value
		Object proceededReturnValue = new Object();
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		delegate1.method1("a1");
		expectLastCall();
		
		delegate2.method1("a1");
		expectLastCall();

		replay( delegate1, delegate2, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_NullDelegateReturnValueHandlerList_2DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithListReturnTypeMethod delegate2 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		Object proceededReturnValue = new Object();
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		ArrayList<Object> result1 = new ArrayList<Object>();
		expect( delegate1.method2("a1") ).andReturn(result1);
		
		ArrayList<Object> result2 = new ArrayList<Object>();
		expect( delegate2.method2("a1") ).andReturn(result2);

		replay( delegate1, delegate2, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation );
		
		assertSame( result, proceededReturnValue );
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

		Object proceededReturnValue = new Object();
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		delegate1.method1("a1");
		expectLastCall();
		
		ArrayList<Object> result2 = new ArrayList<Object>();
		expect( delegate2.method2("a1") ).andReturn(result2);

		replay( delegate1, delegate2, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation );
		
		assertSame( result, proceededReturnValue );
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
		//with error we should not even proceeed
		//expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
				
		// even though we are calling void method other interceptors down the
		// chain can still return non null value
		Object proceededReturnValue = new Object();
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1 ) );
		//with void we should not call DelegateReturnValueHandler, so dont do any easymock expect(..)
		//expect( delegateReturnValueHandler1.supports( Void.TYPE, returnValues, proceededReturnValue ) ).andReturn(true);
		
		delegate1.method1("a1");
		expectLastCall();

		replay( delegate1, methodInvocation, delegateReturnValueHandler1 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation, delegateReturnValueHandler1 );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		Object proceededReturnValue = new Object();
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
		expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		//we should not call delegateReturnValueHandler3.supports since delegateReturnValueHandler2.supports returns true
		//expect( delegateReturnValueHandler3.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
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

		Object proceededReturnValue = new Object();
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
		//expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		//expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		//Object handledReturnValue = new Object();
		//expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
		replay( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_1DelegateWithVoidReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithVoidReturnTypeMethod delegate1 = createMock( DelegateWithVoidReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1) );
		
		HashMap< Object, Method > delegateMethodMap = new HashMap< Object, Method >();
		delegateMethodMap.put( delegate1, DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		delegateInterceptor.setDelegateMethodMap( delegateMethodMap );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		Object proceededReturnValue = new Object();
		//we are using DelegateWithVoidReturnTypeMethod in this case to create wrong method that will be overridden by delegate method
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		delegate1.method1("a1");
		expectLastCall();
		
		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler2 = createMock(DelegateReturnValueHandler.class);
		DelegateReturnValueHandler delegateReturnValueHandler3 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1, 
	    delegateReturnValueHandler2, delegateReturnValueHandler3 ) );

		ArrayList<Object> returnValues = new ArrayList<Object>();
		returnValues.add(null);	
	    expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		//we should not call delegateReturnValueHandler3.supports since delegateReturnValueHandler2.supports returns true
		//expect( delegateReturnValueHandler3.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
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
		//with error we should not even proceeed
		//expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );
		
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
	    //expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		//expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		//Object handledReturnValue = new Object();
		//expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
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
		
		// even though we are calling void method other interceptors down the
		// chain can still return non null value
		Object proceededReturnValue = new Object();
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

		DelegateReturnValueHandler delegateReturnValueHandler1 = createMock(DelegateReturnValueHandler.class);
		delegateInterceptor.setDelegateReturnValueHandlers( Arrays.<DelegateReturnValueHandler>asList( delegateReturnValueHandler1 ) );
		//with void we should not call DelegateReturnValueHandler, so dont do any easymock expect(..)
		//expect( delegateReturnValueHandler1.supports( Void.TYPE, returnValues, proceededReturnValue ) ).andReturn(true);
		
		delegate1.method1("a1");
		expectLastCall();
		
		delegate2.method1("a1");
		expectLastCall();

		replay( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1 );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate1, delegate2, methodInvocation, delegateReturnValueHandler1 );
		
		assertSame( result, proceededReturnValue );
	}
	
	@Test
	public void testWith_DelegateReturnValueHandlerList_2DelegateWithListReturnTypeMethod_ListReturnTypeInvocationMethod() throws Throwable {
		DelegateWithListReturnTypeMethod delegate1 = createMock( DelegateWithListReturnTypeMethod.class );
		DelegateWithListReturnTypeMethod delegate2 = createMock( DelegateWithListReturnTypeMethod.class );
		
		delegateInterceptor.setDelegates( Arrays.<Object>asList(delegate1, delegate2) );
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		Object proceededReturnValue = new Object();
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
		
		expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		//we should not call delegateReturnValueHandler3.supports since delegateReturnValueHandler2.supports returns true
		//expect( delegateReturnValueHandler3.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true)
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
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

		Object proceededReturnValue = new Object();
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithVoidReturnTypeMethod.class.getMethod( "method1", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
		
		assertSame( result, proceededReturnValue );
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

		Object proceededReturnValue = new Object();
		expect( methodInvocation.getMethod() ).andReturn( DelegateWithListReturnTypeMethod.class.getMethod( "method2", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new String[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
		expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		Object handledReturnValue = new Object();
		expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
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
		//with error we should not even proceeed
		//expect( methodInvocation.proceed() ).andReturn( proceededReturnValue );

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
		//expect( delegateReturnValueHandler1.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(false);
		//expect( delegateReturnValueHandler2.supports( List.class, returnValues, proceededReturnValue ) ).andReturn(true);
		
		//Object handledReturnValue = new Object();
		//expect( delegateReturnValueHandler2.getReturnValue( List.class, returnValues, proceededReturnValue ) ).andReturn(handledReturnValue);
		
		replay( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
		try{ delegateInterceptor.invoke( methodInvocation ); fail(); }
		catch(Exception e){}
		verify( delegate1, methodInvocation, delegateReturnValueHandler1, delegateReturnValueHandler2, delegateReturnValueHandler3 );
	}
}

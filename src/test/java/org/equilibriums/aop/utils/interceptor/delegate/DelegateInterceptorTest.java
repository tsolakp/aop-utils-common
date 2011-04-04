package org.equilibriums.aop.utils.interceptor.delegate;

import java.util.Map;
import java.util.HashMap;

import java.lang.reflect.Method;
import java.lang.reflect.AccessibleObject;

import org.junit.*;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.aopalliance.intercept.MethodInvocation;

public class DelegateInterceptorTest {

	private DelegateInterceptor delegateInterceptor = null;
	
	//make it a class and have methods do something like calling "toString" method so that we can performance test it
    private static class Delegate {
    	
    	public void method1(){toString();}	
    	
		public void method2(String arg1){toString();}		
				
        public String method3(String arg1){return toString();}	
		
		public String method3(String arg1, Map<String, String> arg2){return toString();}
		
	}
    	
	@Before
	public void initialize(){
		delegateInterceptor = new DelegateInterceptor();
	}
	
	@Test
	public void testWithEmptyDelegateMap_ProceeedFalse() throws Throwable {	
		delegateInterceptor.setDelegateMap( new HashMap<Object, Object>() );
		delegateInterceptor.setProceed(false);
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"a1"} );
		
		replay( methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( methodInvocation );
		
		assertNull( result );
	}
	
	@Test
	public void testWithEmptyDelegateMap_ProceeedTrue() throws Throwable {	
		delegateInterceptor.setDelegateMap( new HashMap<Object, Object>() );
		delegateInterceptor.setProceed(true);
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"a1"} );
		expect( methodInvocation.proceed() ).andReturn( "p1" );
		
		replay( methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( methodInvocation );
		
		assertEquals( "p1", result );
	}
	
	
	@Test
	public void testWithDelegateMap_Method1_ProceeedFalse() throws Throwable {	
		delegateInterceptor.setProceed(false);
		
		Delegate delegate = createMock(Delegate.class);
		HashMap<Object, Object> delegateMap = new HashMap<Object, Object>();
		delegateMap.put("v1", delegate);
		delegateInterceptor.setDelegateMap(delegateMap);
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );
		
		//we should not even get invocation method with non arg methods
		//expect( methodInvocation.getMethod() ).andReturn( Delegate.class.getMethod( "method1", null ) );
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{} );
		
		//we should not call delegate with non arg methods
		//delegate.method1();
		//expectLastCall();
		
		replay( delegate, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate, methodInvocation );
		
		assertNull(result);
	}
	
	
	@Test
	public void testWithDelegateMap_Method3_1Arg_ProceeedFalse() throws Throwable {	
		delegateInterceptor.setProceed(false);
		
		Delegate delegate = createMock(Delegate.class);
		HashMap<Object, Object> delegateMap = new HashMap<Object, Object>();
		delegateMap.put("v1", delegate);
		delegateInterceptor.setDelegateMap(delegateMap);
		
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( Delegate.class.getMethod( "method3", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"v1"} );
		
		expect( delegate.method3("v1") ).andReturn("r1");
		
		replay( delegate, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate, methodInvocation );
		
		assertEquals( "r1", result );
	}
		
	@Test
	public void testWithDelegateMap_Method3_1Arg_ProceeedTrue() throws Throwable {	
		delegateInterceptor.setProceed(true);

		Delegate delegate = createMock(Delegate.class);
		HashMap<Object, Object> delegateMap = new HashMap<Object, Object>();
		delegateMap.put("v1", delegate);
		delegateInterceptor.setDelegateMap(delegateMap);
				
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( Delegate.class.getMethod( "method3", new Class[] {String.class} ) );
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"v1"} );
		
		expect( delegate.method3("v1") ).andReturn("r1");
		
		replay( delegate, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate, methodInvocation );
		
		assertEquals( "r1", result );
	}
	
	
	@Test
	public void testWithDelegateMap_Method3_2Args() throws Throwable {	
		delegateInterceptor.setProceed(true);
		delegateInterceptor.setArgumentIndex(1);
		delegateInterceptor.setPropertyPath("key1");
		
		Delegate delegate = createMock(Delegate.class);
		HashMap<Object, Object> delegateMap = new HashMap<Object, Object>();
		delegateMap.put("value1", delegate);
		delegateInterceptor.setDelegateMap(delegateMap);
				
		MethodInvocation methodInvocation = createMock( MethodInvocation.class );

		expect( methodInvocation.getMethod() ).andReturn( Delegate.class.getMethod( "method3", new Class[]{String.class, Map.class} ) );
		
		HashMap<String, String> arg1 = new HashMap<String, String>();
		arg1.put("key1", "value1");
		expect( methodInvocation.getArguments() ).andReturn( new Object[]{"v1", arg1} );
		
		expect( delegate.method3("v1", arg1) ).andReturn("r1");
		
		replay( delegate, methodInvocation );
		Object result = delegateInterceptor.invoke( methodInvocation );
		verify( delegate, methodInvocation );
		
		assertEquals( "r1", result );
	}
	
	//@Test
	public void testPerformance() throws Throwable{		
		delegateInterceptor.setProceed(true);
		delegateInterceptor.setArgumentIndex(1);
		delegateInterceptor.setPropertyPath("key1");
		
		Delegate delegate = new Delegate();
		HashMap<Object, Object> delegateMap = new HashMap<Object, Object>();
		delegateMap.put("value1", delegate);
		delegateInterceptor.setDelegateMap(delegateMap);
						
		HashMap<String, String> arg1 = new HashMap<String, String>();
		arg1.put("key1", "value1");	
		Object[] args = new Object[]{"v1", arg1}; 
		Method m = Delegate.class.getMethod( "method3", new Class[]{String.class, Map.class} );		
		MethodInvocation methodInvocation = createMethodInvocation(m, args);
					
		int count = 10000000;
		
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < count; i++)	delegate.method3("v1", arg1);	
		long endTime = System.currentTimeMillis();
		
		long regularRunTime = endTime - startTime;
		
		startTime = System.currentTimeMillis();
		for (int i = 0; i < count; i++)	delegateInterceptor.invoke( methodInvocation );			
		endTime = System.currentTimeMillis();	
		
		long aopRunTime = endTime - startTime;
				
		double percentDiff = ( (double)(aopRunTime - regularRunTime)/(double)regularRunTime )*100.0;
		
		System.out.println("aopRunTime: " + aopRunTime + "  regularRunTime: " + regularRunTime + ", percentDiff: " + percentDiff  );
	}
	
	
	private MethodInvocation createMethodInvocation(final Method m, final Object[] arguments){
		return ( new MethodInvocation(){

			@Override
			public Object[] getArguments() {
				return arguments;
			}

			@Override
			public Object proceed() throws Throwable {
				return null;
			}

			@Override
			public Object getThis() {
				return null;
			}

			@Override
			public AccessibleObject getStaticPart() {
				return null;
			}

			@Override
			public Method getMethod() {
				return m;
			}			
		});
	}
}

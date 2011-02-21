package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;

public class LastReturnValueHandlerTest {

    private LastReturnValueHandler lastReturnValueHandler = null;
	
	@Before
	public void initialize(){
		lastReturnValueHandler = new LastReturnValueHandler();
	}
	
	@Test
	public void testSupports(){
		assertTrue( lastReturnValueHandler.supports( Object.class, null, null ) );
	}
	
	@Test
	public void testGetReturnValue_SingleReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		assertSame( lastReturnValueHandler.getReturnValue( Object.class, returnValues, null ), o );
	}
	
	@Test
	public void testGetReturnValue_SingleReturnValue_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		assertSame( lastReturnValueHandler.getReturnValue( Object.class, returnValues, new Object() ), o );
	}
	
	
	@Test
	public void testGetReturnValue_2ReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( new Object() );
		returnValues.add( o );
		
		assertSame( lastReturnValueHandler.getReturnValue( Object.class, returnValues, null ), o );
	}
	
	@Test
	public void testGetReturnValue_2ReturnValue_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( new Object() );
		returnValues.add( o );
		
		assertSame( lastReturnValueHandler.getReturnValue( Object.class, returnValues, new Object() ), o );
	}
}

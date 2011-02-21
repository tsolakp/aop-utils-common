package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;

public class FirstReturnValueHandlerTest {

    private FirstReturnValueHandler firstReturnValueHandler = null;
	
	@Before
	public void initialize(){
		firstReturnValueHandler = new FirstReturnValueHandler();
	}
	
	@Test
	public void testSupports(){
		assertTrue( firstReturnValueHandler.supports( Object.class, null, null ) );
	}
	
	@Test
	public void testGetReturnValue_SingleReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		assertSame( firstReturnValueHandler.getReturnValue( Object.class, returnValues, null ), o );
	}
	
	@Test
	public void testGetReturnValue_SingleReturnValue_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		assertSame( firstReturnValueHandler.getReturnValue( Object.class, returnValues, new Object() ), o );
	}
	
	
	@Test
	public void testGetReturnValue_2ReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		returnValues.add( new Object() );
		
		assertSame( firstReturnValueHandler.getReturnValue( Object.class, returnValues, null ), o );
	}
	
	@Test
	public void testGetReturnValue_2ReturnValue_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		returnValues.add( new Object() );
		
		assertSame( firstReturnValueHandler.getReturnValue( Object.class, returnValues, new Object() ), o );
	}
}

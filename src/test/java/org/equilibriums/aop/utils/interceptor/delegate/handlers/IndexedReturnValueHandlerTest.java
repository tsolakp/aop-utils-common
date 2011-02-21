package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;
import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;

public class IndexedReturnValueHandlerTest {

    private IndexedReturnValueHandler indexedReturnValueHandler = null;
	
	@Before
	public void initialize(){
		indexedReturnValueHandler = new IndexedReturnValueHandler();
	}
	
	@Test
	public void testSupports(){
		assertTrue( indexedReturnValueHandler.supports( Object.class, null, null ) );
	}
	
	@Test
	public void testGetReturnValue_SingleReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		indexedReturnValueHandler.setIndex( 0 );
		assertSame( indexedReturnValueHandler.getReturnValue( Object.class, returnValues, null ), o );
	}
	
	@Test
	public void testGetReturnValue_SingleReturnValue_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		indexedReturnValueHandler.setIndex( 0 );
		assertSame( indexedReturnValueHandler.getReturnValue( Object.class, returnValues, new Object() ), o );
	}
	
	
	@Test
	public void testGetReturnValue_2ReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( new Object() );
		returnValues.add( o );
		
		indexedReturnValueHandler.setIndex( 1 );
		assertSame( indexedReturnValueHandler.getReturnValue( Object.class, returnValues, null ), o );
	}
	
	@Test
	public void testGetReturnValue_2ReturnValue_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( new Object() );
		returnValues.add( o );
		
		indexedReturnValueHandler.setIndex( 1 );
		assertSame( indexedReturnValueHandler.getReturnValue( Object.class, returnValues, new Object() ), o );
	}
}

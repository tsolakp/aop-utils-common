package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

import static org.junit.Assert.*;

public class ObjectCollectionReturnValueHandlerTest {

    private ObjectCollectionReturnValueHandler objectCollectionReturnValueHandler = null;
	
	@Before
	public void initialize(){
		objectCollectionReturnValueHandler = new ObjectCollectionReturnValueHandler();
	}
	
	@Test
	public void testSupports_ObjectReturnType(){
		assertTrue( objectCollectionReturnValueHandler.supports( Object.class, null, null ) );
	}
	
	@Test
	public void testSupports_GenericCollectionReturnType(){
		assertFalse( objectCollectionReturnValueHandler.supports( Collection.class, null, null ) );
	}
	
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		List<Object> result = ( List<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get( 0 ), o );
		assertNull( result.get( 1 ) );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2ReturnValue_NullProceededReturnValue(){
    	objectCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	
    	List<Object> returnValues = new ArrayList<Object>();
		Object o1 = new Object();
		returnValues.add( o1 );
		Object o2 = new Object();
		returnValues.add( o2 );
		
		HashSet<Object> result = ( HashSet<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
				
		assertEquals( result.size(), 3 );
		assertTrue( result.contains( o1 ) );
		assertTrue( result.contains( o2 ) );
		assertTrue( result.contains( null )  );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleReturnValue_ProceededReturnValue(){
    	objectCollectionReturnValueHandler.setCollectionClass( HashSet.class );
    	
		List<Object> returnValues = new ArrayList<Object>();
		Object o = new Object();
		returnValues.add( o );
		
		Object proceededReturnValue = new Object();
		
		HashSet<Object> result = ( HashSet<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 2 );
		assertTrue( result.contains( o ) );
		assertTrue( result.contains( proceededReturnValue )  );;
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2ReturnValue_ProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		Object o1 = new Object();
		returnValues.add( o1 );
		Object o2 = new Object();
		returnValues.add( o2 );
		
		Object proceededReturnValue = new Object();
		List<Object> result = ( List<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, proceededReturnValue );
		
		assertEquals( result.size(), 3 );
		assertEquals( result.get( 0 ), o1 );
		assertEquals( result.get( 1 ), o2 );
		assertEquals( result.get( 2 ), proceededReturnValue );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleListReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		List<Object> o = new ArrayList<Object>();
		returnValues.add( o );
		
		List<Object> result = ( List<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get( 0 ), o );
		assertNull( result.get( 1 ) );
	}
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2ListReturnValue_NullProceededReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		List<Object> o1 = new ArrayList<Object>();
		returnValues.add( o1 );
		List<Object> o2 = new ArrayList<Object>();
		returnValues.add( o2 );
		
		List<Object> result = ( List<Object> )objectCollectionReturnValueHandler.getReturnValue( Object.class, returnValues, null );
		
		assertEquals( result.size(), 3 );
		assertEquals( result.get( 0 ), o1 );
		assertEquals( result.get( 1 ), o2 );
		assertNull( result.get( 2 ) );
	}
}

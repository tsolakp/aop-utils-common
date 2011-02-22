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
package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

import static org.junit.Assert.*;

public class MergeMapReturnValueHandlerTest {

    private MergeMapReturnValueHandler mergeMapReturnValueHandler = null;
	
	@Before
	public void initialize(){
		mergeMapReturnValueHandler = new MergeMapReturnValueHandler();
	}
	
	@Test
	public void testSupports_ObjectReturnType(){
		assertFalse( mergeMapReturnValueHandler.supports( Object.class, null ) );
	}
	
	@Test
	public void testSupports_MapReturnType(){
		assertTrue( mergeMapReturnValueHandler.supports( Map.class, null ) );
	}
	
	@Test
	public void testSupports_HashMapReturnType(){
		assertTrue( mergeMapReturnValueHandler.supports( HashMap.class, null ) );
	}
	
	@Test
	public void testSupports_CollectionReturnType(){
		assertFalse( mergeMapReturnValueHandler.supports( Collection.class, null ) );
	}
	
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyMapReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 0 );
	}
    
        
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedMapReturnValue(){
    	mergeMapReturnValueHandler.setMapClass( TreeMap.class );
    	
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		TreeMap<Object, Object> result = ( TreeMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 1 );
		assertEquals( result.get( "o11" ), o11 );
	}    
      
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyMapReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		returnValues.add( o2 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 0 );
	}
        
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedMapReturnValue(){
		List<Object> returnValues = new ArrayList<Object>();
		
		HashMap<Object, Object> o1 = new HashMap<Object, Object>();
		Object o11 = new Object();
		o1.put( "o11", o11 );
		returnValues.add( o1 );
		
		HashMap<Object, Object> o2 = new HashMap<Object, Object>();
		Object o22 = new Object();
		o2.put( "o22", o22 );
		returnValues.add( o2 );
		
		HashMap<Object, Object> o3 = new HashMap<Object, Object>();
		returnValues.add( o3 );
		
		HashMap<Object, Object> result = ( HashMap<Object, Object> )mergeMapReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get("o11"), o11 );
		assertEquals( result.get("o22"), o22 );
	}    
}

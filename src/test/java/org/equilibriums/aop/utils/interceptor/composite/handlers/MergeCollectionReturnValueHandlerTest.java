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
package org.equilibriums.aop.utils.interceptor.composite.handlers;

import java.util.Map;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collection;

import org.junit.*;

import static org.junit.Assert.*;

public class MergeCollectionReturnValueHandlerTest {

    private MergeCollectionReturnValueHandler mergeCollectionReturnValueHandler = null;
	
	@Before
	public void initialize(){
		mergeCollectionReturnValueHandler = new MergeCollectionReturnValueHandler();
	}
	
	@Test
	public void testSupports_ObjectReturnType(){
		assertFalse( mergeCollectionReturnValueHandler.supports( Object.class, null ) );
	}
	
	@Test
	public void testSupports_CollectionReturnType(){
		assertTrue( mergeCollectionReturnValueHandler.supports( Collection.class, null ) );
	}
	
	@Test
	public void testSupports_ArrayListReturnType(){
		assertTrue( mergeCollectionReturnValueHandler.supports( ArrayList.class, null ) );
	}
	
	@Test
	public void testSupports_MapReturnType(){
		assertFalse( mergeCollectionReturnValueHandler.supports( Map.class, null ) );
	}
	
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SingleEmptyListReturnValue(){		
		List<Object> o1 = new ArrayList<Object>();
		Object[] returnValues = new Object[]{ o1 };
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 0 );
	}
    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_SinglePopulatedListReturnValue(){
    	mergeCollectionReturnValueHandler.setCollectionClass( HashSet.class );
		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		Object[] returnValues = new Object[]{ o1 };
		
		HashSet<Object> result = ( HashSet<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 1 );
		assertTrue( result.contains( o11 ) );
	}    
    
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2EmptyListReturnValue(){		
		List<Object> o1 = new ArrayList<Object>();
		List<Object> o2 = new ArrayList<Object>();
		Object[] returnValues = new Object[]{ o1, o2 };
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 0 );
	}
    
        
    @Test
	@SuppressWarnings( "unchecked" )
	public void testGetReturnValue_2PopulatedListReturnValue(){		
		List<Object> o1 = new ArrayList<Object>();
		Object o11 = new Object();
		o1.add( o11 );
		
		List<Object> o2 = new ArrayList<Object>();
		Object o22 = new Object();
		o2.add( o22 );
		
		List<Object> o3 = new ArrayList<Object>();
		
		Object[] returnValues = new Object[]{ o1, o2, o3 };
		
		List<Object> result = ( List<Object> )mergeCollectionReturnValueHandler.getReturnValue( Object.class, returnValues );
		
		assertEquals( result.size(), 2 );
		assertEquals( result.get(0), o11 );
		assertEquals( result.get(1), o22 );
	}
}

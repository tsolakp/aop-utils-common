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
		assertTrue( indexedReturnValueHandler.supports( Object.class, null ) );
	}
	
	@Test
	public void testGetReturnValue_SingleReturnValue(){		
		Object o = new Object();
		Object[] returnValues = new Object[]{ o };
		
		indexedReturnValueHandler.setIndex( 0 );
		assertSame( indexedReturnValueHandler.getReturnValue( Object.class, returnValues ), o );
	}
		
	@Test
	public void testGetReturnValue_2ReturnValue(){
		Object o = new Object();
		Object[] returnValues = new Object[]{ new Object(), o };
		
		indexedReturnValueHandler.setIndex( 1 );
		assertSame( indexedReturnValueHandler.getReturnValue( Object.class, returnValues ), o );
	}
}

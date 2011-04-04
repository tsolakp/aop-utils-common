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

import java.util.ArrayList;
import java.util.Collection;

import org.equilibriums.aop.utils.interceptor.composite.ReturnValueHandler;

/**
 * <p>Assumes returnType is {@link Object.class}. Will merge all {@link Object} return values from delegates into single {@link Collection} return value.</p>
 * <p>{@link #supports} method will return true only of returnType is {@link Object.class}.</p>
 * 
 * @author Tsolak Petrosian
 */
public class ObjectCollectionReturnValueHandler implements ReturnValueHandler {

	@SuppressWarnings( "rawtypes" )
    private Class< ? extends Collection > collectionClass = null;
	
    @SuppressWarnings( "rawtypes" )
    public Class< ? extends Collection > getCollectionClass() {
    	return collectionClass;
    }

	@SuppressWarnings( "rawtypes" )
    public void setCollectionClass( Class< ? extends Collection > collectionClass ) {
    	this.collectionClass = collectionClass;
    }

	@Override
	public boolean supports( Class<? extends Object> returnType, Object[] returnValues ){
	    return returnType.equals( Object.class );
    }
	
	@Override
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public Object getReturnValue( Class<? extends Object> returnType, Object[] returnValues ){
		Collection result = collectionClass != null? createInstance(collectionClass) : new ArrayList();
		for ( Object o:returnValues ) result.add(o);
		return result;
	}
	
	private static < T > T createInstance( Class< T > clazz ) {
		try { return clazz.newInstance(); } 
		catch ( Exception e ) {	throw new RuntimeException( e ); }
	}
}

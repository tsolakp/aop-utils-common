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
import java.util.HashMap;

import org.equilibriums.aop.utils.interceptor.composite.ReturnValueHandler;

/**
 * <p>Assumes returnType is of {@link Map} type. Will merge all {@link Map} return values from delegates into single {@link Map} return value.</p>
 * <p>{@link #supports} method will return true only of returnType is of {@link Map} sub type.</p>
 * 
 * @author Tsolak Petrosian
 */
public class MergeMapReturnValueHandler implements ReturnValueHandler {
	
	@SuppressWarnings( "rawtypes" )	
    private Class< ? extends Map > mapClass = null;
	
	@SuppressWarnings( "rawtypes" )
    public Class< ? extends Map > getMapClass() {
    	return mapClass;
    }

	@SuppressWarnings( "rawtypes" )
    public void setMapClass( Class< ? extends Map > mapClass ) {
    	this.mapClass = mapClass;
    }

	@Override
	public boolean supports( Class<? extends Object> returnType, Object[] returnValues ){
	    return Map.class.isAssignableFrom(returnType);
    }
	
	@Override
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public Object getReturnValue( Class<? extends Object> returnType, Object[] returnValues ){
		Map result = mapClass != null? createInstance(mapClass) : new HashMap();
		for ( Object o:returnValues ) if (o != null) result.putAll( (Map)o );
		return result;
	}
	
	private static < T > T createInstance( Class< T > clazz ) {
		try { return clazz.newInstance(); } 
		catch ( Exception e ) {	throw new RuntimeException( e ); }
	}
}

package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.Map;
import java.util.List;
import java.util.HashMap;

import org.equilibriums.aop.utils.interceptor.delegate.DelegateReturnValueHandler;

public class MergeMapReturnValueHandler implements DelegateReturnValueHandler {
	
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
	public boolean supports( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
	    return Map.class.isAssignableFrom(returnType);
    }
	
	@Override
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public Object getReturnValue( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		Map result = mapClass != null? createInstance(mapClass) : new HashMap();
		for ( Object o:returnValues ) if (o != null) result.putAll( (Map)o );
		if ( proceededReturnValue != null ) result.putAll( (Map)proceededReturnValue );
		return result;
	}
	
	private static < T > T createInstance( Class< T > clazz ) {
		try { return clazz.newInstance(); } 
		catch ( Exception e ) {	throw new RuntimeException( e ); }
	}
}

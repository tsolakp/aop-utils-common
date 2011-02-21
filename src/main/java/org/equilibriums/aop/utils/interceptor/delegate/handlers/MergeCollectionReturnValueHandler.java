package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

import org.equilibriums.aop.utils.interceptor.delegate.DelegateReturnValueHandler;

public class MergeCollectionReturnValueHandler implements DelegateReturnValueHandler {
	
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
	public boolean supports( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
	    return Collection.class.isAssignableFrom(returnType);
    }
	
	@Override
	@SuppressWarnings( { "rawtypes", "unchecked" } )
	public Object getReturnValue( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		Collection result = collectionClass != null? createInstance(collectionClass) : new ArrayList();
		for ( Object o:returnValues ) if (o != null) result.addAll( (Collection)o );
		if ( proceededReturnValue != null ) result.addAll( (Collection)proceededReturnValue );
		return result;
	}
	
	private static < T > T createInstance( Class< T > clazz ) {
		try { return clazz.newInstance(); } 
		catch ( Exception e ) {	throw new RuntimeException( e ); }
	}
}

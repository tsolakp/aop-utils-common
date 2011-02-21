package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;

import org.equilibriums.aop.utils.interceptor.delegate.DelegateReturnValueHandler;

public class IndexedReturnValueHandler implements DelegateReturnValueHandler {

	private Integer index = 0;
	
	public Integer getIndex() {
    	return index;
    }

	public void setIndex( Integer index ) {
    	this.index = index;
    }

	@Override
	public boolean supports( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		return true;
	}
	
	@Override
	public Object getReturnValue( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		return returnValues.get( index );
	}
}

package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;

import org.equilibriums.aop.utils.interceptor.delegate.DelegateReturnValueHandler;

public class FirstReturnValueHandler implements DelegateReturnValueHandler {

	@Override
	public boolean supports( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		return true;
	}
	
	@Override
	public Object getReturnValue( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		return returnValues.get(0);
	}
}

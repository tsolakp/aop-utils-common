package org.equilibriums.aop.utils.interceptor.delegate;

import java.util.List;

public interface DelegateReturnValueHandler {

	public boolean supports( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue );
	
	public Object getReturnValue( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue );
}

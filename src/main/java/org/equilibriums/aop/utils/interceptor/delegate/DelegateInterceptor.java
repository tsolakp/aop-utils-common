package org.equilibriums.aop.utils.interceptor.delegate;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author Tsolak Petrosian
 */
public class DelegateInterceptor implements MethodInterceptor {

	private List< Object > delegates = null;
	private Map< Object, Method > delegateMethodMap = null;
	
	private List<DelegateReturnValueHandler> delegateReturnValueHandlers = null;

	public List< Object > getDelegates() {
    	return delegates;
    }

	public void setDelegates( List< Object > delegates ) {
    	this.delegates = delegates;
    }

	public Map< Object, Method > getDelegateMethodMap() {
    	return delegateMethodMap;
    }

	public void setDelegateMethodMap( Map< Object, Method > delegateMethodMap ) {
    	this.delegateMethodMap = delegateMethodMap;
    }

	public List< DelegateReturnValueHandler > getDelegateReturnValueHandlers() {
    	return delegateReturnValueHandlers;
    }

	public void setDelegateReturnValueHandlers( List< DelegateReturnValueHandler > delegateReturnValueHandlers ) {
    	this.delegateReturnValueHandlers = delegateReturnValueHandlers;
    }

	@Override
	public Object invoke( MethodInvocation invocation ) throws Throwable {
		if ( delegates == null || delegates.size() == 0 ) throw new IllegalArgumentException( "delegates property needs to specified and cannot be empty" );

		Method invocationMethod = invocation.getMethod();
		Object[] invocationMethodArguments = invocation.getArguments();
			
		List< Object > returnValues = new ArrayList< Object >();
		for ( Object delegate : delegates )	returnValues.add( resolveDelegateMethod(invocationMethod, delegate).invoke(
		delegate, invocationMethodArguments ) );
		
		Object proceededReturnValue = invocation.proceed();
		
		Class<?> returnType = invocationMethod.getReturnType();
		
		if ( returnType.equals(Void.TYPE) ) return proceededReturnValue;
		
		if ( delegateReturnValueHandlers != null ) for (DelegateReturnValueHandler delegateReturnValueHandler:delegateReturnValueHandlers) if ( 
		delegateReturnValueHandler.supports( returnType, returnValues, proceededReturnValue ) ) return delegateReturnValueHandler.getReturnValue( 
		returnType, returnValues, proceededReturnValue );
		
		return proceededReturnValue;
	}
	
	private Method resolveDelegateMethod(Method invocationMethod, Object delegate){
		if (delegateMethodMap == null) return invocationMethod;
		Method delegateMethod = delegateMethodMap.get(delegate);
		if (delegateMethod == null) return invocationMethod;
		else return delegateMethod;
	}
}

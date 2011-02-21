package org.equilibriums.aop.utils.interceptor.delegate.handlers;

import java.util.List;

import org.equilibriums.aop.utils.interceptor.delegate.DelegateReturnValueHandler;

public class BooleanReturnValueHandler implements DelegateReturnValueHandler {

	public enum BooleanOperator{AND, OR};
	
	private BooleanOperator booleanOperator = BooleanOperator.AND; 
	
	public BooleanOperator getBooleanOperator() {
    	return booleanOperator;
    }

	public void setBooleanOperator( BooleanOperator booleanOperator ) {
    	this.booleanOperator = booleanOperator;
    }

	@Override
	public boolean supports( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		return returnType.equals( Boolean.class ) || returnType.equals( Boolean.TYPE );
	}
	
	@Override
	public Object getReturnValue( Class<? extends Object> returnType, List< Object > returnValues, Object proceededReturnValue ){
		Boolean result = null;			
		for ( Object o:returnValues ) if (o != null) result = ( result == null ? (Boolean)o : 
		eval( result, (Boolean)o, booleanOperator ) );
		if ( proceededReturnValue != null ) result = eval( result, (Boolean)proceededReturnValue, booleanOperator );
		return result;
	}
	
	private Boolean eval(Boolean b1, Boolean b2, BooleanOperator booleanOperator){
		if ( booleanOperator == BooleanOperator.AND ) return b1 && b2;
		else return b1 || b2;
	}
}

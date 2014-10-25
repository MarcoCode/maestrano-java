package com.maestrano.net;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MnoApiAccountResponse<T> {
	private Boolean success;
	private T data;
	private Map<String,String> errors;
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public Map<String,String> getErrors() {
		return errors;
	}
	public void setErrors(Map<String,String> errors) {
		this.errors = errors;
	}
	
	public String getErrorsAsString() {
		List<String> errorMsgs = new ArrayList<String>();
		for ( String paramName : this.errors.keySet()) {
			errorMsgs.add(paramName + ": " + this.errors.get(paramName));
		}
		
		return errorMsgs.toString();
	}
}
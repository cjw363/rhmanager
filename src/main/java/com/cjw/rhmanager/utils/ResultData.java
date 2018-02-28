package com.cjw.rhmanager.utils;


public class ResultData<T> {
	private int code;
    private String message;
    private T result;
	
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public ResultData(HandleEnum operateEnum) {
        this.code = operateEnum.getCode();
        this.message = operateEnum.getMessage();
    }
	
	public ResultData(HandleEnum operateEnum,T result) {
        this.code = operateEnum.getCode();
        this.message = operateEnum.getMessage();
        this.result = result;
    }
	
	public ResultData(HandleEnum operateEnum,String message) {
        this.code = operateEnum.getCode();
        this.message = message;
    }
}
package com.cjw.rhmanager.utils;


public enum HandleEnum {
    FAIL(0,"操作异常"),
    SUCCESS(1,"操作成功"),
    UNLOGIN(2,"未登录");

    private int code;
    private String message;

    HandleEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public static HandleEnum codeOf(int index){
        for (HandleEnum code : values()){
            if (code.getCode()==index){
                return code;
            }
        }
        return null;
    }
}
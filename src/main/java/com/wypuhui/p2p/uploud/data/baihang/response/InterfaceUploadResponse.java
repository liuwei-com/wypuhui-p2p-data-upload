package com.wypuhui.p2p.uploud.data.baihang.response;

/**
 * 接口上传数据响应类
 *
 */
public class InterfaceUploadResponse {

	/*
	 * 是否成功
	 */
	public boolean isSuccess = true;
	
	/*
	 * 错误信息
	 */
	private String errorMessage = "";

	/*
	 * 加密后的数据
	 */
	private String params;

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}
}

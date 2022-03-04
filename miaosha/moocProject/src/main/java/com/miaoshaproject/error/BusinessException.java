package com.miaoshaproject.error;


// 包装器, 业务异常类实现
public class BusinessException extends Exception implements CommonError{
    private CommonError commonError;

    //direct receive embusineessseror param to build
    public BusinessException(CommonError commonError){
        super();
        this.commonError = commonError;
    }
    // receive self define errmsg
    public BusinessException(CommonError commonError, String errMsg){
        super();
        this.commonError = commonError;
        this.commonError.setErrMsg(errMsg);
    }

    @Override
    public int getErrcode() {
        return this.commonError.getErrcode();
    }

    @Override
    public String getErrMsg() {
        return this.commonError.getErrMsg();
    }

    @Override
    public CommonError setErrMsg(String errMsg) {
        this.commonError.setErrMsg(errMsg);
        return this;
    }
}

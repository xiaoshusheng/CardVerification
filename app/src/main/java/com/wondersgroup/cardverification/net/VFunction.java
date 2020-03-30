package com.wondersgroup.cardverification.net;


import com.wondersgroup.cardverification.model.base.BaseResponse;

import io.reactivex.functions.Function;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: Function的封装 以便后续扩展
 */
public abstract class VFunction<T,R> implements Function<BaseResponse<T>,R> {
    @Override
    public R apply(BaseResponse<T> statusResponse) throws Exception {
        applyA(statusResponse.getData());
        return null;
    }
    protected abstract R applyA(T t) ;
}

package com.wondersgroup.cardverification.net;


import com.wondersgroup.cardverification.model.base.BaseResponse;

import io.reactivex.functions.BiFunction;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: ABiFunction封装 RxJava2操作符zip调用
 *      使用场景：当需要获取两个接口数据后，才展示UI 多个接口以此类推
 */
public abstract class VBiFunction<T1, T2, R> implements BiFunction<BaseResponse<T1>,BaseResponse<T2>,R> {
    @Override
    public R apply(BaseResponse<T1> t1StatusResponse, BaseResponse<T2> t2StatusResponse) throws Exception {
        //可以做状态码统一处理
        applyA(t1StatusResponse.getData(),t2StatusResponse.getData());
        return null;
    }
    protected abstract R applyA(T1 t1, T2 t2) ;
}

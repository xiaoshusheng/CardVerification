package com.wondersgroup.cardverification.net;


import com.wondersgroup.cardverification.app.Constant;
import com.wondersgroup.cardverification.model.base.BaseResponse;
import com.wondersgroup.cardverification.model.bean.UpdateAppBean;


import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Great by YangZL
 * created on 2019/5/21
 * description: 请求数据的接口
 */
public interface IService {

    /**
     * 检查版本
     */
    @POST(Constant.CHECK_VERSION)
    Observable<BaseResponse<UpdateAppBean>> checkVersion();


}

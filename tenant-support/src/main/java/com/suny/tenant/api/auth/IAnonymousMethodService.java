package com.suny.tenant.api.auth;

import java.util.List;

/**
 * 匿名接口
 *
 * @author sunjianrong
 * @date 2021-05-27 20:18
 */
public interface IAnonymousMethodService {

    /**
     * 返回匿名函数列表. 如 com.cie.vending.modules.system.controller.LoginController.getCheckCode
     *
     * @return 匿名访问函数列表
     */
    List<String> anonymousMethodList();
}

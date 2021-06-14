package com.suny.tenant.api.quartz;

/**
 * quartz key生成接口
 *
 * @author sunjianrong
 * @date 2021-06-01 15:13
 */
public interface QuartzKeyProcessor {


    String processJobKey(String originJobKey);

    String processTriggerKey(String originJobKey);

    String processIdentityKey(String originIdentity);

}

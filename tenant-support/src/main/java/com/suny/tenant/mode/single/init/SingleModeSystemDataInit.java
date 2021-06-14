package com.suny.tenant.mode.single.init;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author sunjianrong
 * @date 2021-05-13 11:17
 */
@Component
@Slf4j
public class SingleModeSystemDataInit {


    @PostConstruct
    public void onApplicationEvent() {
    }
}

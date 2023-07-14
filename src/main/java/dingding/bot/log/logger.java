package dingding.bot.log;


import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
public class logger {
    public void StringInfo(Object... objects){
        log.info("=======================");
        for (Object o : objects){
            log.info(String.valueOf(o));
        }
        log.info("=======================");
    }
}

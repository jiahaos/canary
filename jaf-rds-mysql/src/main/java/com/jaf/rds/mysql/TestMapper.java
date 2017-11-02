package com.jaf.rds.mysql;


import java.util.List;
import java.util.Map;

/**
 * Created by jiahao on 2017/4/21.
 */
public interface TestMapper {

    List<Integer> selectTestByParam(Map<String, Object> paraMap);
}

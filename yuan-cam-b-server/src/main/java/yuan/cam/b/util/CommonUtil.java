package yuan.cam.b.util;


import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CommonUtil {

    @SuppressWarnings("unchecked")
    public static <T> List<T> batchCopyProperties(List list, Class clazz){
        if(CollectionUtils.isEmpty(list)){
            return Collections.emptyList();
        }
        List resultList = new ArrayList();
        try {
            Object object;
            for(Object obj : list){
                object = clazz.newInstance();
                BeanUtils.copyProperties(obj, object);
                resultList.add(object);
            }
        } catch (Exception e) {
            log.error("在批量转对象类型时发生异常,list:{},clazz:{},e:{}",
                    JSON.toJSONString(list), JSON.toJSONString(clazz), Throwables.getStackTraceAsString(e));
        }
        return resultList;
    }

}
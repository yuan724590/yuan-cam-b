package yuan.cam.b.service.impl;

import lombok.extern.slf4j.Slf4j;
import yuan.cam.b.service.CommonParent;

@Slf4j
public class CommonParentImpl implements CommonParent {

    @Override
    public void doSomething() {

        System.out.println("疯狂运算");
    }
}
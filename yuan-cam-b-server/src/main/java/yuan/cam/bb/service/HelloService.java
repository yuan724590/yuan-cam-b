package yuan.cam.bb.service;



public interface HelloService {

    String sayHello(String name);

    String sayHelloAgain();

    String sayHelloHystrix();

    String helloA(Object object);

    String rest(String msg);

    /**
     * 启用Fanout
     * @param message
     */
    void helloFanout(String message);

    /**
     * 启用Direct
     * @param message
     */
    void helloDirect(String message);

    /**
     * 启用Topic
     * @param message
     */
    void helloTopic(String message);
}

package com.yu7.order.config;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.yu7.order.pojo.RegistryData;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/control")
public class Control {

    @Resource
    private EurekaClient eurekaClient;


    @Value("${eureka-server.url}")
    private String url;

    @Value("${eureka-server.appName}")
    private String appName;


    /**
     * 通过客户端获取实例的方式并不能实时反应服务状态，这拿的还是三级缓存的数据
     *
     * @return
     */
    @GetMapping("/show-useful")
    public List<InstanceInfo> showUseful() {
        return eurekaClient.getInstancesByVipAddress(appName, false);
    }

    @GetMapping("/show-registry")
    public List<RegistryData> showRegistry() throws IOException {
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(new Request.Builder().url(url + "/" + appName).get().build()).execute();
        String string = response.body().toString();
        String message = response.message();
        log.error(string);
        log.error(message);
        return null;
    }

}

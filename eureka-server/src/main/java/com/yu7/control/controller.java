package com.yu7.control;

import AllUtil.StringUtils.StringChange;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/control")
public class controller {

    @Resource
    EurekaDiscoveryClient eurekaDiscoveryClient;

    @Resource
    EurekaClient eurekaClient;

    @Value("${eureka-server.name}")
    private String eurekaServer;

    private static final HashMap<Integer, String> sourceMap = new HashMap<>();


    /**
     * 下线指定节点下的Eureka服务
     * @return hello
     */
    @GetMapping("/service-down")
    public String shutDown(@RequestParam Integer portParam,@RequestParam String vipAddress) {
        //获取到服务名下的所有服务实例
        List<InstanceInfo> instances = eurekaClient.getInstancesByVipAddress(vipAddress, false);
        //map<端口-实例id>
        instances.forEach(temp -> {
            String instanceId = temp.getInstanceId();
            String appName = temp.getAppName();
            int port = temp.getPort();
            //"http://eureka-server-url/eureka/apps/" + appName + "/" + instanceId;
            sourceMap.put(port, appName +"/"+instanceId);
        });
        //创建请求体
        OkHttpClient client = new OkHttpClient();

        //处理服务信息
        String serviceInfo = sourceMap.get(portParam);
        //创建请求去删除服务
        Request request = new Request.Builder()
                .url("http://"+eurekaServer+"/eureka/apps/" + serviceInfo)
                .delete()
                .build();
        log.debug(request.url().toString());

        try {
            Response response = client.newCall(request).execute();
            if (response.code() == 200) {
                log.debug(serviceInfo+"服务下线成功");
            } else {
                log.debug(serviceInfo+"服务下线失败");
            }
        } catch (IOException e) {
            log.error(e.toString());
        }

        return "hello";
    }
}

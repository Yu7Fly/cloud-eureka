package com.yu7.control;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
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
     * 下线指定节点下的Eureka服务，目前只是清除了Eureka的一级缓存
     * @return hello
     */
    @GetMapping("/service-down")
    public String shutDown(@RequestParam List<Integer> portParams,@RequestParam String vipAddress) {
        List<Integer> successList = new ArrayList<>();
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

        if (ObjectUtils.isEmpty(portParams)){
          return "端口为空"; //todo 完善自定义异常
        }
        log.error("开始时间：{}",System.currentTimeMillis());
        portParams.forEach(temp->{
            //处理服务信息
            String serviceInfo = sourceMap.get(temp);
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
                    successList.add(temp);
                } else {
                    log.debug(serviceInfo+"服务下线失败");
                }
            } catch (IOException e) {
                log.error(e.toString());
            }
        });
        log.error("结束时间：{}",System.currentTimeMillis());
        return "goodbye service"+successList;
    }
}

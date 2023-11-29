package com.yu7.order.pojo;

import com.netflix.loadbalancer.Server;

public class RegistryData extends Server {

    public RegistryData(String host, int port) {
        super(host, port);
    }

    public RegistryData(String scheme, String host, int port) {
        super(scheme, host, port);
    }

    public RegistryData(String id) {
        super(id);
    }
}

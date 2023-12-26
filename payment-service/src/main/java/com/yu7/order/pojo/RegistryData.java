package com.yu7.order.pojo;

import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;

import java.util.List;

public class RegistryData implements ILoadBalancer {


    @Override
    public void addServers(List<Server> newServers) {

    }

    @Override
    public Server chooseServer(Object key) {
        return null;
    }

    @Override
    public void markServerDown(Server server) {

    }

    @Override
    public List<Server> getServerList(boolean availableOnly) {
        return null;
    }

    @Override
    public List<Server> getReachableServers() {
        return null;
    }

    @Override
    public List<Server> getAllServers() {
        return null;
    }
}

package com.atguigu.springcloud.lb;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MyLb implements LoadBalancer{

    private AtomicInteger atomicInteger = new AtomicInteger(0);

    public final int getAndIncrement() {
        int current;
        int next;
        do {
            current = this.atomicInteger.get();
            next = current >= 2147483647 ? 0: current + 1;
        } while (!this.atomicInteger.compareAndSet(current, next));
        System.out.println("第几次访问，次数：next:" + next);
        return next;
    }

    // 负载均衡算法：rest第几次请求 % 服务集群总数 = 实际调用服务的位置下标， 每次重启服务后rest接口计数从0开始
    @Override
    public ServiceInstance instance(List<ServiceInstance> serviceInstances) {
        int index = getAndIncrement() % serviceInstances.size();
        return serviceInstances.get(index);
    }
}

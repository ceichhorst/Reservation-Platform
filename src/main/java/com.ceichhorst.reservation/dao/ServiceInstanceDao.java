package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.service.ServiceInstance;

public class ServiceInstanceDao extends GenericDao<ServiceInstance>{
    public ServiceInstanceDao() {
        super(ServiceInstance.class);
    }
}

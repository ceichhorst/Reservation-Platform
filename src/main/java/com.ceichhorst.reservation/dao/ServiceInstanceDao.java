package com.ceichhorst.reservation.dao;

import com.ceichhorst.reservation.service.ServiceInstance;

public class ServiceInstanceDao extends GenericDaoImpl<ServiceInstance>{
    public ServiceInstanceDao() {
        super(ServiceInstance.class);
    }
}

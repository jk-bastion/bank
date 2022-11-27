package com.bastion.transfer.infrustructure.mapper;

public interface Mapper <T,V> {

    V mapToEntity(T t);

    T mapToData(V r);
}

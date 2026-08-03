package com.ephirious.mapper;

public interface Mapper<T, R> {
    R directMap(T mapped);
    T reverseMap(R mapped);
}

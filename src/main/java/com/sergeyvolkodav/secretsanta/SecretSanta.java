package com.sergeyvolkodav.secretsanta;

import java.util.Map;
import java.util.function.Predicate;

import com.sergeyvolkodav.secretsanta.model.RelationEntity;

public interface SecretSanta<T extends RelationEntity> {

    Map<T, T> getPairs();

    Map<T, T> getPairs(Predicate<T> filter);

}

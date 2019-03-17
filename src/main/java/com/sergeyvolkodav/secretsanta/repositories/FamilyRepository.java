package com.sergeyvolkodav.secretsanta.repositories;

import java.util.List;
import java.util.function.Predicate;

import com.sergeyvolkodav.secretsanta.model.Member;
import com.sergeyvolkodav.secretsanta.model.RelationEntity;
import com.sergeyvolkodav.secretsanta.model.RelationType;

public interface FamilyRepository<T extends RelationEntity> {

    void addMemberPair(T source, T destination, RelationType type);

    List<T> getAllMembers();

    List<T> getMembers(Predicate<Member> filter);

    boolean isFamilyMembers(T source, T destination);
}

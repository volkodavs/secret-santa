package com.sergeyvolkodav.secretsanta.model;

import java.util.List;

public interface RelationEntity {

    void addRelation(Edge edge);

    List<Edge> getRelation();
}

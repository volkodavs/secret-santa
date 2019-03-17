package com.sergeyvolkodav.secretsanta.model;

import java.util.Objects;

public class Edge {

    private final RelationType type;
    private final Member member;

    public Edge(RelationType type, Member member) {
        this.type = type;
        this.member = member;
    }

    public RelationType getType() {
        return type;
    }

    public Member getMember() {
        return member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return Objects.equals(member, edge.member);
    }

    @Override
    public int hashCode() {
        return Objects.hash(member);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "type=" + type +
                ", member=" + member +
                '}';
    }
}

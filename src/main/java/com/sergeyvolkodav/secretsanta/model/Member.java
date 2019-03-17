package com.sergeyvolkodav.secretsanta.model;

import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public class Member implements RelationEntity {

    private final String id;
    private final String name;
    private Integer age;
    private List<Edge> relation;
    private NavigableMap<Integer, String> giftPicksHistory;

    public Member(String id, String name) {
        this.giftPicksHistory = new TreeMap<>();
        this.relation = new LinkedList<>();

        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public NavigableMap<Integer, String> getGiftPicksHistory() {
        return giftPicksHistory;
    }

    @Override
    public List<Edge> getRelation() {
        return relation;
    }

    @Override
    public void addRelation(Edge edge) {
        this.relation.add(edge);
    }

    public void putPicksHistory(Integer year, String familyMemberId) {
        giftPicksHistory.put(year, familyMemberId);
    }

    public boolean isImmediateTo(Member destinationMember) {
        if (destinationMember == null) {
            return false;
        }
        return this.getRelation().stream()
                .anyMatch(edge -> destinationMember.equals(edge.getMember()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", giftPicksHistory=" + giftPicksHistory +
                '}';
    }
}

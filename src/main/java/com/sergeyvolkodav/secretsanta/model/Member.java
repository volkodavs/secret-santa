package com.sergeyvolkodav.secretsanta.model;

import java.util.NavigableMap;
import java.util.Objects;
import java.util.TreeMap;

public class Member {

    private final String id;
    private final String name;
    private Integer age;
    private NavigableMap<Integer, String> giftPicksHistory;

    public Member(String id, String name) {
        this.giftPicksHistory = new TreeMap<>();

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

    public void putPicksHistory(Integer year, String familyMemberId) {
        giftPicksHistory.put(year, familyMemberId);
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

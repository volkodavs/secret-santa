package com.sergeyvolkodav.secretsanta;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Member;
import org.junit.Test;

public class PartOneTest {

    @Test(expected = SecretSantaException.class)
    public void emptyListFailTest() {
        SecretSanta secretSanta = new PartOne(Collections.emptyList());
        secretSanta.getPairs();
    }

    @Test
    public void oddFamilyMembersTest() {
        List<Member> members = new ArrayList<>();
        members.add(new Member(java.util.UUID.randomUUID().toString(), "Mom"));
        members.add(new Member(java.util.UUID.randomUUID().toString(), "Dad"));
        members.add(new Member(java.util.UUID.randomUUID().toString(), "Brother"));

        SecretSanta<Member> secretSanta = new PartOne(members);

        Map<Member, Member> pairs = secretSanta.getPairs();

        checkForDuplicates(members, pairs);
        checkForSelfMatching(pairs);
    }

    @Test
    public void successfulTwoPairsTest() {
        List<Member> members = new ArrayList<>();
        Member mom = new Member(java.util.UUID.randomUUID().toString(), "Mom");
        members.add(mom);
        Member dad = new Member(java.util.UUID.randomUUID().toString(), "Dad");
        members.add(dad);
        SecretSanta<Member> secretSanta = new PartOne(members);

        Map<Member, Member> pairs = secretSanta.getPairs();

        assertThat(pairs.get(mom)).isEqualTo(dad);
        assertThat(pairs.get(dad)).isEqualTo(mom);
    }

    @Test
    public void successfulFourPairsTest() {
        List<Member> members = new ArrayList<>();
        members.add(new Member(java.util.UUID.randomUUID().toString(), "Mom"));
        members.add(new Member(java.util.UUID.randomUUID().toString(), "Dad"));
        members.add(new Member(java.util.UUID.randomUUID().toString(), "Brother"));
        members.add(new Member(java.util.UUID.randomUUID().toString(), "Me"));
        SecretSanta<Member> secretSanta = new PartOne(members);

        Map<Member, Member> pairs = secretSanta.getPairs();

        checkForDuplicates(members, pairs);
        checkForSelfMatching(pairs);
    }

    @Test
    public void successfulMultiplePairsTest() {
        // i'm going generate 42 family members with GUID as a random member name
        List<Member> members = Stream.generate(() -> new Member(java.util.UUID.randomUUID().toString(),
                java.util.UUID.randomUUID().toString())).limit(42)
                .collect(Collectors.toList());

        SecretSanta<Member> secretSanta = new PartOne(members);

        Map<Member, Member> pairs = secretSanta.getPairs();

        checkForDuplicates(members, pairs);
        checkForSelfMatching(pairs);
    }

    private void checkForDuplicates(List<Member> members, Map<Member, Member> pairs) {
        Collection<Member> sidePairs = pairs.values();
        assertThat(new HashSet<>(sidePairs).size()).isEqualTo(sidePairs.size());
        assertThat(pairs.size()).isEqualTo(members.size());
    }


    private void checkForSelfMatching(Map<Member, Member> pairs) {
        Optional<Map.Entry<Member, Member>> selfMatchedMember = pairs.entrySet().stream()
                .filter(member -> member.getKey().equals(member.getValue()))
                .findFirst();
        assertThat(selfMatchedMember.isPresent()).isFalse();
    }
}

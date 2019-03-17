package com.sergeyvolkodav.secretsanta;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Member;
import org.junit.Test;

public class PartTwoTest {

    @Test(expected = SecretSantaException.class)
    public void emptyListFailTest() {
        SecretSanta secretSanta = new PartTwo(Collections.emptyList());
        secretSanta.getPairs();
    }

    @Test
    public void oddFamilyMembersTest() {
        List<Member> members = new ArrayList<>();
        members.add(new Member(UUID.randomUUID().toString(), "Mom"));
        members.add(new Member(UUID.randomUUID().toString(), "Dad"));
        members.add(new Member(UUID.randomUUID().toString(), "Brother"));

        SecretSanta<Member> secretSanta = new PartTwo(members);

        secretSanta.getPairs();
    }

    @Test
    public void successfulTwoPairsTest() {
        List<Member> members = new ArrayList<>();
        Member mom = new Member(UUID.randomUUID().toString(), "Mom");
        members.add(mom);
        Member dad = new Member(UUID.randomUUID().toString(), "Dad");
        members.add(dad);
        SecretSanta<Member> secretSanta = new PartTwo(members);

        Map<Member, Member> pairs = secretSanta.getPairs();

        assertThat(pairs.get(mom)).isEqualTo(dad);
        assertThat(pairs.get(dad)).isEqualTo(mom);
    }

    @Test
    public void twoPairsFormMoreThanThreeYearsTest() {
        List<Member> members = new ArrayList<>();
        Member mom = new Member(UUID.randomUUID().toString(), "Mom");
        members.add(mom);
        Member dad = new Member(UUID.randomUUID().toString(), "Dad");
        members.add(dad);
        PartTwo secretSanta = new PartTwo(members);

        // let's play in 2015
        secretSanta.setCurrentYear(2013);
        Map<Member, Member> pairsFor2013 = secretSanta.getPairs();
        assertThat(pairsFor2013.get(mom)).isEqualTo(dad);
        assertThat(pairsFor2013.get(dad)).isEqualTo(mom);

        // let's play in 2017
        secretSanta.setCurrentYear(2017);
        Map<Member, Member> pairsFor2017 = secretSanta.getPairs();
        assertThat(pairsFor2017.get(mom)).isEqualTo(dad);
        assertThat(pairsFor2017.get(dad)).isEqualTo(mom);
    }

    @Test(expected = SecretSantaException.class)
    public void twoPairsFormLessThanThreeYearsTest() {
        List<Member> members = new ArrayList<>();
        members.add(new Member(UUID.randomUUID().toString(), "Mom"));
        members.add(new Member(UUID.randomUUID().toString(), "Dad"));
        PartTwo secretSanta = new PartTwo(members);

        // let's play in 2016
        secretSanta.setCurrentYear(2016);
        secretSanta.getPairs();

        // let's play in 2017
        secretSanta.setCurrentYear(2017);
        secretSanta.getPairs();
    }

    @Test(expected = SecretSantaException.class)
    public void fourPairsFormLessThanThreeYearsTest() {
        List<Member> members = new ArrayList<>();
        members.add(new Member(UUID.randomUUID().toString(), "Mom"));
        members.add(new Member(UUID.randomUUID().toString(), "Dad"));
        members.add(new Member(UUID.randomUUID().toString(), "Uncle Steve"));
        members.add(new Member(UUID.randomUUID().toString(), "Aunt Jeniffer"));
        PartTwo secretSanta = new PartTwo(members);

        // let's play in 2016
        secretSanta.setCurrentYear(2016);
        secretSanta.getPairs();

        // let's play in 2017
        secretSanta.setCurrentYear(2017);
        secretSanta.getPairs();
        // let's play in 2018
        secretSanta.setCurrentYear(2018);
        secretSanta.getPairs();

        // let's play in 2019, should failed on this stage
        // because we have not enough pairs at this stage
        secretSanta.setCurrentYear(2019);
        secretSanta.getPairs();
    }

    @Test
    public void fourPairsFormMoreThanThreeYearsTest() {
        List<Member> members = new ArrayList<>();
        members.add(new Member(UUID.randomUUID().toString(), "Mom"));
        members.add(new Member(UUID.randomUUID().toString(), "Dad"));
        members.add(new Member(UUID.randomUUID().toString(), "Uncle Steve"));
        members.add(new Member(UUID.randomUUID().toString(), "Aunt Jeniffer"));
        PartTwo secretSanta = new PartTwo(members);

        // let's play in 2016
        secretSanta.setCurrentYear(2016);
        Map<Member, Member> pairs2016 = secretSanta.getPairs();
        checkForDuplicates(members, pairs2016);
        checkForSelfMatching(pairs2016);

        // let's play in 2017
        secretSanta.setCurrentYear(2017);
        Map<Member, Member> pairs2017 = secretSanta.getPairs();
        checkForDuplicates(members, pairs2017);
        checkForSelfMatching(pairs2017);
        // let's play in 2018
        secretSanta.setCurrentYear(2018);
        Map<Member, Member> pairs2018 = secretSanta.getPairs();
        checkForDuplicates(members, pairs2018);
        checkForSelfMatching(pairs2018);

        // let's play in 2020,
        // It should work fine, because we removed from the gift history pairs for 2016
        secretSanta.setCurrentYear(2020);
        Map<Member, Member> pairs2020 = secretSanta.getPairs();
        checkForDuplicates(members, pairs2020);
        checkForSelfMatching(pairs2020);
    }


    @Test
    public void successfulMultiplePairsTest() {
        // i'm going generate 42 family members with GUID as a random member name
        List<Member> members = Stream.generate(() -> new Member(UUID.randomUUID().toString(),
                UUID.randomUUID().toString())).limit(42)
                .collect(Collectors.toList());

        PartTwo secretSanta = new PartTwo(members);

        // this loop should't failed because we're clean up matches every 3 year,
        // so by the time we have a duplicates it will be removed from the gift history
        for (int i = 0; i < 42; i++) {
            secretSanta.setCurrentYear(2000 + i);
            Map<Member, Member> pairs = secretSanta.getPairs();

            checkForDuplicates(members, pairs);
            checkForSelfMatching(pairs);
        }
    }

    @Test
    public void retrievePairWithCustomPredicateTest() {
        List<Member> members = new ArrayList<>();
        Member mom = new Member(UUID.randomUUID().toString(), "Mom");
        mom.setAge(42);
        members.add(mom);
        Member dad = new Member(UUID.randomUUID().toString(), "Dad");
        dad.setAge(52);
        members.add(dad);

        PartTwo secretSanta = new PartTwo(members);

        Map<Member, Member> pairs2016 = secretSanta.getPairs(p -> p.getAge() >= 18);

        checkForDuplicates(members, pairs2016);
        checkForSelfMatching(pairs2016);
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

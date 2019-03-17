package com.sergeyvolkodav.secretsanta;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Member;
import com.sergeyvolkodav.secretsanta.model.RelationType;
import com.sergeyvolkodav.secretsanta.repositories.FamilyCache;
import org.junit.Test;

public class PartThreeTest {

    @Test(expected = SecretSantaException.class)
    public void emptyListFailTest() {
        FamilyCache familyCache = new FamilyCache();
        SecretSanta secretSanta = new PartThree(familyCache);
        secretSanta.getPairs();
    }

    @Test
    public void oddFamilyMembersTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        Member raul = new Member("3", "Raul");

        familyCache.addMemberPair(bob, alice, RelationType.PARENT);
        familyCache.addMemberPair(alice, raul, RelationType.SPOUSE);

        SecretSanta<Member> secretSanta = new PartThree(familyCache);

        Map<Member, Member> pairs = secretSanta.getPairs();

        checkForDuplicates(familyCache.getAllMembers(), pairs);
        checkForSelfMatching(pairs);
    }

    @Test(expected = SecretSantaException.class)
    public void failTwoPairsForNonFamilyTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");

        familyCache.addMemberPair(bob, null, null);
        familyCache.addMemberPair(alice, null, null);

        SecretSanta<Member> secretSanta = new PartThree(familyCache);

        secretSanta.getPairs();
    }

    @Test
    public void twoPairsFormMoreThanThreeYearsTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");

        familyCache.addMemberPair(bob, alice, RelationType.SPOUSE);

        PartThree secretSanta = new PartThree(familyCache);

        // let's play in 2015
        secretSanta.setCurrentYear(2013);
        Map<Member, Member> pairsFor2013 = secretSanta.getPairs();
        assertThat(pairsFor2013.get(bob)).isEqualTo(alice);
        assertThat(pairsFor2013.get(alice)).isEqualTo(bob);

        // let's play in 2017
        secretSanta.setCurrentYear(2017);
        Map<Member, Member> pairsFor2017 = secretSanta.getPairs();
        assertThat(pairsFor2017.get(bob)).isEqualTo(alice);
        assertThat(pairsFor2017.get(alice)).isEqualTo(bob);
    }


    @Test(expected = SecretSantaException.class)
    public void twoPairsFormLessThanThreeYearsTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");

        familyCache.addMemberPair(bob, alice, RelationType.SPOUSE);

        PartThree secretSanta = new PartThree(familyCache);

        // let's play in 2016
        secretSanta.setCurrentYear(2016);
        secretSanta.getPairs();

        // let's play in 2017
        secretSanta.setCurrentYear(2017);
        secretSanta.getPairs();
    }

    @Test(expected = SecretSantaException.class)
    public void fourPairsFormLessThanThreeYearsTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        Member david = new Member("3", "David");
        Member raul = new Member("4", "Raul");

        familyCache.addMemberPair(bob, alice, RelationType.SPOUSE);
        familyCache.addMemberPair(bob, raul, RelationType.PARENT);
        familyCache.addMemberPair(david, raul, RelationType.SIBLING);


        PartThree secretSanta = new PartThree(familyCache);

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
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        Member david = new Member("3", "David");
        Member raul = new Member("4", "Raul");

        familyCache.addMemberPair(bob, alice, RelationType.SPOUSE);
        familyCache.addMemberPair(bob, raul, RelationType.PARENT);
        familyCache.addMemberPair(david, raul, RelationType.SIBLING);


        PartThree secretSanta = new PartThree(familyCache);

        // let's play in 2016
        secretSanta.setCurrentYear(2016);
        Map<Member, Member> pairs2016 = secretSanta.getPairs();
        checkForDuplicates(familyCache.getAllMembers(), pairs2016);
        checkForSelfMatching(pairs2016);

        // let's play in 2017
        secretSanta.setCurrentYear(2017);
        Map<Member, Member> pairs2017 = secretSanta.getPairs();
        checkForDuplicates(familyCache.getAllMembers(), pairs2017);
        checkForSelfMatching(pairs2017);
        // let's play in 2018
        secretSanta.setCurrentYear(2018);
        Map<Member, Member> pairs2018 = secretSanta.getPairs();
        checkForDuplicates(familyCache.getAllMembers(), pairs2018);
        checkForSelfMatching(pairs2018);

        // let's play in 2020,
        // It should work fine, because we removed from the gift history pairs for 2016
        secretSanta.setCurrentYear(2020);
        Map<Member, Member> pairs2020 = secretSanta.getPairs();
        checkForDuplicates(familyCache.getAllMembers(), pairs2020);
        checkForSelfMatching(pairs2020);
    }


    @Test
    public void retrievePairWithCustomPredicateTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        bob.setAge(50);
        Member alice = new Member("2", "Alice");
        alice.setAge(40);
        Member david = new Member("3", "David");
        david.setAge(35);
        Member raul = new Member("4", "Raul");
        raul.setAge(22);

        familyCache.addMemberPair(bob, alice, RelationType.SPOUSE);
        familyCache.addMemberPair(bob, raul, RelationType.PARENT);
        familyCache.addMemberPair(david, raul, RelationType.SIBLING);

        PartThree secretSanta = new PartThree(familyCache);

        Map<Member, Member> pairs2016 = secretSanta.getPairs(p -> p.getAge() >= 18);
        checkForDuplicates(familyCache.getAllMembers(), pairs2016);
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

package com.sergeyvolkodav.secretsanta.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Member;
import com.sergeyvolkodav.secretsanta.model.RelationType;
import org.junit.Test;

public class FamilyCacheTest {

    @Test(expected = SecretSantaException.class)
    public void emptyFamilyAddTest() {
        FamilyRepository familyRepository = new FamilyCache();
        familyRepository.addMemberPair(null, null, null);
    }

    @Test
    public void emptyPairFamilyAddTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        familyCache.addMemberPair(bob, null, null);

        List<Member> members = familyCache.getMembers(null);

        assertThat(members.contains(bob)).isTrue();
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void pairFamilyAddTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");

        familyCache.addMemberPair(bob, alice, RelationType.PARENT);

        List<Member> members = familyCache.getMembers(null);

        assertThat(members.contains(bob)).isTrue();
        assertThat(members.contains(alice)).isTrue();

        assertThat(members.size()).isEqualTo(2);
    }


    @Test
    public void checkForFamilyMemberConnectedTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        Member jon = new Member("3", "Jon");
        Member raul = new Member("4", "Raul");

        familyCache.addMemberPair(bob, alice, RelationType.PARENT);
        familyCache.addMemberPair(jon, raul, RelationType.SIBLING);

        boolean familyMembers = familyCache.isFamilyMembers(bob, alice);
        assertThat(familyMembers).isTrue();
    }

    @Test
    public void checkForFamilyMemberNotConnectedTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        Member jon = new Member("3", "Jon");
        Member raul = new Member("4", "Raul");
        Member david = new Member("5", "David");

        familyCache.addMemberPair(bob, alice, RelationType.PARENT);
        familyCache.addMemberPair(bob, david, RelationType.SIBLING);
        familyCache.addMemberPair(jon, raul, RelationType.SIBLING);

        boolean notFamily = familyCache.isFamilyMembers(bob, raul);
        assertThat(notFamily).isFalse();

    }

    @Test
    public void checkForFamilyMemberConnectedConnectedTest() {
        FamilyCache familyCache = new FamilyCache();
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        Member jon = new Member("3", "Jon");
        Member raul = new Member("4", "Raul");
        Member david = new Member("5", "David");

        familyCache.addMemberPair(bob, alice, RelationType.PARENT);
        familyCache.addMemberPair(bob, david, RelationType.SIBLING);
        familyCache.addMemberPair(alice, raul, RelationType.SPOUSE);
        familyCache.addMemberPair(jon, raul, RelationType.SIBLING);

        boolean notFamily = familyCache.isFamilyMembers(bob, jon);
        assertThat(notFamily).isTrue();
    }
}

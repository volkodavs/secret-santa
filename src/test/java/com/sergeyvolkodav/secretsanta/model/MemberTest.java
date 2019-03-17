package com.sergeyvolkodav.secretsanta.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class MemberTest {

    @Test
    public void isConnectedTest() {
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        bob.addRelation(new Edge(RelationType.PARENT, alice));

        boolean connected = bob.isImmediateTo(alice);

        assertThat(connected).isTrue();
    }

    @Test
    public void isNotConnectedTest() {
        Member bob = new Member("1", "Bob");
        Member alice = new Member("2", "Alice");
        bob.addRelation(new Edge(RelationType.PARENT, alice));

        boolean connected = bob.isImmediateTo(bob);

        assertThat(connected).isFalse();
    }
}

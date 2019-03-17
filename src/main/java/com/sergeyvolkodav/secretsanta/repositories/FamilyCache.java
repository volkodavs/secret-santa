package com.sergeyvolkodav.secretsanta.repositories;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.sergeyvolkodav.secretsanta.exceptions.ErrorCode;
import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Edge;
import com.sergeyvolkodav.secretsanta.model.Member;
import com.sergeyvolkodav.secretsanta.model.RelationType;

public class FamilyCache implements FamilyRepository<Member> {

    private List<Member> members;

    public FamilyCache() {
        members = new ArrayList<>();
    }

    private Member getNode(Member member) {
        if (member == null) {
            return null;
        }

        int memberId = members.indexOf(member);
        if (memberId != -1)
            return members.get(memberId);
        else {
            members.add(member);
            return member;
        }
    }

    @Override
    public void addMemberPair(Member source, Member destination, RelationType type) {
        if (source == null) {
            throw new SecretSantaException("Can't addMemberPair empty member", ErrorCode.INVALID_OPERATION);
        }

        Member sourceMember = getNode(source);
        Member pairedMember = getNode(destination);

        if (pairedMember != null) {
            sourceMember.addRelation(new Edge(type, pairedMember));
            pairedMember.addRelation(new Edge(type.getOppositeRelation(), sourceMember));
        }
    }

    @Override
    public List<Member> getMembers(Predicate<Member> filter) {
        if (Objects.isNull(filter)) {
            return members;
        }
        return members.stream()
                .filter(filter)
                .collect(Collectors.toList());
    }

    @Override
    public List<Member> getAllMembers() {
        return members;
    }

    @Override
    public boolean isFamilyMembers(Member source, Member destination) {
        if (members == null) {
            return false;
        }

        LinkedList<Member> familyMembers = members
                .stream()
                .filter(member -> member.equals(source))
                .flatMap(member -> member.getRelation().stream())
                .map(Edge::getMember)
                .collect(Collectors.toCollection(LinkedList::new));

        Set<Member> visited = new HashSet<>(familyMembers);
        Queue<Member> queue = new LinkedList<>(familyMembers);

        visited.add(source);
        while (queue.size() != 0) {
            Member currentMember = queue.poll();
            if (currentMember.equals(destination)) {
                return true;
            }

            for (Edge edge : currentMember.getRelation()) {
                if (!visited.contains(edge.getMember())) {
                    visited.add(edge.getMember());
                    queue.add(edge.getMember());
                }
            }
        }
        return false;
    }
}

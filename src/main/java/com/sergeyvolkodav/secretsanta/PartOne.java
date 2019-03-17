package com.sergeyvolkodav.secretsanta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;

import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Member;

public class PartOne implements SecretSanta<Member> {

    private List<Member> members;

    public PartOne(List<Member> members) {
        this.members = members;
    }

    @Override
    public Map<Member, Member> getPairs() {
        if (members == null || members.isEmpty()) {
            throw new SecretSantaException("Family members is null or empty");
        }

        int familySize = members.size();

        Map<Member, Member> familyPair = new HashMap<>(familySize);
        int shiftIndex = getShiftIndex(familySize);
        for (int i = 0; i < familySize; i++) {
            int pairMemberIndex = (i + shiftIndex) % familySize;
            familyPair.put(members.get(i), members.get(pairMemberIndex));
        }
        return familyPair;
    }

    @Override
    public Map<Member, Member> getPairs(Predicate<Member> filter) {
        throw new UnsupportedOperationException("Get Santa Secret pairs with predicates does not supported by this algorithm");
    }

    private int getShiftIndex(int familySize) {
        Random random = new Random();
        // generate random in range {min: 1, max: family size}
        return random.nextInt(familySize - 1) + 1;
    }
}

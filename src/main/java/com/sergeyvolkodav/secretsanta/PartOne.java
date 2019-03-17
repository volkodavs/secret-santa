package com.sergeyvolkodav.secretsanta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.sergeyvolkodav.secretsanta.exceptions.ErrorCode;
import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Member;

public class PartOne implements SecretSanta {

    private List<Member> members;

    public PartOne(List<Member> members) {
        this.members = members;
    }

    @Override
    public Map<Member, Member> getPairs() {
        if (members == null || members.isEmpty()) {
            throw new SecretSantaException("Family members is null or empty", ErrorCode.INVALID_OPERATION);
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

    private int getShiftIndex(int familySize) {
        Random random = new Random();
        // generate random in range {min: 1, max: family size}
        return random.nextInt(familySize - 1) + 1;
    }
}

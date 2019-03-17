package com.sergeyvolkodav.secretsanta;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Random;
import java.util.function.Predicate;

import com.sergeyvolkodav.secretsanta.exceptions.ErrorCode;
import com.sergeyvolkodav.secretsanta.exceptions.SecretSantaException;
import com.sergeyvolkodav.secretsanta.model.Member;

public class PartTwo implements SecretSanta<Member> {

    // We want to choose year for calculation
    private int currentYear;
    private List<Member> members;

    public PartTwo(List<Member> members) {
        // if the user does not overwrite a year, we will use the current one
        this.currentYear = Calendar.getInstance().get(Calendar.YEAR);
        this.members = members;
    }

    @Override
    public Map<Member, Member> getPairs() {
        return getPairs(null);
    }

    @Override
    public Map<Member, Member> getPairs(Predicate<Member> filter) {
        checkNotEmptyArgument(members);

        int familySize = members.size();
        Map<Member, Member> familyPair = new HashMap<>(familySize);

        int shiftIndex = getShiftIndex(familySize);
        for (int i = 0; i < familySize; i++) {
            int pairMemberIndex = (i + shiftIndex) % familySize;
            Member member = members.get(i);

            cleanUpOldHistoryGifts(member.getGiftPicksHistory());

            Member pairCandidate = findGiftCandidate(members, pairMemberIndex, member, filter);
            member.putPicksHistory(currentYear, pairCandidate.getId());
            familyPair.put(member, pairCandidate);
        }
        return familyPair;
    }

    private Member findGiftCandidate(List<Member> members,
            int pairMemberIndex, Member member, Predicate<Member> filter) {

        int familySize = members.size();
        int nextElement = 0;

        while (nextElement < familySize) {
            Integer nextSelection = (pairMemberIndex + nextElement) % familySize;
            Member pairCandidate = members.get(nextSelection);

            if (member.equals(pairCandidate)) {
                nextElement++;
                continue;
            }
            if (filter != null && !filter.test(pairCandidate)) {
                nextElement++;
                continue;
            }

            NavigableMap<Integer, String> memberGiftsHistory = member.getGiftPicksHistory();
            if (memberGiftsHistory.isEmpty()) {
                return pairCandidate;
            }

            // check we are not select the same family more than once in the last 3 years
            if (!memberGiftsHistory.containsValue(pairCandidate.getId())) {
                return pairCandidate;
            }
            nextElement++;
        }
        throw new SecretSantaException("Can't find a pair for " + member, ErrorCode.NO_PAIR_FOUND);
    }

    private int getShiftIndex(int familySize) {
        Random random = new Random();
        // generate random in range {min: 1, max: family size}
        // we need this to avoid over loop on the same element
        return random.nextInt(familySize - 1) + 1;
    }

    private void cleanUpOldHistoryGifts(NavigableMap<Integer, String> pickHistory) {
        if (pickHistory.isEmpty()) {
            return;
        }
        if (currentYear - pickHistory.firstEntry().getKey() > 3) {
            pickHistory.remove(pickHistory.firstEntry().getKey());
            cleanUpOldHistoryGifts(pickHistory);
        }
    }

    private void checkNotEmptyArgument(List<Member> members) {
        if (members == null || members.isEmpty()) {
            throw new SecretSantaException("Family members is null or empty", ErrorCode.INVALID_OPERATION);
        }
    }

    void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }
}

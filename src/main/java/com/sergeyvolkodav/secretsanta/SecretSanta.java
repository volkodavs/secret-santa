package com.sergeyvolkodav.secretsanta;

import java.util.Map;

import com.sergeyvolkodav.secretsanta.model.Member;

public interface SecretSanta {

    Map<Member, Member> getPairs();
}

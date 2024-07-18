package com.afis.api.model;

import com.machinezoo.sourceafis.FingerprintTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MatchPair {
    private final FingerprintTemplate probe;
    private final FingerprintTemplate candidate;
}
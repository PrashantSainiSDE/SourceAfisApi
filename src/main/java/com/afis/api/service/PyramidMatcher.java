package com.afis.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.afis.api.model.MatchPair;
import com.machinezoo.sourceafis.FingerprintImage;
import com.machinezoo.sourceafis.FingerprintImageOptions;
import com.machinezoo.sourceafis.FingerprintMatcher;
import com.machinezoo.sourceafis.FingerprintTemplate;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PyramidMatcher {
	private int targetDpi = 0;
	private int stepSizeDpi = 0;
	private int numSteps = 0;

	public PyramidMatcher() {
		this(500,25,7);
	}

	public double verify(byte[] probeBytes, byte[] candidateBytes) {
		List<Integer> dpis = getDotsPerInchList(this.targetDpi, this.stepSizeDpi, this.numSteps);
		
		List<FingerprintTemplate> probes = dpis.stream().map((dpi) -> templateFromBytes(probeBytes, dpi))
				.collect(Collectors.toList());

		List<FingerprintTemplate> candidates = dpis.stream().map((dpi) -> templateFromBytes(candidateBytes, dpi))
				.collect(Collectors.toList());

		List<MatchPair> allCombos = probes.stream().flatMap(p -> candidates.stream().map(c -> new MatchPair(p, c)))
				.collect(Collectors.toList());

		return allCombos.stream().reduce(
				0.0, (currScore, combo) -> Math
						.max(new FingerprintMatcher(combo.getProbe()).match(combo.getCandidate()), currScore),
				Math::max);
	}

	private static List<Integer> getDotsPerInchList(int target, int stepSize, int numSteps) {
		ArrayList<Integer> out = new ArrayList<>();
		for (int i = (target - (numSteps / 2) * stepSize); i <= (target + (numSteps / 2) * stepSize); i += stepSize) {
			out.add(i);
		}

		return out;
	}

	private static FingerprintTemplate templateFromBytes(byte[] data, int dpi) {			
		FingerprintTemplate ft = new FingerprintTemplate(
				new FingerprintImage(data, new FingerprintImageOptions().dpi(dpi)));
		ft.dpi(dpi);

		return ft;
	}
}
package sneps.snip;

import java.util.Set;

import sneps.snip.matching.Substitutions;
import sneps.snebr.Support;

public class Report {

	private Substitutions substitution;
	private Set<Support> supports;
	private boolean sign;
	private int contextID;

	public Report(Substitutions substitution, Set<Support> set, boolean sign, int contextID) {
		this.substitution = substitution;
		this.supports = set;
		this.sign = sign;
		this.contextID = contextID;
	}

	public Substitutions getSubstitutions() {
		return substitution;
	}

	public Set<Support> getSupports() {
		return supports;
	}

	public int getContextID() {
		return contextID;
	}

	@Override
	public boolean equals(Object report) {
		Report castedReport = (Report) report;
		return this.substitution.equals(castedReport.substitution) && this.sign == castedReport.sign
				&& this.contextID == castedReport.contextID;
	}

	public boolean getSign() {
		return sign;
	}

	public boolean isPositive() {
		return sign;
	}

	public boolean isNegative() {
		return !sign;
	}

	public String toString() {
		return "ContextID : " + contextID + "\nSign: " + sign + "\nSubstitution: " + substitution + "\nSupport: " + supports;
	}
}

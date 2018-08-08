package com.zyngl.drools.loans;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.zyngl.drools.junit.BaseDroolsTestCase;
import com.zyngl.loans.facts.Applicant;
import com.zyngl.loans.facts.LoanApplication;
import com.zyngl.loans.facts.Mortgage;
import com.zyngl.loans.util.LoansUtil;

public class AcmeLoansTest extends BaseDroolsTestCase {

	private LoanApplication application;

	public AcmeLoansTest() {
		super("ksession-rules-drl");
	}

	@Before
	public void setUpBaseApplication() {
		application = new LoanApplication();

		application.setApplicant(new Applicant("Brian", "Sam-Bodden", LoansUtil
				.getDateForSimpleFormat("1972-06-21"), true,
				"Integrallis Software, LLC."));

		application.addLender("ACME Mortgage");
		application.addTermInYears("5");
		application.addTermInYears("15");
		application.addTermInYears("25");
		application.addTermInYears("30");
		application.addMortgage("XA-25");
		application.addMortgage("XA-30");
		application.addMortgage("XB-20");
		application.addMortgage("XC-15");
		application.setFicoScore(681);
		application.setPrincipal(50000);
		application.setPropertyValue(75000);
		application.setNumberOfUnits(1);

		application.setLoanPurpose("Purchase");
		application.setPropertyType("Primary Residence");

		for (Mortgage mortgage : application.getMortgagesForApplication()) {
			knowledgeSession.insert(mortgage);
		}
	}

	@Test
	public void testBaseApplication() {
		knowledgeSession.insert(application);
		knowledgeSession.fireAllRules();
		System.out.println(application.getMessages());
		assertTrue(application.getMessages().isEmpty());
	}
}

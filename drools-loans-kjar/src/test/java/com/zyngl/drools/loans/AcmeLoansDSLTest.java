package com.zyngl.drools.loans;

import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.zyngl.drools.junit.BaseDroolsTestCase;
import com.zyngl.loans.facts.Applicant;
import com.zyngl.loans.facts.LoanApplication;
import com.zyngl.loans.facts.MessageCatalog;
import com.zyngl.loans.facts.Mortgage;
import com.zyngl.loans.util.LoansUtil;

//@Ignore
public class AcmeLoansDSLTest extends BaseDroolsTestCase {

	private LoanApplication application;

	public AcmeLoansDSLTest() {
		super("ksession-rules-dslr");
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
	public void testBaseTest() {
		knowledgeSession.insert(application);
		knowledgeSession.fireAllRules();
		assertTrue(application.getMessages().isEmpty());
	}

	@Test
	public void testACMEFico() {
		application.setFicoScore(600);
		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because of low FICO",
				application
						.getMessages()
						.contains(
								MessageCatalog.MSG_ACME_FICO_680));
	}

	@Test
	public void testACMEPrincipal() {
		application.setPrincipal(49950);
		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because of principal is too low",
				application
						.getMessages()
						.contains(
								MessageCatalog.MSG_ACME_PRINCIPAL_50K));
	}

	@Test
	public void testACMENumberOfUnits() {
		application.setNumberOfUnits(5);
		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because the number of units is greater than 5",
				application
						.getMessages()
						.contains(
								MessageCatalog.MSG_ACME_4_UNIT_MAX));
	}

	@Test
	public void testACMEOneUnitPrincipal() {
		application.setNumberOfUnits(1);
		application.setPrincipal(203200);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because the principal for a 1 unit property must be no more than $203,150",
				application
						.getMessages()
						.contains(
								MessageCatalog.MSG_ACME_1_UNIT_PRINCIPAL));
	}

	@Ignore
	@Test
	public void testACMETwoUnitPrincipal() {
		application.setNumberOfUnits(2);
		application.setPrincipal(259900);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because the principal for a 2 unit property must be no more than $259,850",
				application
						.getMessages()
						.contains(
								MessageCatalog.MSG_ACME_2_UNIT_PRINCIPAL));
	}

	@Ignore
	@Test
	public void testACMEThreeUnitPrincipal() {
		application.setNumberOfUnits(3);
		application.setPrincipal(314150);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because " + MessageCatalog.MSG_ACME_3_UNIT_PRINCIPAL,
				application
						.getMessages()
						.contains(MessageCatalog.MSG_ACME_3_UNIT_PRINCIPAL));
	}

	@Ignore
	@Test
	public void testACMEFourUnitPrincipal() {
		application.setNumberOfUnits(4);
		application.setPrincipal(390450);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because " + MessageCatalog.MSG_ACME_4_UNIT_PRINCIPAL,
				application
						.getMessages()
						.contains(MessageCatalog.MSG_ACME_4_UNIT_PRINCIPAL));
	}

	@Ignore
	@Test
	public void testACMEPrincipalMultipleOf50() {
		application.setPrincipal(390401);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because " + MessageCatalog.MSG_ACME_MULT_OF_50_PRINCIPAL,
				application
						.getMessages()
						.contains(MessageCatalog.MSG_ACME_MULT_OF_50_PRINCIPAL));
	}

	@Ignore
	@Test
	public void testACMELoanPurpose() {
		application.setLoanPurpose("Improvement");

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because " + MessageCatalog.MSG_ACME_LOAN_PURPOSE,
				application
						.getMessages()
						.contains(MessageCatalog.MSG_ACME_LOAN_PURPOSE));
	}

	@Ignore
	@Test
	public void testACMEPercentOfProperyPrimaryResidence() {
		application.setPropertyType("Primary Residence");
		application.setPropertyValue(70000);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because lender will not loan more than 70% of the value a primary residence",
				application
						.getMessages()
						.contains(
								"Declined by ACME Mortgage because lender will not loan more than 70% of the value a primary residence"));
	}

	@Ignore
	@Test
	public void testACMEPercentOfProperyInvestmentProperty() {
		application.setPropertyType("Investment Property");
		application.setPropertyValue(51000);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because lender will not loan more than 95% of the value a investment property",
				application
						.getMessages()
						.contains(
								"Declined by ACME Mortgage because lender will not loan more than 95% of the value a investment property"));
	}

	@Ignore
	@Test
	public void testACMEApplicantIsTooYoung() {
		int year = Calendar.getInstance().get(Calendar.YEAR) - 16;
		
		application.getApplicant().setDateOfBirth(
				LoansUtil.getDateForSimpleFormat("" + year + "-06-21"));
		
		application.setNumberOfUnits(1);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because the primary applicant must be older than 18 years of age",
				application
						.getMessages()
						.contains(
								"Declined by ACME Mortgage because the primary applicant must be older than 18 years of age"));
	}

	@Ignore
	@Test
	public void testACMEApplicantCannotBeAnEmployee() {
		application.getApplicant().setEmployer("ACME Mortgage");

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because the primary applicant cannot be an ACME-Loans employee",
				application
						.getMessages()
						.contains(
								"Declined by ACME Mortgage because the primary applicant cannot be an ACME-Loans employee"));
	}

	@Ignore
	@Test
	public void testACMEApplicantMustBeAResident() {
		application.getApplicant().setResident(false);

		knowledgeSession.insert(application);

		knowledgeSession.fireAllRules();

		assertTrue(
				"Application should have been declined because the primary applicant must be a US resident",
				application
						.getMessages()
						.contains(
								"Declined by ACME Mortgage because the primary applicant must be a US resident"));

	}
}

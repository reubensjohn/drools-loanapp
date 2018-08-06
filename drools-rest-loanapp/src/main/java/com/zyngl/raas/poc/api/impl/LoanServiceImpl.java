package com.zyngl.raas.poc.api.impl;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.kie.api.KieServices;
import org.kie.api.builder.KieScanner;
import org.kie.api.builder.ReleaseId;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Component;

import com.zyngl.loans.facts.LoanApplication;
import com.zyngl.raas.poc.api.LoanService;

@Component
public class LoanServiceImpl implements LoanService {

//    @Inject
//    @KReleaseId(groupId = "com.zyngl.raas", artifactId = "drools-user-kjar", version = "1.0-SNAPSHOT")
//    @KSession
    private KieSession kSession;

  //RSJ Modification
    //@Bean(name="dynamic-session")
    //@Bean
	public KieSession getKieSession() {
	   KieServices kieServices = KieServices.Factory.get();
       ReleaseId releaseId = kieServices.newReleaseId("com.zyngl.raas", "drools-loans-kjar", "1.0.0-SNAPSHOT");

       // make sure you use "LATEST" version
       KieContainer kieContainer = kieServices.newKieContainer(releaseId);
       KieSession kSession = kieContainer.newKieSession("ksession-rules-dslr");

       // Poll every 30 seconds
       KieScanner kieScanner = kieServices.newKieScanner(kieContainer);
       kieScanner.start(30000L); //TODO instead of polling may be integrate a event bus and react to a rule change event

	   return kSession;
	}
    
    public LoanServiceImpl() {
    }

    @Override
    public Response loanApplicationStatus(LoanApplication application) {
    	//RSJ Modification
    	kSession = getKieSession();
    	
        System.out.println(">> kSession: " + kSession);
        System.out.println(">> LoanApplication: " + application);
        kSession.insert(application);
        int fired = kSession.fireAllRules();
        System.out.println(">> Fired: " + fired);
        printKieSessionAllFacts(kSession);
        return Response.status(Status.OK).entity(application).build();
    }

    @Override
    public Response getApplications() {
        List<LoanApplication> users = new ArrayList<LoanApplication>();
        for (Object o : kSession.getObjects()) {
            if (o instanceof LoanApplication) {
                users.add((LoanApplication) o);
            }
        }
        return Response.status(Status.OK).entity(users).build();
    }

    private void printKieSessionAllFacts(KieSession kSession) {
        System.out.println(" >> Start - Printing All Facts in the Kie Session");
        for (Object o : kSession.getObjects()) {
            System.out.println(">> Fact: " + o);
        }
        System.out.println(" >> End - Printing All Facts in the Kie Session");
    }

}

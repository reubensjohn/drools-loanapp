/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zyngl.raas.poc.api;

import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.zyngl.loans.facts.LoanApplication;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/v1/loans")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "Loan API - Aprroval or rejected status for a Loan Application", produces = "application/json")
public interface LoanService {
    @POST
    @Path("/application")
	@ApiOperation(		//Swagger Annotation
			value = "Given a Loan application, returns the approval status of the Loan", 
			response = LoanApplication.class)  
	@ApiResponses(value = {		//Swagger Annotation
		@ApiResponse(code = 200, message = "Resource found"),
	    @ApiResponse(code = 404, message = "Resource not found")
	})
    public Response loanApplicationStatus(@NotNull LoanApplication application);
    
        
    @GET
    @Path("")
	@ApiOperation(		//Swagger Annotation
			value = "Get the list of applications with their status", 
			response = LoanApplication.class)  
	@ApiResponses(value = {		//Swagger Annotation
		@ApiResponse(code = 200, message = "Resource found"),
	    @ApiResponse(code = 404, message = "Resource not found")
	})
    public Response getApplications();
}

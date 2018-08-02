[condition][]the lender is "{mortgage_company}"=mortgage:Mortgage(lender:lenderName == "{mortgage_company}",product:mortgageName)
[condition][]and there is an application=application:LoanApplication(lenders contains lender)
[condition][]- with a FICO score below {score}=ficoScore<{score}
[consequence][]reject the application because "{message}"=application.addMessage("Declined by " + lender + " because {message}");
[condition][]- with a Principal that is less than {principal}=principal<{principal}
[condition][]- with a Number of Units greater than {numberOfUnits}=numberOfUnits<{numberOfUnits}
[condition][]- with a Number of Units equal to {numberOfUnits}=numberOfUnits=={numberOfUnits}
[condition][]- with a Principal that is equal or greater than {principal}=principal >= {principal}
[condition][]- with a Principal that is not a multiple of {multiple}=principal % {multiple} != 0

# kiva-exercise
This project is used to solve the Kiva take home exercise. This is a spring boot application using Jersey , Hikaricp , Hibernate.
This project also has some basic usage of Java8 Features as CompletableFuture.This project also contains a standart method of seperating different layers of the server.

## Requirements
1. Java 8. For windows users setup Java https://www.mkyong.com/java/how-to-set-java_home-on-windows-10/
2. Maven. For windows users setup maven https://www.mkyong.com/maven/how-to-install-maven-in-windows/

## Run Project.
1. Build the project using mvn clean install command.
2. Change the configuration of the DB in the application.properties in the src/main/resource folder.
3. Run the SQL form the file DB.sql in the src/main/resource folder.
3. mvn spring-boot:run

# Exemple
```
TO DISPERSE LOAN TO LENDERS
METHOD POST
URL localhost:8080/loans/1681633/lenders/disperse
BODY: 
{
	"installments":7,
	"firstinstallmentDate":"01/10/2019"
}
RESPONSE:
[
    {
        "lenderPaymentId": "745e0abf-cd64-4ca7-81dc-d8c8ab410d65",
        "id": {
            "loanId": "1681633",
            "lenderId": "timothy7934"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547879314,
        "updatedEpoc": 1547879314
    },
    {
        "lenderPaymentId": "15d4e759-4740-4533-9381-9d18ba3900a4",
        "id": {
            "loanId": "1681633",
            "lenderId": "elizabeth6595"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547879314,
        "updatedEpoc": 1547879314
    },
    {
        "lenderPaymentId": "f3698506-6843-4457-b420-9fb9d059cd92",
        "id": {
            "loanId": "1681633",
            "lenderId": "david1820"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547879314,
        "updatedEpoc": 1547879314
    },
    {
        "lenderPaymentId": "01a1f30f-108e-459d-93bb-92addcde5966",
        "id": {
            "loanId": "1681633",
            "lenderId": "andrea9724"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547879314,
        "updatedEpoc": 1547879314
    },
    {
        "lenderPaymentId": "36c0de92-4bab-4c5c-9276-a9e8391c1e15",
        "id": {
            "loanId": "1681633",
            "lenderId": "claudio9326"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547879314,
        "updatedEpoc": 1547879314
    }
]

TO GET SCHEDULE
METHOD: GET
URL: localhost:8080/loans/1681633/lenders/elizabeth6595/schedules
RESPONSE:
[
    {
        "amount": 3.57,
        "paymentScheduledDate": "01/10/2019"
    },
    {
        "amount": 3.57,
        "paymentScheduledDate": "02/10/2019"
    },
    {
        "amount": 3.57,
        "paymentScheduledDate": "03/10/2019"
    },
    {
        "amount": 3.57,
        "paymentScheduledDate": "04/10/2019"
    },
    {
        "amount": 3.57,
        "paymentScheduledDate": "05/10/2019"
    },
    {
        "amount": 3.57,
        "paymentScheduledDate": "06/10/2019"
    },
    {
        "amount": 3.58,
        "paymentScheduledDate": "07/10/2019"
    }
]

TO MAKE PAYMENT
METHOD:POST
URL:localhost:8080/loans/1681633/lenders/elizabeth6595/payment
BODY:
{
	"amount":3.57
	
}
RESPONSE:
{
    "lenderPaymentId": "15d4e759-4740-4533-9381-9d18ba3900a4",
    "id": {
        "loanId": "1681633",
        "lenderId": "elizabeth6595"
    },
    "amount": 25,
    "outstandingAmount": 21.43,
    "installments": 7,
    "outstandingInstallments": 6,
    "firstinstallmentDate": "01/10/2019",
    "createdEpoc": 1547879314,
    "updatedEpoc": 1547879321
}

TO GET PAYMENTS MADE
METHOD: GET
URL:localhost:8080/loans/1681633/lenders/elizabeth6595/payments
RESPONSE:
[
    {
        "paymentId": "b4a6e077-3428-423d-805e-98467cee934d",
        "lenderPaymentId": "15d4e759-4740-4533-9381-9d18ba3900a4",
        "amount": 3.57,
        "createdEpoc": 1547879321
    }
]
```
## Questions and Concerns
https://better-coder.slack.com/

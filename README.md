# kiva-exercise
This project is used to solve the Kiva Take home exercise. This is a spring boot application using Jersey , Hikaricp , Hibernate.
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
        "lenderPaymentId": "4f7a5ae2-1707-43f5-9c36-e9d4d136ff1d",
        "id": {
            "loanId": "1681633",
            "lenderId": "timothy7934"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547878249,
        "updatedEpoc": 1547878249
    },
    {
        "lenderPaymentId": "19cc2fd4-4c4d-4b9b-bdd9-34fb3131014a",
        "id": {
            "loanId": "1681633",
            "lenderId": "elizabeth6595"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547878249,
        "updatedEpoc": 1547878249
    },
    {
        "lenderPaymentId": "90fe1e42-d9e7-47c7-a194-9878545304d0",
        "id": {
            "loanId": "1681633",
            "lenderId": "david1820"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547878249,
        "updatedEpoc": 1547878249
    },
    {
        "lenderPaymentId": "6916e4d0-3206-4bd6-90d2-1eebdadba44f",
        "id": {
            "loanId": "1681633",
            "lenderId": "andrea9724"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547878249,
        "updatedEpoc": 1547878249
    },
    {
        "lenderPaymentId": "f4494807-5f05-407a-bf37-0f8b54179fb5",
        "id": {
            "loanId": "1681633",
            "lenderId": "claudio9326"
        },
        "amount": 25,
        "outstandingAmount": 25,
        "installments": 7,
        "outstandingInstallments": 7,
        "firstinstallmentDate": "01/10/2019",
        "createdEpoc": 1547878249,
        "updatedEpoc": 1547878249
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
    "lenderPaymentId": "19cc2fd4-4c4d-4b9b-bdd9-34fb3131014a",
    "id": {
        "loanId": "1681633",
        "lenderId": "elizabeth6595"
    },
    "amount": 25,
    "outstandingAmount": 21.43,
    "installments": 7,
    "outstandingInstallments": 6,
    "firstinstallmentDate": "01/10/2019",
    "createdEpoc": 1547871569,
    "updatedEpoc": 1547871579
}

TO GET PAYMENTS MADE
METHOD: GET
URL:localhost:8080/loans/1681633/lenders/elizabeth6595/payments
RESPONSE:
[
    {
        "paymentId": "5c66c210-62b3-4c98-94fc-586a322fca1a",
        "lenderPaymentId": "19cc2fd4-4c4d-4b9b-bdd9-34fb3131014a",
        "amount": 3.57,
        "createdEpoc": 1547878294
    }
]
```
## Questions and Concerns
https://better-coder.slack.com/

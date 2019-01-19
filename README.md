# Kiva-exercise
This project is used to solve the Kiva take home exercise. This is a spring boot application using Jersey , Hikaricp , Hibernate, Mockito for Testing , caffeine for caching and MYSql for persistance.
This project also has some basic usage of Java8 Features as CompletableFuture.This project also contains a standart method of seperating different layers of the server.

## Requirements
1. Java 8. For windows users setup Java https://www.mkyong.com/java/how-to-set-java_home-on-windows-10/
2. Maven. For windows users setup maven https://www.mkyong.com/maven/how-to-install-maven-in-windows/

## Run Project.
1. Build the project using mvn clean install command.
2. Change the configuration of the DB in the application.properties in the src/main/resource folder.
3. Run the SQL form the file DB.sql in the src/main/resource folder.
3. mvn spring-boot:run

## How Disperse is calculated
1. Make a rest api request to KIVA API to get loan.
2. Make a rest api request to KIVA API to get Lenders.
3. Calculate amount each lender owns using formulae loan/number of lenders.
4. Insert the Loan Payment for each lender to the DB.

## How Schedule is calculated.(Did not make sense to persist the data in the DB)
1. Take the total amount the lender own /number of installments
2. On the fly calculate the payment dates by adding 1 month each on the first payment date(taking montly payment statergy into consideration);

## How payment is made.
1. Once a request to make the payment it is persisted in the DB.
2. The Loan Payment is updated for its outstandingAmount and outstandingInstallments and persisted in DB. So now we have the updated version.

## How payment is retrived.
1. Based on the lender payment id relation between the LenderPayment Table and the Payment Table we retrive all the payments from the Payment Table.

## Limitations
1. Not all validation has been handeled.
2. While making the payment we do not check against the schedule payment. It is just checked against the outstanding amount and the outstanding installments.
3. The total funded amount is has been distributed equally for all the lenders.(May be different in some amount in decimal)
4. The total amount for the lender has been distributed equally for all the installments.(May be different in some amount in decimal)
5. The Schedule does not take considerations for the holidays.
6. If there is no lender id in the lender response have made the lender id as annonymus.
7. Considering only 50 lenders per loan. Only making one request to get lender not considering paging to get more lenders.
8. Not all the test scenerios has been handled in the tests.

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

TO GET LOAN BY FUNDED STATUS
METHOD:GET
URL localhost:8080/loans
RESPONSE:
{
##TO LARGE TO BE ADDED
}

TO GET LOAN BY ID
METHOD:GET
URL localhost:8080/loans/1681633
RESPONSE:
{
  "loans": [
    {
      "id": 1681633,
      "name": "Grace",
      "description": {
        "languages": [
          "en"
        ],
        "texts": {
          "en": "Grace is a married woman. She has four children. She describes herself to be industrious. She operates a farm where she sells milk from her dairy farm. She has been involved in this business for 10 years. Her business is located in a good area and her primary customers are locals. She describes her biggest business challenge to be inadequate working capital. <br /><br />She will use the KES 11,000 loan to buy another dairy cow. Her business goal is to have a bigger dairy farm within five years. She hopes that in the future, she will be financially stable to provide for her family's financial needs. This is her second loan with SMEP Microfinance Bank. The previous loan was repaid successfully. She will use the anticipated profits to educate her children."
        }
      },
      "status": "funded",
      "funded_amount": 125,
      "image": {
        "id": 3045338,
        "template_id": 1
      },
      "activity": "Dairy",
      "sector": "Agriculture",
      "themes": [
        "Rural Exclusion"
      ],
      "use": "to buy another dairy cow.",
      "location": {
        "country_code": "KE",
        "country": "Kenya",
        "town": "Kitale",
        "geo": {
          "level": "town",
          "pairs": "1.019089 35.002305",
          "type": "point"
        }
      },
      "partner_id": 138,
      "posted_date": "2019-01-17T19:50:04Z",
      "planned_expiration_date": "2019-02-16T19:50:04Z",
      "loan_amount": 125,
      "lender_count": 5,
      "bonus_credit_eligibility": false,
      "tags": [
        {
          "name": "user_favorite"
        },
        {
          "name": "#Animals"
        }
      ],
      "borrowers": [
        {
          "first_name": "Grace",
          "last_name": "",
          "gender": "F",
          "pictured": true
        }
      ],
      "terms": {
        "disbursal_date": "2018-12-27T08:00:00Z",
        "disbursal_currency": "KES",
        "disbursal_amount": 11000,
        "repayment_interval": "Monthly",
        "repayment_term": 8,
        "loan_amount": 125,
        "local_payments": [],
        "scheduled_payments": [],
        "loss_liability": {
          "nonpayment": "lender",
          "currency_exchange": "shared",
          "currency_exchange_coverage_rate": 0.1
        }
      },
      "payments": [],
      "funded_date": "2019-01-17T21:53:54Z",
      "journal_totals": {
        "entries": 0,
        "bulkEntries": 0
      },
      "translator": {
        "byline": "Lauri Fried-Lee",
        "image": 2210835
      }
    }
  ]
}


TO GET LENDER BY LOAN ID
METHOD: GET
URL: localhost:8080/loans/1681633/lenders
RESPONSE:
{
  "paging": {
    "page": 1,
    "total": 5,
    "page_size": 50,
    "pages": 1
  },
  "lenders": [
    {
      "lender_id": "timothy7934",
      "name": "Timothy",
      "image": {
        "id": 726677,
        "template_id": 1
      },
      "whereabouts": "",
      "uid": "timothy7934"
    },
    {
      "lender_id": "elizabeth6595",
      "name": "Elizabeth",
      "image": {
        "id": 1748433,
        "template_id": 1
      },
      "whereabouts": "",
      "uid": "elizabeth6595"
    },
    {
      "lender_id": "david1820",
      "name": "David",
      "image": {
        "id": 947478,
        "template_id": 1
      },
      "whereabouts": "Morphett Vale SA",
      "country_code": "AU",
      "uid": "david1820"
    },
    {
      "lender_id": "andrea9724",
      "name": "Andrea",
      "image": {
        "id": 726677,
        "template_id": 1
      },
      "whereabouts": "",
      "uid": "andrea9724"
    },
    {
      "lender_id": "claudio9326",
      "name": "Claudio Screpanti",
      "image": {
        "id": 839844,
        "template_id": 1
      },
      "whereabouts": "Basel",
      "country_code": "CH",
      "uid": "claudio9326"
    }
  ]
}
```
## Questions and Concerns
https://better-coder.slack.com/

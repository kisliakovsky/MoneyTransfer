# MoneyTransmitter
MoneyTransmitter is an app that provides a simple RESTful API to transfer money between accounts.  


## Usage
```bash
~$ java -jar transmitter.jar
```
By default the app runs on port number 4567. 
```bash
localhost:4567 
```
### Get completed money transfers
**Description:**  
You can call completed transfers. 
API responds with a JSON object with a list of
descriptions of transfers.  
**API call:**
```bash
GET localhost:4567/api/transfers
```
**Example response**  
```bash
{
  "status": "SUCCESS",
  "data": [
    {
      "id": 1,
      "senderAccount": {
        "id": 1,
        "priority": 0
      },
      "receiverAccount": {
        "id": 3,
        "priority": 0
      },
      "sum": 200,
      "timestamp": "Jun 4, 2018 12:28:51 PM"
    }
  ]
}
```
**id** identifier of a transfer (a number of *long* type)
### Get a completed money transfer by its id
**Description:**  
You can call a completed transfer by its id. 
API responds with a JSON object with 
the description of the transfer that match the id.  
**API call:**
```bash
GET localhost:4567/api/transfers/:id
```
**Parameters**  
**id** identifier of a transfer (a number of *long* type)  
**Example request**  
```bash
localhost:4567/api/transfers/1
```
**Example response**  
```bash
{
  "status": "SUCCESS",
  "data": {
    "id": 1,
    "senderAccount": {
      "id": 1,
      "priority": 0
    },
    "receiverAccount": {
      "id": 3,
      "priority": 0
    },
    "sum": 200,
    "timestamp": "Jun 4, 2018 12:28:51 PM"
  }
}
```
### Transfer money from one account to another by their ids
**Description:**  
You can make a money transfer specifying 
a sender account id, a receiver account id and a sum of money (in currency of the sender's account). 
Pay attention that you have to specify parameters of the request 
in its body in JSON format.
API responds with a JSON object with 
the description of the completed transfer.  
**API call:**
```bash
POST localhost:4567/api/transfers/make
```
**Parameters**  
**senderAccountId** identifier of the account of the sender 
**receiverAccountId** identifier of the account of the receiver  
**sum** sum of money to transfer   
**Example request**  
```bash
localhost:4567/api/transfers/make
{
	"senderAccountId": "1",
	"receiverAccountId": "3",
	"sum": "200"
}
```
**Example response**  
```bash
{
  "status": "SUCCESS",
  "data": {
    "id": 2,
    "senderAccount": {
      "id": 1,
      "customer": {
        "id": 1
      },
      "balance": 600,
      "priority": 1,
      "currency": "RUB"
    },
    "receiverAccount": {
      "id": 3,
      "customer": {
        "id": 2
      },
      "balance": 2400,
      "priority": 1,
      "currency": "RUB"
    },
    "sum": 200,
    "timestamp": "Jun 4, 2018 12:40:44 PM"
  }
}
```
### Transfer money from one account to another by linked phone numbers
**Description:**  
You can make a money transfer specifying 
both sender's and receiver's phone numbers and a sum of money (in currency of the sender's account). 
Pay attention that you have to specify parameters of the request 
in its body in JSON format.
API responds with a JSON object with 
the description of the completed transfer.  
**API call:**
```bash
localhost:4567/api/transfers/makebyphone
```
**Parameters**  
**senderPhoneNumber** the phone number of the sender 
**receiverPhoneNumber** the phone number of the receiver  
**sum** sum of money to transfer   
**Example request**  
```bash
localhost:4567/api/transfers/makebyphone
{
	"senderPhoneNumber": "+7911111111",
	"receiverPhoneNumber": "+79055555555",
	"sum": "200"
}
```
**Example response**  
```bash
{
  "status": "SUCCESS",
  "data": {
    "id": 3,
    "senderAccount": {
      "id": 1,
      "customer": {
        "id": 1
      },
      "balance": 400,
      "priority": 1,
      "currency": "RUB"
    },
    "receiverAccount": {
      "id": 4,
      "customer": {
        "id": 3
      },
      "balance": 3200,
      "priority": 1,
      "currency": "RUB"
    },
    "sum": 200,
    "timestamp": "Jun 4, 2018 12:45:40 PM"
  }
}
```
### Errors
When your call fails by some reason you get a JSON object with 
the description of the error.  
**Example of bad request** (wrong phone number of the sender)
```bash
localhost:4567/api/transfers/makebyphone
{
	"senderPhoneNumber": "+7911111111",
	"receiverPhoneNumber": "+79055555555",
	"sum": "200"
}
```
**Example response**  
```bash
{
  "status": "ERROR",
  "message": "Customer not found"
}
```

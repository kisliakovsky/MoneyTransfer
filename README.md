# MoneyTransmitter
MoneyTransmitter is an app that provides a simple RESTful API to transfer money between accounts.  
By default the app runs on port number 4567. 
```bash
localhost:4567 
```
##Get a completed money transfer by its id
**Description:**  
You can call a completed transfer by its id. 
API responds with a JSON object with 
the description of the transfer that match the id.  
**API call:**
```bash
GET localhost:4567/api/transfer/make
```



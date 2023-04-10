# ScentbirdTestTask

Develop and implement an API that allows you to find for a specified list of countries:  The maximum/minimum number of new cases per day during the selected day/period. Retrieve data through the API: https://covid19api.com/

Restrictions:  

It is necessary to minimize the number of requests to the source website during the daily use of the application. 

Use Java 17+, Spring Boot/Micronaut.


--------------------------------------------------------------

This is a 2-microservice solution, where service 1 (ApiGateway) only reads from DB and service 2 (CovidApiService) fetches data from covid api and writes to DB.
Both services share one DB

CovidApiService uses 3 endpoints from covid api:
- /countries          to get a list of countries
- /dayone/country     to get all historic data for one country
- /summary            for everyday data update

Every time user calls /new-cases-stats, application checks whether DB contains data for the countries passed in request. If not, then a gRPC call is sent to fetch data for the country and write to DB. Then it gets max and min new cases for the specified period of time.

This way, the solution makes:
- 1 api call on startup (get countries)
- <= 248 api calls for historic data of each country when requested (for each country data is only fetched once)
- 1 scheduled api call every day to update DB with newest dattta for today

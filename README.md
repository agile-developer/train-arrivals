##### Description:
This program is simple CLI utility that provides 2 functions:
1. Search for Underground (Tube) stations by name
2. Fetch train arrival information for a selected Underground station

Both of the above functions call out to [TfL's Unified API](https://api.tfl.gov.uk).

The results are displayed on the console and a single execution of the program will execute one invocation for each of
the above. To perform a new search, the program needs to be run again.

##### Assumptions
* If no results are found for a station name or arrivals for a station, the program displays this fact and terminates.
* Arrivals are listed in increasing order of arrival time, but the program can be modified to get a different display.
* Error handling is very basic. If the program encounters network or JSON parsing issues, it logs an error and exits.

##### Instructions:
* This project can be run with Java SE 11 or upwards, and maven 3.6.x.
* Run `./mvn clean package` to build the project. 
This will create an executable `train-arrivals.jar` file in the `target` directory.
* To run the application issue the following command:  
`java -jar target/train-arrivals.jar <station name to search, e.g. Portland>`  
* If the station search is successful, it will display a numbered list of matching station names. The user can then enter
the index of the station for which the arrivals need to be retrieved.
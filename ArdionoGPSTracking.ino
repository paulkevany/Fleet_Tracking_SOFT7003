

#define CUSTOM_SETTINGS
#define INCLUDE_GPS_SHIELD
#define INCLUDE_TERMINAL_SHIELD
#define INCLUDE_INTERNET_SHIELD



/* Include 1Sheeld library.*/
#include <OneSheeld.h>

HttpRequest firebaseRequest("https://uber-23725.firebaseio.com/Coordinates.json");


//Create longitude and latitude variables
float longitude; 
float latitude; 
//char latitudeReadings [100]; //Array to store latitudes
//char longitudeReadings [100]; //Array to store longitudes
char buff [100]; //Hold longitude
char buff2 [100]; //Hold latitude
char readings[200];





void setup() 
{
  /* Start communication.*/
  OneSheeld.begin();
  delay(5000);

/* Subscribe to success callback for the request. */
  firebaseRequest.setOnSuccess(&postedSuccess);
  /* Subscribe to response errors. */
  firebaseRequest.getResponse().setOnError(&onResponseError);
  /* Subscribe to Internet errors. */
  Internet.setOnError(&onInternetError);
  
}

void loop()
{

   longitude = GPS.getLongitude();
   latitude = GPS.getLatitude();

   if(longitude == 0|| latitude == 0){

      //Do nothing

   }else{ //Only post data to firebase if real coordinates exits

   /* In order to post the info to firebase we need to convert the coords to a string
   as the .addRawData() method only expects a string parameter*/

 

   //Print Latitude and Longitude values to terminal
 
   delay(10000); //Update GPS every 10 seconds

    dtostrf(longitude,5,5, buff);
    dtostrf(latitude,5,5,buff2);

    strcpy(readings, "{\"longitude\":");
    strcat(readings, buff);
    strcat(readings, ", \"latitude\":");
    strcat(readings, buff2);
    strcat(readings, "}");

    Terminal.println(readings);
    
   
   //strcat(readings, buff2);


    

    Terminal.println(readings);

    //strcat(readings, buff);
    //strcat(readings, buff2);
    //strcpy(readings, buff2);


   
   

    

   //Create a http request

 
   //Append the .json file as the "Table" in your database. e.g(Coordinates "Table" would be coords.json in the request)


//  firebaseRequest.addRawData(buff);
  firebaseRequest.addRawData(readings);
  //firebaseRequest.addRawData(stringRes2);

  firebaseRequest.setOnSuccess(&postedSuccess);

  Internet.performPost(firebaseRequest);
 
  
  //Internet.performPost(firebaseRequest); //Performs the post request

   }
    
  }


  void postedSuccess(HttpResponse response){

  Terminal.println("Success Posting!");
   
  }


  void onResponseError(int errorNumber)
{
  /* Print out error Number.*/
  Terminal.print("Response error:");
  switch(errorNumber) //Switch between errors
  {
    case INDEX_OUT_OF_BOUNDS: Terminal.println("INDEX_OUT_OF_BOUNDS");break;
    case RESPONSE_CAN_NOT_BE_FOUND: Terminal.println("RESPONSE_CAN_NOT_BE_FOUND");break;
    case HEADER_CAN_NOT_BE_FOUND: Terminal.println("HEADER_CAN_NOT_BE_FOUND");break;
    case NO_ENOUGH_BYTES: Terminal.println("NO_ENOUGH_BYTES");break;
    case REQUEST_HAS_NO_RESPONSE: Terminal.println("REQUEST_HAS_NO_RESPONSE");break;
    case SIZE_OF_REQUEST_CAN_NOT_BE_ZERO: Terminal.println("SIZE_OF_REQUEST_CAN_NOT_BE_ZERO");break;
    case UNSUPPORTED_HTTP_ENTITY: Terminal.println("UNSUPPORTED_HTTP_ENTITY");break;
    case JSON_KEYCHAIN_IS_WRONG: Terminal.println("JSON_KEYCHAIN_IS_WRONG");break;
  }
}


void onInternetError(int requestId, int errorNumber)
//If no connection is possible 
{
  /* Print out error Number.*/
  Terminal.print("Request id:");
  Terminal.println(requestId);
  Terminal.print("Internet error:");
  switch(errorNumber)
  {
    case REQUEST_CAN_NOT_BE_FOUND: Terminal.println("REQUEST_CAN_NOT_BE_FOUND");break;
    case NOT_CONNECTED_TO_NETWORK: Terminal.println("NOT_CONNECTED_TO_NETWORK");break;
    case URL_IS_NOT_FOUND: Terminal.println("URL_IS_NOT_FOUND");break;
    case ALREADY_EXECUTING_REQUEST: Terminal.println("ALREADY_EXECUTING_REQUEST");break;
    case URL_IS_WRONG: Terminal.println("URL_IS_WRONG");break;
  }

}



var array = [];

var coordinatesArray = [];
var coordItem1; 
var coordItem2;

var coords = array.concat()
  // Initialize Firebase
  var config = {
    apiKey: "AIzaSyDf9lLQw0A-3HPsZoHn3VdOsn1uwVXJzQk",
    authDomain: "groupproject2018-4452.firebaseapp.com",
    databaseURL: "https://groupproject2018-4452.firebaseio.com",
    projectId: "groupproject2018-4452",
    storageBucket: "groupproject2018-4452.appspot.com",
    messagingSenderId: "854704310539"
  };
  firebase.initializeApp(config);
    //Initialised

/*const dbRef = firebase.database().ref('/Coordinates').limitToLast(1).on('value', function(snapshot){
    var coord = snapshot.val();
    
    console.log("latitude is: ", coord._key)
    
    console.log(snapToArray(snapshot));

});

*/


const dbRef = firebase.database().ref('/Coordinates').limitToLast(1).orderByValue().on("value", function(snapshot){
    
    snapshot.forEach(function(data){
        
      
        data.forEach(function(data){
            
            var item = data.val();
            
            console.log(item);
            
            
            coordItem = item;
            
            array.push(item);            
            
            
            
           
            
            
            
            
        });
        
    });
    
    
    
});






function snapToArray(snapshot){

    
    
    snapshot.forEach(function(childSnapshot){

                     var item = childSnapshot.val();
                    item.key = childSnapshot.key;

    
                    coordinatesArray.push(item);
                     
                     });

       ;
   
        return coordinatesArray;

};


function sendArray(){

   return coordItem1; 
}




function getLatitude(){

    var latitude = array[0];
    
    return latitude+",";

}


function getLongitude(){

    var longitude = array[1];
    
    return longitude;

}



function getCoords(){

    getLongitude();
    getLatitude();


}
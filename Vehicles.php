


<?php

session_start();

require_once('./vendor/autoload.php');


use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;



    $make;
    $model; 
    $registration; 
    $city;

class Vehicles{
    
   

    protected $database;
    protected $dbname = 'vehicles';

    public function __construct(){
        
          if(!isset($_SESSION['user'])){
        
        
        die("You are not logged in!");
              header('Location: logout.php');
    }

        $account = ServiceAccount::fromJsonFile(__DIR__ . '/secret/groupproject2018-4452-047a86027b88.json');
        $firebase = (new Factory)->withServiceAccount($account)->create();
        require('Header.php');
        $this->database = $firebase->getDatabase();

    }

    public function get( $vehicleID = null){

        if(empty($userID) || !isset($userID)){

            return false;
        }

        if($this->database->getReference($this->dbname)->getSnapshot()->hasChild($userID)){
            return $this->database->getReference($this->dbname)->getChild($userID)->getValue();
        }else{
            return false;
        }
        
    }

        
        
     public function getAllVehicles(){

    $dbreference = $this->database->getReference('vehicles'); //Create reference to vehicles
        
        $snapshot = $dbreference->getSnapshot(); //Create an overview of the vehicles table
        
        $value = $snapshot->getValue(); //Set value to the snapshot of the database 
         
         //$value will return an array
        
        
         echo('<table class="table" width="450" style="margin-left: 350%; margin-top: -300%;">');
        echo('<th>Registration</th>');
         echo('<th>Make</th>');
         echo('<th>Model</th>');
         echo('<th>City</th>');
        
         
         foreach($value as $item){ //Loop throught thr array
             
             //Construct a table using the returned data
             echo('<tr><td>');
             
             echo($item['registration']);
             
             echo('<td > &nbsp');
             echo($item['make']); //Get the make field out of the array and display , etc.
             
             echo("<td>");
             echo($item['model']);
             
             echo("<td>");
             echo($item['city']);
             
            
             
         }
         
          
         

         
         
         }
        
        
    
    



    

    public function insert(array $data){

        if(empty($data)|| !isset($data)){
            return false;
        }

        foreach ($data as $key => $value){
            $this->database->getReference()->getChild($this->dbname)->getChild($key)->set($value);
        }

        return true;
    }

        public function delete($userID){

            if(empty($userID) || !isset($userID)){

                if($this->database->getReference($this->dbname)->getSnapshot()->getChild($userID)){
                    $this->database->getReference($this->dbname)->getChild($userID)->remove();
                    return true;
                }else{
                }
            }

        }





}





?>



<html>
    
<script src="https://www.gstatic.com/firebasejs/5.5.8/firebase.js"></script>
    
<script src="app.js"></script>
    

    
<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4" style="margin-top: -55%;">


    
    <div class="" id="jumbotron" style="width: 50%; text-align: center;">
        
        <div class="header clearfix">
   
    <link rel="stylesheet" href="map.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.4/dist/leaflet.css"
   integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
   crossorigin=""/>
    
    <!-- Make sure you put this AFTER Leaflet's CSS -->
 <script src="https://unpkg.com/leaflet@1.3.4/dist/leaflet.js"
   integrity="sha512-nMMmRyTVoLYqjP9hrbed9S+FzjZHW5gY1TWCHA5ckwXZBadntCNs8kEqAWdrb9O7rxbCaA4lKTIWjDXZxflOcA=="
   crossorigin=""></script>

            

        
        <div id="mapid" style="margin-left: 60%; float:right; position: absolute;">
        
        <script>
            
            
            
            
            var mymap = L.map('mapid').setView([52, -8], 8);
            
            L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=sk.eyJ1IjoicGtldmFueSIsImEiOiJjam9lOXBzM3UxZ2cwM3BtcmN1MDZwcnFhIn0.5GgLaGp0BBVIsMGjPk08-g', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets',
    accessToken: 'sk.eyJ1IjoicGtldmFueSIsImEiOiJjam9lOXBzM3UxZ2cwM3BtcmN1MDZwcnFhIn0.5GgLaGp0BBVIsMGjPk08-g'
}).addTo(mymap);

            
                   
                        
           // var marker = L.marker(getCoords([]).addTo(mymap);
        
        </script>
        
        </div>
    
    
        
        <h3 class="text-muted" align="center">Add Vehicles</h3>
       <div class="col-md-5">
    <div class="form-area" style="margin-left: 80%; width:100%;">  
        <form role="form" action="" method="post">
        <br>
                    
                    <div class="form-group">
						<input type="text" class="form-control" id="make" name="make" placeholder="Make" required>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="model" name="model" placeholder="Model" required>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="registration" name="registration" placeholder="Registration Number" required>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="city" name="city" placeholder="City" required>
					</div>
            
        <input type="submit" id="submit" name="submit" class="btn btn-primary pull-right" value="Add Vehicle" style="margin-left: 15%;">
        </form>
        
        
           </div>
           
        
        
        
        
        <div id="googleMap" style="width:200%;height:400px;">
            <script>
                    var template = 'http://c.tiles.mapbox.com/v3/examples.map-szwdot65/{Z}/{X}/{Y}.png';
                    var provider = new MM.TemplatedLayer(template);
                    var map = new MM.Map('map', provider);
                    map.setZoom(20).setCenter([54, -8]); //Get current location and add to here
                
                
    
        
        </script>
        
        
        
           </div>
        
        <?php
        //Check if submit button pressed
        
        if(isset($_POST['submit'])){

                $make =stripslashes($_POST['make']);
                $model =stripslashes($_POST['model']); 
                $registration = stripslashes($_POST['registration']);
                $city = stripslashes($_POST['city']);
            
                $v = new Vehicles();
            
           $message = array(
            
          '"'.$registration.'"'=> array(
                    "make" => $make,
                    "model" => $model,
                    "registration" => $registration,
                    "city" => $city
            
               ));
                      
            
            if($v->insert($message) == true){

                    echo('<script>alert("Successfully added vehicle!");</script>');
            
            }else{
                echo("An error occured!");
            }
            
            
        }
           
           $v1 = new Vehicles();
           
           
          
           echo(  $v1->getAllVehicles());
            
               
           
        
        ?>
        
        
    </div>
</div>
        
        
        
    </div>
            
            </main>  
        
</html>




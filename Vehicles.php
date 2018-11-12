<?php

<<<<<<< Updated upstream
include('Header.php');

$v1 = new Vehicle("Ford","Focus","141-c-13332", 1);
$v2 = new Vehicle("Opel","Corsa","08-D-233", 2);

class Vehicle{


    public $make;
    public $model; 
    public $vehicleReg; 
    public $userID;
    
    
    
    
    public function __construct($vehMake, $vehModel, $regNum, $user){
        
        $this->make = $vehMake;
        $this->model = $vehModel;
        $this->vehicleReg = $regNum;
        $this->userID = $user;

    
    }
    
    
    function setMake($vehMake){

        $this->make = $vehMake;
        
        
    }
    
    function getMake(){

        return $this->make;
        
        
    }
    
    
    
    function setModel($vehModel){

        $this->mdoel = $vehModel;
        
        
    }
    
    function getModel(){

        return $this->model;
        
        
    }
    
    
    function setUser($user){
        
        $this->userID = $user;
        
        
        
    }
    
    
    
    function getUser(){
        
       return $this->userID;
        
        
    }
    
    
    
    function setReg($reg){

    
        $this->vehicleReg = $reg; 
    
    }
    
    
    function getReg(){

        return $this->vehicleReg;
    
    }
 
    

}



=======
require_once('./vendor/autoload.php');
require('Header.php');

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

        $account = ServiceAccount::fromJsonFile(__DIR__ . '/secret/groupproject2018-4452-047a86027b88.json');
        $firebase = (new Factory)->withServiceAccount($account)->create();

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
>>>>>>> Stashed changes






?>



<html>
<<<<<<< Updated upstream

<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

    <div class="container"></div>
    
    <div class="jumbotron" style="width: 50%; text-align: center;">
        
        <div class="header clearfix">
        
        <h3 class="text-muted">Vehicles</h3>
            <hr>
           
      </div>
    
        <table align="center" border="1px">
           <tr>
            <th width="130px">Registration &nbsp;</th>
               
               <th width="130px">Make &nbsp;</th>
               
                <th width="130px">Model &nbsp;</th>
               
            </tr>
            <tr>

            <td><?php 
                echo($v1->getReg());
                echo("<br />");
                echo($v2->getReg());
                echo("<br />");
                
                
                ?></td>
            <td><?php 
                echo($v1->getMake());
                echo("<br />");
                echo($v2->getMake());
                
                
                
                ?></td>
            <td><?php 
                echo($v1->getModel());
                echo("<br />");
                echo($v2->getModel());
                
                
                
                ?></td>
            </tr>
            
            
            
            
        </table>
        
        
        
            
        
        

    
    
    </div>
    
    
    
=======
    <link rel="stylesheet" href="map.css">
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.3.4/dist/leaflet.css"
   integrity="sha512-puBpdR0798OZvTTbP4A8Ix/l+A4dHDD0DGqYW6RQ+9jxkRFclaxxQb/SJAWZfWAkuyeQUytO7+7N4QKrDh+drA=="
   crossorigin=""/>
    
    <!-- Make sure you put this AFTER Leaflet's CSS -->
 <script src="https://unpkg.com/leaflet@1.3.4/dist/leaflet.js"
   integrity="sha512-nMMmRyTVoLYqjP9hrbed9S+FzjZHW5gY1TWCHA5ckwXZBadntCNs8kEqAWdrb9O7rxbCaA4lKTIWjDXZxflOcA=="
   crossorigin=""></script>

<main role="main" style=" width: 50%; margin-top: -50%;margin-left: 20%;">
    
    

    
    <div class="jumbotron" style="align: center;">
        
        <div id="mapid" style="margin-left: 40%; float:right;">
        
        <script>
            var mymap = L.map('mapid').setView([52, -8], 8);
            
            L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=sk.eyJ1IjoicGtldmFueSIsImEiOiJjam9lOXBzM3UxZ2cwM3BtcmN1MDZwcnFhIn0.5GgLaGp0BBVIsMGjPk08-g', {
    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>',
    maxZoom: 18,
    id: 'mapbox.streets',
    accessToken: 'sk.eyJ1IjoicGtldmFueSIsImEiOiJjam9lOXBzM3UxZ2cwM3BtcmN1MDZwcnFhIn0.5GgLaGp0BBVIsMGjPk08-g'
}).addTo(mymap);
            
            
            
            var marker = L.marker([52, -8]).addTo(mymap);
        
        </script>
        
        </div>
        
        <div class="header clearfix"></div>
        
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
        
        
        
        <div id="googleMap" style="width:100%;height:400px;"></div>
            <script>
                    var template = 'http://c.tiles.mapbox.com/v3/examples.map-szwdot65/{Z}/{X}/{Y}.png';
                    var provider = new MM.TemplatedLayer(template);
                    var map = new MM.Map('map', provider);
                    map.setZoom(10).setCenter({ lat: 51.55, lon: 0.1 }); //Get current location and add to here
                
                
    
        
        </script>
        
        
        
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
        
        ?>
        
        
    </div>
</div>
        
        
        
    </div>
            
       
>>>>>>> Stashed changes
    
    
 </main>





</html>




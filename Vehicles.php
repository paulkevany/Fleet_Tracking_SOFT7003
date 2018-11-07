<?php

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









?>



<html>

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
    
    
    
    
    
 </main>





</html>




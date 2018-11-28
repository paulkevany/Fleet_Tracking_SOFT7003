<?php

require_once('./vendor/autoload.php');
//require('Header.php');

use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;




class Orders{
    
    protected $database; 
    protected $dbname = 'orders';
    
    
     public function __construct(){

        $account = ServiceAccount::fromJsonFile(__DIR__ . '/secret/groupproject2018-4452-047a86027b88.json');
        $firebase = (new Factory)->withServiceAccount($account)->create();

        $this->database = $firebase->getDatabase();
         
         return $this->database;

    }



public function getAllOrders(){

    
    $dbreference = $this->database->getReference('orders'); //Create reference to orders
        
        $snapshot = $dbreference->getSnapshot(); //Create an overview of the orders table
        
       
         //$value will return an array
        
        
         echo('<table class="table" width="450" style="margin-left: 0; margin-top: 0;">');
        echo('<th>Registration</th>');
         echo('<th>Make</th>');
         echo('<th>Model</th>');
         echo('<th>City</th>');
    
     $value = $snapshot->getValue(); //Set value to the snapshot of the database 
         
        
         
         foreach($value as $item ){
             
             //Loop throught thr array
             
             //Construct a table using the returned data
             echo('<tr><td>');
             
             //echo($item['']);
             
             echo('<td > &nbsp');
             //echo($item['']); //Get the make field out of the array and display , etc.
             
             echo("<td>");
             //echo($item['']);
             
             echo("<td>");
             //echo($item['']);
             

      }
    
    
}
    
}

    ?>




<html>
    
    <main>

    
    
    
    <?php $order1 = new Orders();
    echo($order1->getAllOrders());
    
    ?>
        
        </main>
    
    



</html>
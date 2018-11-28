



<?php
/**
 * Created by PhpStorm.
 * User: paul
 * Date: 14/10/2018
 * Time: 10:29
 */


session_start();


require_once('./vendor/autoload.php');

use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;


$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/secret/groupproject2018-4452-047a86027b88.json');

$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();




$database = $firebase->getDatabase();


//Login function 


$username;
$password;

function login($username, $password){
    
    if($username == 'admin' && $password == '123'){ //Get data from firebase instead
        
        
        $_SESSION['user'] = $username;

        header('Location: Users.php');
        
    
    }
    
    
    
}



//print_r($database);


?>


<!DOCTYPE html>
<html>
    
    <head>

    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
<script src="//maxcdn.bootstrapcdn.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="//code.jquery.com/jquery-1.11.1.min.js"></script>
<!------ Include the above in your HEAD tag ---------->
        
</head>
    
    <main>

<div class="container">
  
  <div class="row" id="pwd-container">
    <div class="col-md-4"></div>
    
    <div class="col-md-4">
        <br />
        <br />
        
        <br />
        <div class="jumbotron">
      <section class="login-form">
        <form method="post" action="#" role="login">
         
            <div align="center"><p>Admin Login</p>
            </div>
            <br />
          <input type="username" name="username" placeholder="Username" required class="form-control input-lg" />
            
            <br />
           
          <input type="password" name="password" class="form-control input-lg" id="password" placeholder="Password" required="" />
          
            <br />
            <br />
          
          
                        
            <br />
          
          
          <button type="submit" name="go" class="btn btn-lg btn-primary btn-block">Log in</button>
          
          
        </form>
          
          <?php
          
            if(isset($_POST['go'])){

            $username = $_POST['username'];
            $password = $_POST['password'];
                 
            login($username, $password);
            
            
            }
          
          ?>
        
        
      </section>  
        </div>
      </div>
      
      

  </div>
     
  
  
</div>
    
    </main>




</html>



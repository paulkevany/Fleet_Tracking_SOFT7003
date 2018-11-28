<?php
/**
 * Created by PhpStorm.
 * User: paul
 * Date: 14/10/2018
 * Time: 10:57
 */

//AKA Customers
session_start();
require_once('./vendor/autoload.php');
use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;

$name;
$email;
$username;
$city;
$approved;



class Users{

    
protected $database;
protected $dbname = 'users';
    
    
    
    
    
    
    public function __construct(){
    
        
        if(!isset($_SESSION['user'])){
        
        $error = "<html><head><p>You are not logged in!</p></head></html>";
        die($error);
    }

        $account = ServiceAccount::fromJsonFile(__DIR__ . '/secret/groupproject2018-4452-047a86027b88.json');
        $firebase = (new Factory)->withServiceAccount($account)->create();
        require('Header.php');

        $this->database = $firebase->getDatabase();
        
    

    }

    
    public function getAllUsers(){

    
        $reference = $this->database->getReference('users');
        
        $snapshot = $reference->getSnapshot();
        
        $value = $snapshot->getValue();
        
        return $value;
    
    }
    
    
    
    public function get( $userID = null){

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

//Create users
$user1 = new Users();

?>

<html>
<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">


    <div class="jumbotron" style="width: 50%; text-align: center; margin-top: -70%;">
        
                <div class="header clearfix"></div>
        
        <h3 class="text-muted" align="center">Add Users</h3>
       <div class="col-md-5">
    <div class="form-area" style="margin-left: 80%; width:100%;">  
        <form role="form" action="" method="post">
        <br>
                    
                    <div class="form-group">
						<input type="text" class="form-control" id="name" name="name" placeholder="Name" required>
					</div>
					<div class="form-group">
						<input type="email" class="form-control" id="email" name="email" placeholder="Email" required>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="username" name="username" placeholder="Username" required>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="city" name="city" placeholder="City" required>
					</div>
            
        <input type="submit" id="submit" name="submit" class="btn btn-primary pull-right" value="Add User" style="margin-left: 15%;">
        </form>
        
        
        
    
    <?php 
        
        
        if(isset($_POST['submit'])){

        //Secure input
            
        
            
            $name = stripslashes($_POST['name']);
            $email = stripslashes($_POST['email']);
            $username = stripslashes($_POST['username']);
            $city = stripslashes($_POST['city']);
            
            
            $name = htmlspecialchars($name);
            
            $username= htmlspecialchars($username);
            $city = htmlspecialchars($city);
            
            $userdata = array(
            
            
                '"'.$username.'"' => array(

                   'Name' => $name,
                    'Email' => $email,
                    'Username' => $username,
                    'City' => $city,
                    'Approved' => 0
                
              ) );
            
            if($user1->insert($userdata) == true){

              echo('<script>alert("Successfully added User!")</script>');
            }else{

                 echo('<script>alert("An error occured adding user!");</script>');
            
            }

                        

        
        }
        
    
       
    
    
    ?>
    
           </div>
        </div>
    </div>
    
    
<?php
    
    
    
    var_dump($user1->getAllUsers());
    
    
    ?>
    
    

    </main>
    

</html>

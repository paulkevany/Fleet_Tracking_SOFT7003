<?php
/**
 * Created by PhpStorm.
 * User: paul
 * Date: 14/10/2018
 * Time: 10:57
 */

//AKA Customers

require_once('./vendor/autoload.php');
require('Header.php');

use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;

class Users{

    protected $database;
    protected $dbname = 'users';

    public function __construct(){

        $account = ServiceAccount::fromJsonFile(__DIR__ . '/secret/groupproject2018-4452-047a86027b88.json');
        $firebase = (new Factory)->withServiceAccount($account)->create();

        $this->database = $firebase->getDatabase();

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

//In future will use forms and pass that data in instead

$user1 = new Users();
$user2 = new Users();


?>

<html>
<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4">

    <div class="container">
    
    <div class="jumbotron" style="width: 50%; text-align: center;">
    
    <?php 
        ($user1->insert([

    'ID' => '1',
    'Name', 'Mary K',
    'Comment' => '',

]));
        
        
        //Create second user
        
        ($user2->insert([

    'ID' => '2',
    'Name', 'John B',
    'Comment' => 'Hi',

]));
        
        //Get the user from database
        
        echo($user1->get(1));
        echo("<br />");
        echo($user2->get(2));
    
    ?>
        
    </div>
    </div>
    </main>
    

</html>


<?php

//var_dump($users->delete(7));

//var_dump($users->delete(1));

?>
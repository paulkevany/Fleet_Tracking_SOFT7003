<?php
/**
 * Created by PhpStorm.
 * User: paul
 * Date: 14/10/2018
 * Time: 10:57
 */

require_once('./vendor/autoload.php');

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

$users = new Users();


//var_dump($users->insert([

  //  'ID' => '1',
   // 'Name', 'Mary',
    //'Comment' => 'I like Trains',

//]));

//var_dump($users->delete(7));

var_dump($users->delete(2));
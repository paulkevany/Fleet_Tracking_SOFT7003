<?php
session_start();

$l = new logout();

class logout{
    


function __construct(){

    
    session_unset();
    
    echo("Please wait to be redirected..");
    header('refresh:2;url=index.php');
    





}



}




?>
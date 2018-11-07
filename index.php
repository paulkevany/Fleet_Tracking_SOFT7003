



<?php
/**
 * Created by PhpStorm.
 * User: paul
 * Date: 14/10/2018
 * Time: 10:29
 */

require('Header.php');

require_once('./vendor/autoload.php');

use Kreait\Firebase\Factory;
use Kreait\Firebase\ServiceAccount;


$serviceAccount = ServiceAccount::fromJsonFile(__DIR__.'/secret/groupproject2018-4452-047a86027b88.json');

$firebase = (new Factory)
    ->withServiceAccount($serviceAccount)
    ->create();




$database = $firebase->getDatabase();

print_r($database);


?>


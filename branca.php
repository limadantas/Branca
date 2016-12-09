<?php
$servername = "localhost";
$username = "root";
$password = "root";

try {
    $conn = new PDO("mysql:host=$servername;dbname=branca_chat", $username, $password);
    // set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    //echo "Connected successfully"; 

    if (isset($_GET["q"])) {
    	$q = $_GET["q"];
    	//die("SELECT * FROM grupos LIKE '".$q."%' ");
/*$query = $database->prepare('SELECT * FROM table WHERE column LIKE ?');
$query->execute(array('value%'));

while ($results = $query->fetch())
{
    echo $results['column'];
}*/


    	$stmt = $conn->prepare('SELECT * FROM grupos WHERE id LIKE ? '); 
    	$stmt->execute(array($q."%"));
    	$results=$stmt->fetchAll(PDO::FETCH_ASSOC);
		$json=json_encode($results);

		echo $json;
    }

    }
catch(PDOException $e)
    {
    die("Connection failed: " . $e->getMessage());
    }
?>
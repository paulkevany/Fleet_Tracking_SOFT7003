<?php
require('Header.php');

?>


<html>
	<head>
	
	<script src="https://www.gstatic.com/firebasejs/5.7.0/firebase.js"></script>
		
		<script>
					// Initialize Firebase
					var config = {
    				apiKey: "AIzaSyB9bReOizDs7NGcKKuswCylGZx-nhNXt3Y",
    				authDomain: "uber-23725.firebaseapp.com",
    				databaseURL: "https://uber-23725.firebaseio.com",
    				projectId: "uber-23725",
    				storageBucket: "uber-23725.appspot.com",
    				messagingSenderId: "905617104551"
  				};
  				firebase.initializeApp(config);
			
			var database = firebase.database();
			
			firebase.auth().onAuthStateChanged(user =>{
						
						
						if(user){
							
						
						}else{
							document.write("You need to be signed in to access this page!");
							window.location = "index.php";
							
						}
					});
			
		</script>
		
		
		
	</head>
    


<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4" style="margin-top: -55%;">


	
	
	
	
	<script>
	var totalKM=0;
	var i;
	var values = [];
	var	allTimeRev = 0;
	
	var ordersRef = firebase.database().ref("History").limitToLast(21);
	
		ordersRef.on('value', function(snapshot){
  
			
			snapshot.forEach(function(childSnapshot){
				
				
				var item = childSnapshot.val();
				item.key = childSnapshot.key;
			
				
				allTimeRev = allTimeRev+item.price; //Add all the prices up
				
			
				
				
				
			});
				
				price = parseFloat(Math.round(allTimeRev * 100)/100).toFixed(2);
			
				console.log(allTimeRev);
			document.getElementById('allTimeRev').innerHTML = "€" + price;
		
			previous5Trips();
			totalKMTravelled();
			calculateAverages();
		});
			
		
		
	function previous5Trips(){
		
		var last5=0;
		
		var ordersRef = firebase.database().ref("History").limitToLast(5);
	
		ordersRef.on('value', function(snapshot){
  
			
			snapshot.forEach(function(childSnapshot){
				
				
				var item = childSnapshot.val();
				item.key = childSnapshot.key;
			
				
				last5 = last5+item.price; //Add all the prices up
				
			
				
				
				
			});
				
				//price = parseFloat(Math.round(allTimeRev * 100)/100).toFixed(2);
			
				console.log(last5);
			document.getElementById('5TripRev').innerHTML = "€" + last5;
		
			
		});
		
		
		
	}
		

		
			function totalKMTravelled(){
				
				
					
		
		var ordersRef = firebase.database().ref("History").limitToLast(21);
	
		ordersRef.on('value', function(snapshot){
  
			
			snapshot.forEach(function(childSnapshot){
				
				
				var item = childSnapshot.val();
				item.key = childSnapshot.key;
			
				
				totalKM = totalKM+item.distance; //Add all the prices up
				
				
				
			
				
				
				
			});
				
				//price = parseFloat(Math.round(allTimeRev * 100)/100).toFixed(2);
			
				console.log(totalKM);
			document.getElementById('totalDistance').innerHTML = totalKM + "KM";
		
			
		});
				
		
				
			}
		
		
	function calculateAverages(){
		
		
		var avgCost = allTimeRev/21;
		
		var costAvg = parseFloat(Math.round(avgCost * 100)/100).toFixed(2);
		
		document.getElementById('avgDistance').innerHTML = "€" + costAvg;

		var avgDistance = totalKM/21;
		
		var roundDist = parseFloat(Math.round(avgDistance * 100)/100).toFixed(2);
		
		document.getElementById('avgPerKM').innerHTML = roundDist + "KM";
		
		avgCostKM = costAvg/avgDistance;
		
		var avgKM = parseFloat(Math.round(avgCostKM * 100)/100).toFixed(2);
		
		document.getElementById('avgCostPerKM').innerHTML = "€" + avgKM;
	
	
	
	}
		
		
			
		
		
				
	</script>
	
	
	<table id="dataTable"  class="table table-dark"style="margin-top: -20%;">
  <thead>
    
     
      <th scope="col" id="col1">All-Time Revenue</th>
      <th scope="col" id"col2">Previous 5 Trips</th>
      <th scope="col" id="col3">Total Distance To-Date</th>
      <th scope="col" id="col4">Average Cost Per Trip</th>
      <th scope="col" id="col5">Average Distance Per Trip</th>
      <th scope="col" id="col6">Average Cost Per KM</th>
     
	  <tr>
	  <td id="allTimeRev"></td>
		  <td id="5TripRev"></td>
		    <td id="totalDistance"></td>
		   <td id="avgDistance"></td>
		   <td id="avgPerKM"></td>
		   <td id="avgCostPerKM"></td>
	  </tr>

	
	
	

		
	
	</main>
        
    
        

</html>



        
    




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
		
		
		<?php require("header.php") ?>
	</head>
    


<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4" style="margin-top: -55%;">


	
	
	
	
	<script>
	
	var i;
	var values = [];
	
	var ordersRef = firebase.database().ref("Vehicle").limitToLast(3);
	
		ordersRef.on('value', function(snapshot){
  
			
			snapshot.forEach(function(childSnapshot){
				
				
				var item = childSnapshot.val();
				item.key = childSnapshot.key;
				
				values.push(item);
				
				
			
				
				for(i=0;i<values.length;i++){
					
				document.getElementById("Dest"+i).innerHTML = values[i].userRequest.destination;
				document.getElementById("Dist"+i).innerHTML = values[i].userRequest.distance;
				document.getElementById("user"+i).innerHTML = values[i].userRequest.userID;
				document.getElementById("orderID"+i).innerHTML = values[i].key;
				
					
					
					
					
				
					
					
					
					
					
				}
			
			
			
				
			
			
				
				
			
				
				document.getElementById("approve0").addEventListener("click", function(){
				
				
					//Update the approved value in firebase 
					
						document.getElementById("approve0").disabled = true;
					document.getElementById("approve0").style.backgroundColor = "gray";
					
					var approveRef = firebase.database().ref("Vehicle/"+values[0].key +"/userRequest").update({
						
						
						
							approved: 1
						
						
					});
				
				
				
			});
				
				
					document.getElementById("approve1").addEventListener("click", function(){
				
				
					//Update the approved value in firebase 
					
						document.getElementById("approve1").disabled = true;
					document.getElementById("approve1").style.backgroundColor = "gray";
					
					var approveRef = firebase.database().ref("Vehicle/"+values[1].key +"/userRequest").update({
						
						
						
							approved: 1
						
						
					});
				
				
				
			});
				
				
					document.getElementById("approve2").addEventListener("click", function(){
				
				
					//Update the approved value in firebase 
					
						document.getElementById("approve2").disabled = true;
					document.getElementById("approve2").style.backgroundColor = "gray";
					
					var approveRef = firebase.database().ref("Vehicle/"+values[2].key +"/userRequest").update({
						
						
						
							approved: 1
						
						
					});
				
				
				
			});
    
				
			
				
			});

		});
			
	
				
				//Approve buttons 
			
				var approve1 = document.getElementById("approve1");
				var approve2 = document.getElementById("approve2");
				
				
		

	
		
		
	
	
	
	</script>
	
	
	
	
	
	
	
	
 <table id="dataTable"  class="table table-dark"style="margin-top: -20%;">
  <thead>
    
     
      <th scope="col" id="col1">Destination</th>
      <th scope="col" id"col2">Distance</th>
      <th scope="col" id="col3">UserID</th>
      <th scope="col" id="col4">OrderID</th>
      <th scope="col" id="col5">Options</th>
	  
	  <tr>
	  <td id="Dest0"></td>
		<td id="Dist0"></td>
		<td id="user0"></td>
		  <td id="orderID0"></td>
		<td id="option0"><button id="approve0"class="btn-primary pull-right">Approve</button></td>
	  </tr>

	   <tr>
	  <td id="Dest1"></td>
		<td id="Dist1"></td>
		<td id="user1"></td>
		   <td id="orderID1"></td>
		<td id="option1"><button id="approve1" class="btn-primary pull-right">Approve</button></td>
	  </tr>
	  
	   <tr>
	  <td id="Dest2"></td>
		<td id="Dist2"></td>
		   <td id="user2"></td>
		   <td id="orderID2"></td>
		   <td id="option2"><button id="approve2" class="btn-primary pull-right">Approve</button></td>
	  </tr>
	  
	   
	  
	  
    
  </thead>
  
	</table>
		
	
	</main>
        
    
        

</html>



        
    



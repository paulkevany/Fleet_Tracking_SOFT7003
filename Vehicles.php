
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
		
		
		<?php require("header.php");
		?>
	</head>
    


<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4" style="margin-top:-55%;">

	
		
   
	
       
    
        
        
       <div class="col-md-5" style="margin-top: -20%;">
    	
		   <div class="form-area" style="margin-left: 50%; width:100%;">  
			   <h3 class="text-muted" align="center">Add Vehicles</h3>
        
        <br>
                    
                    <div class="form-group">
						<input type="text" class="form-control" id="make" name="make" placeholder="Make" required>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="model" required name="model" placeholder="Model" >
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="registration" name="registration" placeholder="Registration Number" required>
					</div>
			   <div class="form-group">
						<input type="text" class="form-control" id="location" name="location" placeholder="Location" required>
					</div>
			   <div class="form-group">
				   <select class="form-control form-control-md" id="serviceOptions">
  					<option>UberX</option>
  					<option>UberXL</option>
  					<option>Uber Black</option>
				   	</select>
						
				   
			   </div>
				   		<!-- Default checked -->

			   
					
             
			<button type="submit" id="submit" onclick="submitVehicle();" name="submit" class="btn btn-primary pull-right" value="Add Vehicle" style="width: 100%;">Add Vehicle</button>
		
       
			<script>
			
				function submitVehicle(){
					var make = document.getElementById("make").value;
			 		var model = document.getElementById("model").value;
					
					var reg = document.getElementById("registration").value;
					
					var location = document.getElementById("location").value;
					
					//Get selected radio button
					
					var serviceOption = document.getElementById("serviceOptions");
					var value = serviceOption[serviceOption.selectedIndex].value;
					
				
					var obj = {
						
						make: make, 
						model: model,
						registration: reg,
						location: location,
						service: value
					}
					
					firebase.database().ref('Vehicle').push(obj, function(error){
						
						if(error){
							
							alert("Couldn't post New Vehicle");
						}else{
							
							
							alert("Vehicle Added!");
							
							this.make.value = "";
							this.model.value = "";
							this.registration.value = "";
							this.location.value = "";
						}
						
					});
					
					
					
					
				}
					
					
					
			
			   </script>
      			   
			   
			  
			   
        
			    
        
           </div>
           
        
	</div>
	
	<br />
	<br />
	
	
	
	
	
	
	</main>
        
	
    
        

</html>



        
    



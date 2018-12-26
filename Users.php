
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


   
        
       
    
        
        
       <div class="col-md-5" style="margin-top: -20%;">
    	
		   <div class="form-area" style="margin-left: 50%; width:100%;">  
			   <h3 class="text-muted" align="center">Add User</h3>
        
        <br>
                    
                    <div class="form-group">
						<input type="text" class="form-control" id="emailForm" name="email" placeholder="Email" required>
					</div>
					<div class="form-group">
						<input type="password" class="form-control" id="passwordForm" required name="password" placeholder="Password" >
			   </div>
             
			<button type="submit" id="addUserBtn" onclick="addUser();" name="addUserBtn" class="btn btn-primary pull-right" value="Add User" style="width: 100%;">Add User</button>
		
       
			<script>
			
				function addUser(){
					
					var email = document.getElementById("emailForm").value;
					var password = document.getElementById("passwordForm").value;
					
					if(email.length < 4){
						alert("Email must be at least 4 characters!");
						return;
					}
					
					if(password.length < 8){
						alert("Password must be at least 8 characters!");
						return;
						
					}
					
					firebase.auth().createUserWithEmailAndPassword(email, password).catch(function(error) {
  						// Handle Errors here.
  					var errorCode = error.code;
  					var errorMessage = error.message;
					
						if(error){
						alert(errorMessage);
						return;
							
						}else{
							
							alert("Successful!");
							email.value = "";
							password.value = "";
							return;
						}
				
					});
					
					
					
					
				}
					
					
					
			
			   </script>
      			   
			    
			  
			   
        
			    
        
           </div>
           
        
	</div>
	
	</main>
        
    
        

</html>



        
    



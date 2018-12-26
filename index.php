
<!DOCTYPE html>
<html>
    
    <head>
<!-- Start of user authentication --> 
	
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
			
			firebase.auth().onAuthStateChanged(user =>{
						
						
						if(user){
							
							window.location = "vehicles.php";
								
						}else{
							
							alert("You are not logged in!");
						}
					});
			
		</script>
		<!-- End of user authentication --> 
		
    

<!------ Include the above in your HEAD tag ---------->
        
		
		<link href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.0/css/bootstrap.min.css" rel="stylesheet" id="bootstrap-css">
</head>
	
    
    <main>

<div class="container">
  
  <div class="row" id="pwd-container">
    <div class="col-md-4"></div>
    
    <div class="col-md-4">
        <br />
        <br />
        
        <br />
        <div class="jumbotron">
      <section class="login-form">
  
         
			<div align="center"><h2>Admin Login</h2>
            </div>
            <br />
          <input type="text" name="email" id="email" placeholder="Email" required class="form-control input-lg" />
            
            <br />
           
          <input type="password" name="password" class="form-control input-lg" id="password" placeholder="Password" required />
          
            <br />
            <br />

          
          
                        
            <br />
          
          
          <button type="submit" id="button" onclick="signIn();" name="go" class="btn btn-lg btn-primary btn-block">Log in</button>
			
			
			<script>
			
				function signIn(){

					var email = document.getElementById("email").value;
			 		var password = document.getElementById("password").value;
					
					if(email.length < 4){
						
						alert("Please enter an email greater than 4 characters!");
						return;
					}
					
					if(password.length < 4){
						
						alert("Please enter a password greater than 4 characters!");
						return;
						
					}
					
					
					
					
					firebase.auth().signInWithEmailAndPassword(email, password).catch(function(error) {
  					// Handle Errors here.
  					var errorCode = error.code;
  					var errorMessage = error.message;
						
						alert(errorMessage);
						return;
  					// 
					});
					
				}
			</script>
          
          
		  
        
        
      </section>  
        </div>
      </div>
      
      

  </div>
     
  
  
</div>
    
    </main>




</html>


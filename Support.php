
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
    


<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4" style="margin-right: 20%;margin-top: -65%;">


   
	<script>
			
				function getSupportRequests(){
					
					
					var supportRef = firebase.database().ref('SupportRequests').limitToFirst(1);
					
					supportRef.on('value', snapshot=>{
						
						snapshot.forEach(snap=>{
							
							
						var email = snap.child("email").val();
						var message = snap.child("message").val();
						var subject = snap.child("subject").val();
							
							console.log(email, subject, message);
							
							var emailText= document.getElementById("emailP");
							
							emailText.innerHTML = email;
							
							
							var subjectText= document.getElementById("subjectP");
							
							subjectText.innerHTML = subject;
							
							
							var messageText= document.getElementById("messageP");
							
							messageText.innerHTML = message;
						
							
							//Populate fields is the reply button 
							
							
							var replyButton = document.getElementById("populateFields").addEventListener("click", function(){
								
							//Populate fields in here
								
								
								var emailForm = document.getElementById("emailForm").value = email;
								var subjectForm = document.getElementById("subjectForm").value = "Re: "+ subject;
								
								
								
								
								
								
								
							});
							
						/*	var sendButton = document.getElementById("sendButton").addEventListener("click", function(){
								
							// If send button pressed, do form verification and send email through php
								
								
								var messageForm = document.getElementById("messageTextarea").value;
								var subjectForm = document.getElementById("subjectForm").value;
								var emailForm = document.getElementById("emailForm").value;
								
								if(messageForm.length < 4){
									
									alert("Message Field Cannot be less than 4 characters!");
									return;
								}else
								
								if(subjectForm.length < 4){
									
									alert("Subject Field Cannot be less than 4 characters!");
									return;
								}else
								
								if(emailForm.length < 4){
									
									alert("Email Field Cannot be less than 4 characters!");
									return;
							
									
									
									
								*/	
								
									
									//Send the customer an email with mailgun API 
									
									/*var script = document.createElement('script');
									script.src = 'API/mailgun.js';
									document.head.appendChild(script);

									
									
									var mg = script.client({username: 'api', key:'9be22244e8f290b240ffa9c785d93e45-b3780ee5-69d1ad4c'});
									
									
									mg.messages.create('https://app.mailgun.com/app/domains/sandboxdb048da356f24c1f8d9e120d2164c1dd.mailgun.org', {
    								from: "Excited User <mailgun@sandbox-123.mailgun.org>",
    								to: ["paul.kevany@mycit.ie"],
    								subject: "Hello",
    								text: "Testing some Mailgun awesomness!",
    								html: "<h1>Testing some Mailgun awesomness!</h1>"
  										}).catch(err => console.log(err)); // logs any error
									
									
							
								}
								
								*/
									
									
								
									
								
								//}
								
								
								
							//});
							
							
							
  
						});
						
					
						
						
					});
					
					
				}
		
	
					
					
					
			
			   </script>
        
       
    
        
        
       <div class="col-md-5" style=" margin-top: -20%;  display: inline;" align="center">
    	
		   <div class="form-area" style=" width:60%; float: right; ">  
			   
			   
			   
			   <h3 class="text-muted" align="center">Support Request</h3>
			   
			   <script>getSupportRequests();</script>
        
        <br>
                    
                    <div class="form-group">
						<label>Email: <p id="emailP"></p></label>
			   </div>
							
			   <div class="form-group">
				   <label>Subject: <p id="subjectP"></p></label>
				   </div>
				   
				   <div class="form-group">
					   <label>Message: <p id="messageP"></p></label>
					   </div>
			   
			   <button type="submit" id="populateFields" name="submit" class="btn btn-primary pull-right" value="reply" style="width: 100%;">Reply</button>
			   
			   <br/ >
			   
			   <br />
			   
						
						
						
					
			
			   
			   <h3 class="text-muted" align="center">Reply</h3>
        
        <br>
                    <form action="" method="post">
                    <div class="form-group">
						<input type="text" class="form-control" id="emailForm" name="email" placeholder="Email" required>
					</div>
					<div class="form-group">
						<input type="text" class="form-control" id="subjectForm" required name="subject" placeholder="Subject" >
					</div>
					<div class="form-group">
						<textarea class="form-control" id="messageTextarea" name="messageForm" placeholder="Message" ></textarea>
					</div>
					             
			<input type="submit" id="sendButton" name="submit" class="btn btn-primary pull-right" value="Send Message" style="width: 100%;">
			   
						
			   </form>
			<br />
		   <br />
		
			   <?php
	error_reporting(1);
	
				$button = $_POST['submit'];
				$email = $_POST['email'];
				$subject = $_POST['subject'];
				$message = $_POST['messageForm'];
				
		
		if($button){
			
			if(!empty($email) && !empty($subject) && !empty($message)){
				
				
				//Construct email and send
				
				$from = "support@fleetTracker.com";
				$headers = "From: " . $from;
				mail($email, $subject, $message, $headers);
				echo("Email Sent!");
				
				
				
				
				
			}else{
				
				die("<script>alert('Please fill in all fields');</script>");
				
			}
			
			
		}
				
	
	
				?>
		   
		 
    	
		   
		
		
       
			
      			   
			    
			  
			   
        
			    
        
           </div>
           
        
	</div>
	
	</main>
        
    
        

</html>



        
    



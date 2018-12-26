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
			
			
			firebase.auth().signOut().then(function(){
				
				window.location = "index.php";
				
				
			}).catch(function(error){
				
				
				alert(error);
				
			});
					 
					 
					 
					 
										  
										
						
						
		</script>
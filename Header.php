


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
		


<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="icon" href="../../../../favicon.ico">

    <title>Fleet Tracking Dashboard</title>

    <!-- Bootstrap core CSS -->
    <link href="vendor/twbs/bootstrap/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom styles for this template -->
    <link href="vendor/twbs/bootstrap/dist/css/dashboard.css" rel="stylesheet">
  

    <nav class="navbar navbar-dark fixed-top bg-dark flex-md-nowrap p-0 shadow">
      <a class="navbar-brand col-sm-3 col-md-2 mr-0" href="index.php">Fleet Tracker</a>
      <input id="searchForm" class="form-control form-control-dark w-100" type="text" placeholder="Search OrderID" aria-label="Search">
		<button class="btn-primary pull-right" id="searchBtn">Search</button>
        <a class="navbar-brand col-sm-2 col-md-1 mr-0" href="logout.php">Logout</a>
      <ul class="navbar-nav px-3">
        <li class="nav-item text-nowrap">
          
        </li>
      </ul>
		</nav>
	  
   

    <div class="container-fluid" style="width: 10%; height: 50%;">
      <div class="row">
        <nav class="col-md-2 d-none d-md-block bg-light sidebar">
          <div class="sidebar-sticky">
            <ul class="nav flex-column">
              <li class="nav-item">
                <a class="nav-link" href="Orders.php">
                  <span data-feather="file"></span>
                  Orders
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="Vehicles.php">
                  <span data-feather="shopping-cart"></span>
                  Vehicles
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="Users.php">
                  <span data-feather="users"></span>
                  Customers
                </a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="Support.php">
                  <span data-feather="bar-chart-2"></span>
                  Support
                </a>
              </li>
				<li class="nav-item">
                <a class="nav-link" href="Reports.php">
                  <span data-feather="reports"></span>
                  Reports
                </a>
              </li>
				<li class="nav-item">
                
                  <span data-feather="bar-chart-2"></span>
                  <p class="nav-link" id="currentUser"></p>
					
							
					
                
              </li>
              <li class="nav-item">
                <a class="nav-link">
                  <span data-feather="layers"></span>
                    <center>&copy Fleet Tracker 2018</center>
                </a>
              </li>
            </ul>
	
          </div>
        
			<script>
			
			//Search functionality
				
			
				
				
			
			
			
			</script>
		  
		 


	</head>
            
            
            
            
            
      </div>
    </div>
	</head>

   

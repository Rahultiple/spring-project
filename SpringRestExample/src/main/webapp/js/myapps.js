/* 
 *  created by rahul
 */
GlobalMyapp={
		ip:"",
		photourl:""
};
var myModule=angular.module("testapp",[]);

myModule.directive("fileModel",function($parse){
	alert('myfile');
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
});
function init(){
	alert("123");
}
function myContoller($scope,$http){
	
	var url;
	
	if(GlobalMyapp.ip=='undefined' || GlobalMyapp.ip==null || GlobalMyapp.ip==""){
		var ip="10.125.126.148";
		url="http://"+ip+":8383/SpringRestExample";
		$scope.ipaddress=ip;
	}
	$scope.init=function(){
		document.addEventListener("deviceready", function(){
		      alert("camera started");
			},true);
	};
	$scope.takePhoto=function(){
		callCamera($scope);
		
		alert("$scope.myFile=> "+$scope.myFile);
	};
	$scope.setIp= function(){
		GlobalMyapp.ip=$scope.ipaddress;
		url="http://"+GlobalMyapp.ip+":8383/SpringRestExample";
		ajaxResponseToDisplay(url,$http,$scope);
	};
	
	$scope.addPerson= function(){
		ajaxPostAddMethod(url,$http,$scope);
	};
	
	$scope.editPerson= function(id){
		
	};
	$scope.deletePerson= function(id){
	
		removePerson(url,$scope,$http,id);
	};
	$scope.upLoadFile=function(){
		var fileUrl=$scope.myFile;
		console.log('file url :: '+fileUrl);
		uploadFile(url,$scope,$http,fileUrl);
	};
	//default to display
	ajaxResponseToDisplay(url,$http,$scope);
	
}
/*
 * function to upload the image
 */
 function uploadFile(url,$scope,$http,fileUrl){
	 url=url+"/rest/person/image";
	 
	 var fd = new FormData();
				 
	 	fd.append('file', fileUrl);
		fd.append('person', "rahul_tiple");
		//fd.append('date', "10-10-2015");
	 var ajaxObj= $http.post(url,fd,{
			 							transformRequest : angular.identity,
										headers : {	'Content-Type' : undefined}
	 								}
	 						);
		outputobj= ajaxObj.success(function(data) {
			alert('Success=> '+data);
		});
		outputobj= ajaxObj.error(function(data) {
			alert('Error happened');
		});
 }

 function callCamera(scopevar){
	 //event.preventDefault();
	  if (!navigator.camera) {
	      alert("Camera API not supported", "Error");
	      return;
	  }
	  var options =   {   quality: 50,
	                     // destinationType: Camera.DestinationType.DATA_URL,
	                      destinationType: Camera.DestinationType.FILE_URI,
		                  sourceType: 1,      // 0:Photo Library, 1=Camera, 2=Saved Album
	                      encodingType: 0     // 0=JPG 1=PNG
	                  };

	  navigator.camera.getPicture(
	     onSuccess,onFail,
	      options);
	  function onSuccess(imgURL) {
          GlobalMyapp.photourl=imgURL;
          $scope.myFile=GlobalMyapp.photourl;
          alert('image captured '+imgURL);
      }
	  function onFail(imgURL) {
		  alert('image captured ERROR');
	  }
	  return false;
			 
 }
/*
 * function to add the person
 */
function editPerson(id){
	alert(id);
}
function ajaxPostAddMethod(url,$http,$scope){
	url=url+"/rest/person/add";
	var ajaxObj= $http.post(url,{name:$scope.firstname,country:$scope.country});
	outputobj= ajaxObj.success(function(data) {
		//alert('Success=> '+data);
	});
	outputobj= ajaxObj.error(function(data) {
		alert('Error happened');
	});
}
/*
 * function to display the person
 */
function ajaxResponseToDisplay(url,$http,$scope){
	console.log("inside the the ajax response");
	var outputobj="";
	url=url+"/rest/persons";
	try{
	var ajaxObj= $http.get(url);
	
	outputobj= ajaxObj.success(function(data) {
		//alert('in the suceess');
		console.log(data);
		$scope.personlist=data;
		return data;
	});
	
	outputobj= ajaxObj.error(function(data) {
		alert('in the error '+data);
		console.log(data);
			//alert('error '+data);
		$scope.personlist=data;
	});

	}catch(e){
		alert ('Exception '+e);
	}
	//return outputobj;
	
}
function removePerson(url,$scope,$http,id){
	console.log('in the remove method '+id);
	var originalurl;
	url=url+"/rest/person/delete/"+id;
	console.log(url);
	
	var ajaxObj= $http.get(url);
	
	outputobj= ajaxObj.success(function(data) {
		//alert('in the suceess');
		console.log(data);
		
		return data;
	});
	
	outputobj= ajaxObj.error(function(data) {
	
		console.log(data);
			//alert('error '+data);
		$scope.personlist=data;
	});

}
function addEditController($scope,$http){

}



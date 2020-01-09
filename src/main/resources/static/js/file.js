$(function() {
   var form = $("#register");
	enableFastFeedback(form);

	$("#register").submit(function() {

		var name = $("#name-input").val();
		var password = $("#password-input").val();
		var username = $("#username-input").val();
		var email = $("#email-input").val();

		validatNamefield(name, event);
		validatPasswordfield(password, event);
		validateUsername(username, event);
		validatemailfield(email, event);
	});
	
	
	$("#password-confirm-input").keyup(function(){
		passwordConfirm();
	});

});




function enableFastFeedback(formElement) {
	var nameInput = formElement.find("#name-input");
	var passwordInput = formElement.find("#password-input");
	var usernameInput = formElement.find("#username-input");
	var emailInput = formElement.find("#email-input");
	


	nameInput.blur(function(event){
		var name =$(this).val();
		validatNamefield(name,event);
		if(!isValidName(name)){
			$(this).css({"box-shadow":"0 0 4px #F44336" ,"border":"1px solid #F44336"});
		}else{
			$(this).css({"box-shadow":"0 0 4px #181" ,"border":"1px solid #181"});
		}
	});


  passwordInput.blur(function(event){
		var password =$(this).val();
		validatPasswordfield(password,event);
		if(!isValidPassword(password)){
			$(this).css({"box-shadow":"0 0 4px #F44336" ,"border":"1px solid #F44336"});
		}else{
			$(this).css({"box-shadow":"0 0 4px #060" ,"border":"1px solid #060"});
		}
	});

  usernameInput.blur(function(event){
		var username =$(this).val();
		validateUsername(username,event);
		if(!isValidUsername(username)){
			$(this).css({"box-shadow":"0 0 4px #F44336" ,"border":"1px solid #F44336"});
		}else{
			$(this).css({"box-shadow":"0 0 4px #181" ,"border":"1px solid #060"});
		}
	});

  emailInput.change(function(event){
		var email =$(this).val();
		validatemailfield(email, event)
		if(!isEmailValid(email)){
			$(this).css({"box-shadow":"0 0 4px #F44336" ,"border":"1px solid #F44336"});
		}else{
			$(this).css({"box-shadow":"0 0 4px #181" ,"border":"1px solid #060"});
		}
	});


}

function passwordConfirm(){
	if($("#password-input").val()!= $("#password-confirm-input").val()){
		$("#msg").html("Password do not match").css({"color":"red"});
		$("#sign-up").prop("disabled",true);
	}else{
		$("#msg").html("Password match").css({"color":"green"});
		$("#sign-up").prop("disabled",false);
	}

}

function validatNamefield(name, event) {
	if (!isValidName(name)) {
		$("#name-feedback").text("Please enter at least five character").css("color","red");
		event.preventDefault();
	} else {
		$("#name-feedback").text("");
	}
}

function isValidName(name) {
	return name.length >= 5;
}

function validatPasswordfield(password, event) {
	if (!isValidPassword(password)) {
		$("#password-feedback").text(
				"Password should be at least five character with letter and numbers").css("color","red");
		event.preventDefault();
	} else {
		$("#password-feedback").text("");
	}
}

function isValidPassword(password) {
	return password.length >= 5 && /.*[0-9].*/.test(password);
}

function validateUsername(username, event) {
	if (!isValidUsername(username)) {
		$("#username-feedback").text("Username should be atleast 5 character").css("color","red");
		event.preventDefault();
	} else {
		$("#message-feedback").text("");
	}
}

function isValidUsername(username) {
	return username.length >= 5;
}

function validatemailfield(email, event) {
	if (!isEmailValid(email)) {
		$("#email-feedback").text("Provide a valid email").css("color","red");
		event.preventDefault();
	} else {
		$("#email-feedback").text("");
	}
}

function isEmailValid(email) {
	var regex = /^([a-zA-Z0-9_.+-])+\@(([a-zA-Z0-9-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	  return regex.test(email);
}

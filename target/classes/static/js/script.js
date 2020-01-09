$(document).ready(function(){

	$(".cartItemQuantity").on("change",function(){
		
		var id=this.id;
		
		$("#update-item-"+id).css("display","inline-block");
		
	});
	$("#sameAsShippingAddress").on("click",checkBillingAddress);
	
	
	$("#soldout").on("change" ,function(){
		$("#addtoCart").prop("disabled" ,true);
	});
	
});


function checkBillingAddress(){
	if($("#sameAsShippingAddress").is(":checked")){
		$(".billingAddress").prop("disabled",true);
		
	}else{
		$(".billingAddress").prop("disabled",false);
	}
}


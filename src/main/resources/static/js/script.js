console.log("this is js file")
const toggleSidebar = () => {

if($(".sidebar").is(":visible")){
$(".sidebar").css("display","none");
$(".content").css("margin-left","0%");
}

else{
$(".sidebar").css("display","block");
$(".content").css("margin-left","20%");
}
};

const search = () => {
let query=$("#search-input").val();
if(query == ""){
$(".search-result").hide();
}
else{
let url=`http://localhost:8222/search/${query}`;
fetch(url)
       .then((response) => {
       return response.json();
        })
        .then((data) => {
        console.log(data);
        let text = `<div class='list-group'>`
        data.forEach((contact) => {
        text += `<a href='/user/${contact.cId}/contact' class='list-group-item list-group-item-action'> ${contact.name} </a>`

        });
        text += `</div>`;
        $(".search-result").html(text);
        $(".search-result").show();

        });
}
};

const paymentStart = () => {
var amount = $("#payment_field").val();
if(amount == "" || amount == null){
    alert("Amount is required");
    return;
}

$.ajax({
    url:'/user/create_order',
    data:JSON.stringify({amount:amount,info:'order_request'}),
    contentType:'application/json',
    type:'POST',
    dataType:'json',
    
    success:function(response){
        if(response.status == "created"){
        let options={
            key: "rzp_test_oHMMsYxMKfUulO",
            amount: response.amount,
            currency: "INR",
            name: "Smart Contact Manager Donation",
            description: "Donation",
            order_id: response.id,
            handler: function (response){
            alert(response.razorpay_signature)
            },
            prefill: {
            name: "Gaurav Kumar",
            email: "gaurav.kumar@example.com",
            contact: "9999999999"
            },
            notes: {
            address: "Razorpay Corporate Office"
            },
            theme: {
            color: "#3399cc"
            }
            };
            var rzp1 = new Razorpay(options);
            rzp1.on('payment.failed', function (response){
            alert("Payment Failed");
            });
            document.getElementById('rzp-button1').onclick = function(e){
            rzp1.open();
            }
        }
        }
    })
};
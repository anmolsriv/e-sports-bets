function passwordMatchVerification() {
    let cnf_password = $("#confirm_password").val();
    let password = $("#password").val();

    if( cnf_password !== password ) {
        $("#pwd_msg").html("passwords do not match!");
    } else {
        $("#pwd_msg").html("");
    }
}

function ageVerification() {
    let dob = $("#dob").val();
    console.log("DOB: " + dob)

}

$("#user").submit(function () {
    ageVerification();
})

$(document).ready(function() {

    $("#confirm_password").keyup( passwordMatchVerification );

    $("#password").keyup( passwordMatchVerification );

});




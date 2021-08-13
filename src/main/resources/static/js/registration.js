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
    let dobElement = document.getElementById('dob');
    let dob = new Date($("#dob").val());
    let ageDifMs = Date.now() - dob.getTime();
    let ageDate = new Date(ageDifMs); // miliseconds from epoch
    if (Math.abs(ageDate.getUTCFullYear() - 1970) < 18) {
      dobElement.setCustomValidity("You must be 18 years or older to signup");
      dobElement.reportValidity();
      console.log("too young")
    } else {
      dobElement.setCustomValidity("");
    };
}

$("#user").submit(function (event) {
    event.preventDefault();
})

$(document).ready(function() {

    $("#confirm_password").keyup( passwordMatchVerification );

    $("#password").keyup( passwordMatchVerification );

});




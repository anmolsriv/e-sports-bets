$("#confirm_password").change( function () {
    console.log("testing..." );
    let cnf_password = $("#confirm_password").val();
    let password = $("#password").val();

    console.log(cnf_password + " " + password );
    if( cnf_password !== password ) {
        alert("passwords do not match!");
    }

} );
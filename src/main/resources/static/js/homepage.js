var bettableMatches = new Map();
var userCredits;

$("resultsDisplay1").ready(function () {

    $.get( "/bets/user_credits").done(
        function ( data ) {
            userCredits = JSON.parse(data);
            adjustCredits();
        }
    )

    $.get( "/matches/bettable").done(
        function ( data ) {
            populateResults(data)
        }
    )
});

function populateResults(matches) {
    var rows = "";
    matches.forEach(match=>{
        rows += populateRow(match);

        $("#resultsDisplay").html(rows)//.attr('id', match.matchId);
    });
    $("#resultsDisplay").html(rows);
}

const betUponMatches = [];

function populateRow(match) {

    bettableMatches.set(match.matchId, match)

    divText = ''
    divText+='<div class="col-lg-9"\n' //outermost tag
    divText+='     style="margin:auto; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
    divText+='    <div class="container">\n'
    divText+='        <div class="row">\n'
    divText+='            <div class="col-lg-3" style="display: block; position: relative">\n'
    divText+='                <img src="images/halo5_maps/' + match.map + '.png" width="100" height="100"\n'
    divText+='                     style="background-image: url(\'images/game_icon.png\'); background-size: 100px auto; height: 100px; width: auto; display: block; position: relative; float:left;">\n'
    divText+='                <img src="images/halo5_game_variants/' + match.gameVariant.split(" ")[0] + '.png" width="100" height="100"\n'
    divText+='                     style="filter: invert(1); height: 80px; width: auto; position: absolute; left: 60px; top: 10px;">\n'
    divText+='            </div>\n'
    divText+='            <div class="col-lg-9">\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-6">\n'
    divText+='                        Time:\n'
    divText+='                        <span id="time">' + match.time +'</span>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Team\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Moneyline Odds\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Spread\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Spread Odds\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row alert-info">\n'
  divText+='                    <div class="col-sm-3">\n'
  divText+='                      <a href="#" onclick="selectTeam(\'' + match.matchId + '\', 0)">\n'
  divText+='                        ' + match.team_0 + '\n'
  divText+='                      </a>\n'
  divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      <a href="#" onclick="selectBet( \'' +  match.matchId + '\' , \'MONEYLINE\', 0 )">\n'
    divText+='                        ' + match.team0WinOdds + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team0Spread + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      <a href="#" onclick="selectBet( \'' +  match.matchId + '\', \'SPREAD\', 0 )">\n'
    divText+='                        ' + 1.91 + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row alert-danger">\n'
  divText+='                    <div class="col-sm-3">\n'
  divText+='                      <a href="#" onclick="selectTeam(\'' + match.matchId + '\', 1)">\n'
  divText+='                        ' + match.team_1 + '\n'
  divText+='                      </a>\n'
  divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      <a href="#" onclick="selectBet(\'' +  match.matchId + '\', \'MONEYLINE\', 1 )">\n'
    divText+='                        ' + match.team1WinOdds + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      ' + match.team1Spread + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      <a href="#" onclick="selectBet(\'' +  match.matchId + '\', \'SPREAD\', 1 )">\n'
    divText+='                        ' + 1.91 + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='            </div>\n'
    divText+='        </div>\n'
    divText+='    </div>\n'
    divText+='</div>\n'

    return divText;
}

// Bryan did these :)

$("#bamount").on("input", function(){

    updatePotentialWinnings();
    checkBetAmountValidity();

//     payoutRatio = betUponMatches.reduce((accumulator, currentValue) => {
//         return accumulator*currentValue.bet.odds
//     }, 1)
// //    TODO double check that odds are from correct kind of bet from front end
//
//     document.getElementById("potentialWinning").innerHTML = payoutRatio*document.getElementById("bamount").value;

})

function checkBetAmountValidity() {
  let bamount = document.getElementById('bamount');
  if ($("#bamount").val() > userCredits) {
    bamount.setCustomValidity("Insufficient credits for bet. Current credits: " + userCredits);
    bamount.reportValidity();
  } else {
    bamount.setCustomValidity("");
  }
}

updatePotentialWinnings = function(){
    payoutRatio = betUponMatches.reduce((accumulator, currentValue) => {
        return accumulator*currentValue.bet.odds
    }, 1)
//    TODO double check that odds are from correct kind of bet from front end

    document.getElementById("potentialWinning").innerHTML = payoutRatio*document.getElementById("bamount").value;
}



adjustCredits = function(){
    document.getElementById('userFunds').innerHTML = userCredits;
};


function toggleBetUponMenu() {
    var menu = document.getElementById("betsMenu");
    var results = document.getElementById("resultsDisplay");

    menu.classList.toggle("d-none");
    menu.classList.toggle("col-md-3");
    results.classList.toggle("col-md-9");
    results.classList.toggle("col-md-12");

    return null;
};

// document.getElementById("betsMenuToggle").onclick = function () {
//     toggleBetUponMenu();
//     // console.log("Aint it");
// };

function populateBetMenu(matches) {
    var rows = "";
    var idx = 0;
    matches.forEach(match=>{
        rows += populateBet(match, idx);
        $("#betUponDisplay").html(rows);
        idx += 1;
    });
    $("#betUponDisplay").html(rows);
//for each bet, sticky card of details?
}

function heheText(match) {
    // console.log(match.attributes);
    // console.log(match);
    // console.log(JSON.stringify(match));
    return "Looking to make a " + match.type + " bet on " + match.id_match + " team " + match.team + " with odds " + match.odds + " and spread " + match.spread
}

function populateBet(Match,betUponIndex) {

    match = Match.match;
    bet = Match.bet;
    // TODO make dynamic/enumerated
    chance = bet.odds;

    divText=''
    divText+='<div class="card-body" style="background-color:#ECFAEE!important">\n'
    // divText+='' + heheText(match) +  '\n'
    divText+='<p>' + match.team_0 + ' vs. ' + match.team_1 + ' </p>\n'
    divText+='<p>' + match.time + ' </p>\n'
    divText+='<br>\n'
    divText+='<p>' + bet.type + ' for ' + bet.teamName + ' with odds  ' + chance + (bet.type=="MONEYLINE"?'':(' and spread ' + bet.spread)) + '</p>\n'
    divText+='  <div class="text-center">\n'
    divText+='      <button type="button" class="btn btn-danger" onclick="unselectBet(' + betUponIndex + ')">Remove Bet</button>\n'
    divText+='  </div>\n'
    divText+='</div>\n'
    divText+='<br>'

    return divText;
}


function unselectBet(betUponIndex) {
    // console.log("removed at index", betUponIndex, ":",heheText(betUponMatches[betUponIndex]))
    betUponMatches.splice(betUponIndex,1)
    updatePotentialWinnings();
    populateBetMenu(betUponMatches);
    if (betUponMatches.length === 0){
        toggleBetUponMenu();
    }
}

function clearBets() {
    betUponMatches.length = 0;
    populateBetMenu(betUponMatches);
    toggleBetUponMenu();
}

function checkIfBetIsAlreadyAdded(matchId, type, team) {

    for (const betUponMatch of betUponMatches) {
        if ( betUponMatch.match.matchId == matchId && betUponMatch.bet.type == type &&
                betUponMatch.bet.team == team ) {
            alert ("Bet already selected previously.");
            return true;
        }
    }
    return false;
}

function selectBet(matchId, type='INCOMPLETE', team=-1) {
    if (betUponMatches.length === 0) {
        // updatePotentialWinnings();
        toggleBetUponMenu();
    }

    if ( checkIfBetIsAlreadyAdded(matchId, type, team) )
        return;

    match = bettableMatches.get(matchId);
    id_match = match.id_match;

    if ( team == 0 ) {
        odds = match.team0WinOdds;
        teamName = match.team_0;
        spread = match.team0Spread;
    } else {
        odds = match.team1WinOdds
        teamName = match.team_1;
        spread = match.team1Spread;
    }

    if (type == 'SPREAD') {
        odds = 1.91;
    }


    let bUM = {match:match, bet:{type:type, team:team, teamName:teamName, odds:odds, spread:spread} };
    let betUponIndex = betUponMatches.push(bUM);
    // betUponMatches.push("Looking to make a "+type+" bet on "+id_match+" team " + team + " with odds " + odds + " and spread " + spread);
    updatePotentialWinnings();
    populateBetMenu(betUponMatches, betUponIndex);
    // window.alert("Looking to make a "+type+" bet on "+id_match+" team " + team + " with odds " + odds + " and spread " + spread)
}

$("#submitBet").submit(function( event ){
    event.preventDefault();
    let bamount = document.getElementById('bamount');
    let amount = $("#bamount").val();

    var bets = "[";
    for (var matchBet in betUponMatches) {
        bets += "{";
        bets += "\"matchId\": \"" + betUponMatches[matchBet].match.matchId + "\",";
        bets += "\"betType\": \"" + betUponMatches[matchBet].bet.type + "\",";
        bets += "\"teamId\": " + betUponMatches[matchBet].bet.team;
        bets += "}" + (matchBet==betUponMatches.length-1?" ":", ");
    }
    bets += "]";

    var betRequest = "{" ;
    betRequest += "\"betType\": \"" + (betUponMatches.length>1?"ACCUMULATOR":"SINGLE") + "\",";
    betRequest += "\"amount\": " + amount + ",";
    betRequest += "\"bets\": " + bets;
    betRequest += "}" ;
    if (bamount.checkValidity() == true) {
      $.ajax({
        url: "/bets/place_bet",
        method: "POST",
        contentType: "application/json",
        data: betRequest,
        success: function (data) {
          clearBets();
          userCredits = JSON.parse(data);
          alert("Bet successfully placed.")
        },
        error: function (request) {
          if (request.status == 400) {
            alert(request.responseText)
          } else {
            alert("Error occurred while placing the bet.")
          }
        }
      })
    }
})


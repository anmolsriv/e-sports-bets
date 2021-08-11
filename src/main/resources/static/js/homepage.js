$("resultsDisplay1").ready(function () {
    $.get( "/matches/bettable").done(
        function ( data ) {
            console.log(typeof(data),"data: ", data)
            populateResults(data)
        }
    )
});

function populateResults(matches) {
    var rows = "";
    matches.forEach(match=>{
        console.log(typeof(match));
        rows += populateRow(match);

        $("#resultsDisplay").html(rows)//.attr('id', match.matchId);
    });
    $("#resultsDisplay").html(rows);
}

const betUponMatches = [];

function populateRow(match) {

    Match = JSON.stringify(match);
    console.log(Match);

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
    divText+='                        ' + match.team_0 + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    // divText+='                      <a href="#" onclick="selectBet(\'' + match.matchId + '\', \'MONEYLINE\', 0, ' + match.team0WinOdds + ')">\n'
    divText+='                      <a href="#" onclick="selectBet( \'' +  Match + '\' , \'MONEYLINE\', 0 )">\n'
    // divText+='                      <a href="#" onclick="console.log(JSON.stringify( '+match+' ))">\n'
    // divText+='                      <a href="#" onclick="console.log(\'' + match.matchId + '\')">\n'
    // divText+='                      <a href="#" onclick="console.log(\'' + match.team_0 + '\')">\n'
    // match = match;
    // divText+='                      <a href="#" onclick="console.log(Match)">\n'
    divText+='                        ' + match.team0WinOdds + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team0Spread + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    // divText+='                      <a href="#" onclick="selectBet(\''+ match.matchId + '\', \'SPREAD\', 0, 2, ' + match.team0Spread + ')">\n'
    divText+='                      <a href="#" onclick="selectBet(\''+ match.matchId + '\', \'SPREAD\', 0, 2, ' + match.team0Spread + ')">\n'
    divText+='                        ' + 2 + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row alert-danger">\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team_1 + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      <a href="#" onclick="selectBet(\'' + match.matchId + '\', \'MONEYLINE\', 1, ' + match.team1WinOdds + ')">\n'
    divText+='                        ' + match.team1WinOdds + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      ' + match.team1Spread + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      <a href="#" onclick="selectBet(\'' + + match.matchId + '\', \'SPREAD\', 1, 2, ' + match.team1Spread + ')">\n'
    divText+='                        ' + 2 + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    // divText+='                <div class="row">\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        \n'
    // divText+='                    </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // // divText+='                        Moneyline Odds\n'
    // divText+='                      <button class="moneyline btn btn-success" id="' + match.matchId + '" type="button"   onclick="selectBet(this.id, this.className)"   >Moneyline Bet</button>\n'
    // // divText+='                      <button type="button" id="sidebarCollapse" class="btn btn-info">pop</button>\n'
    // divText+='                    </div>\n'
    // divText+='                    <div class="col-sm-6" style="text-align: center">\n'
    // // divText+='                        Spread\n'
    // divText+='                      <button class="moneyline btn btn-success"  id="' + match.matchId + '" type="button"   onclick="selectBet(this.id, this.className)"   >Spread Bet</button>\n'
    // divText+='                    </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        Spread Odds\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='            </div>\n'
    divText+='        </div>\n'
    divText+='    </div>\n'
    divText+='</div>\n'

    return divText;
}

// Bryan did these :)

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

    // console.log(match);
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
    divText+='<p>' + bet.type + ' for ' + bet.team + ' and  ' + chance + '</p>\n'
    divText+='  <div class="text-center">\n'
    divText+='      <button type="button" class="btn btn-danger" onclick="unselectBet(' + betUponIndex + ')">Remove Bet</button>\n'
    divText+='      <p> ' + match.id_match + ' </p>\n'
    divText+='  </div>\n'
    divText+='</div>\n'
    divText+='<br>'

    return divText;
}


function unselectBet(betUponIndex) {
    // console.log("removed at index", betUponIndex, ":",heheText(betUponMatches[betUponIndex]))
    betUponMatches.splice(betUponIndex,1)
    populateBetMenu(betUponMatches);
    if (betUponMatches.length === 0){
        toggleBetUponMenu();
    }
}

function selectBet(match, type='INCOMPLETE', team=-1) {
    if (betUponMatches.length === 0) {
        toggleBetUponMenu();
    }
    console.log(match);
    id_match = match.id_match;
    odds = null;
    spread = null;
    let bUM = {match:match, bet:{type:type, team:team, odds:odds, spread:spread} };
    let betUponIndex = betUponMatches.push(bUM);
    // betUponMatches.push("Looking to make a "+type+" bet on "+id_match+" team " + team + " with odds " + odds + " and spread " + spread);
    populateBetMenu(betUponMatches, betUponIndex);
    // window.alert("Looking to make a "+type+" bet on "+id_match+" team " + team + " with odds " + odds + " and spread " + spread)
}

var settings = {
    "url": "/bets/place_bet",
    "method": "POST",
    "headers": {
        "Content-Type": "application/json"
    },
    "data": JSON.stringify({
        "betType": "ACCUMULATOR",
        "amount": 1000,
        "odds": 7.7,
        "bets": [
            {
                "matchId": "358fd72a-b651-464c-8023-ba15ba2401b1",
                "betType": "WIN",
                "odds": 2.29,
                "teamId": 0
            },
            {
                "matchId": "a2545fc3-989a-46b1-b78d-0a7f041c2a6d",
                "betType": "WIN",
                "odds": 1.68,
                "teamId": 0
            },
            {
                "matchId": "e27fbe1b-4688-417b-986b-62b326e73514",
                "betType": "SPREAD",
                "spread": -5.5,
                "odds": 2,
                "teamId": 0
            }
        ]
    }),
};

$("#submitBet").submit(function( event ){
    event.preventDefault();
    placeholder();
})

function placeholder(){

    $.ajax(settings
    ).done(function (msg){
        console.log(msg)
        })
//    xhttp = new XMLHttpRequest();
//    xhttp.open("POST", "/bets/place_bet", true);
//    xhttp.send();
}

$.ajax(settings).done(function (response) {
    console.log(response);
});

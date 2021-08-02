$("resultsDisplay1").ready(function () {
    $.get( "/matches/bettable").done(
        function ( data ) {
            console.log("data: ", data)
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

function populateRow(match) {
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
    divText+='                      <a href="#" onclick="selectBet(\'' + match.matchId + '\', \'MONEYLINE\', 0, ' + match.team0WinOdds + ')">\n'
    divText+='                        ' + match.team0WinOdds + '\n'
    divText+='                      </a>\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team0Spread + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                      <a href="#" onclick="selectBet(\'' + match.matchId + '\', \'SPREAD\', 0, 2, ' + match.team0Spread + ')">\n'
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
    divText+='                      <a href="#" onclick="selectBet(\'' + match.matchId + '\', \'SPREAD\', 1, 2, ' + match.team1Spread + ')">\n'
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
};

document.getElementById("betsMenuToggle").onclick = function () {
    toggleBetUponMenu();
    // console.log("Aint it");
};

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
    return "Looking to make a " + match.type + " bet on " + match.id_match + " team " + match.team + " with odds " + match.odds + " and spread " + match.spread
}

function populateBet(match,betUponIndex) {

    divText=''
    divText+='<div class="card-body" style="background-color:#ECFAEE!important">\n'
    divText+='' + heheText(match) +  '\n'
    divText+='<br>'
    divText+='  <div class="text-center">\n'
    divText+='      <button type="button" class="btn btn-danger" onclick="unselectBet(' + betUponIndex + ')">Remove Bet</button>\n'
    divText+='      <p> ' + match.id_match + ' </p>\n'
    divText+='  </div>\n'
    divText+='</div>\n'
    divText+='<br>'

    return divText;
}

const betUponMatches = []

function unselectBet(betUponIndex) {
    // console.log("removed at index", betUponIndex, ":",heheText(betUponMatches[betUponIndex]))
    betUponMatches.splice(betUponIndex,1)
    populateBetMenu(betUponMatches);
    if (betUponMatches.length == 0){
        toggleBetUponMenu();
    }
}

function selectBet(id_match, type, team='test', odds=1.1, spread=2.0) {
    if (betUponMatches.length == 0) {
        toggleBetUponMenu();
    }
    let bUM = {id_match:id_match, type:type, team:team, odds:odds, spread:spread };
    let betUponIndex = betUponMatches.push(bUM);
    // betUponMatches.push("Looking to make a "+type+" bet on "+id_match+" team " + team + " with odds " + odds + " and spread " + spread);
    populateBetMenu(betUponMatches, betUponIndex);
    // window.alert("Looking to make a "+type+" bet on "+id_match+" team " + team + " with odds " + odds + " and spread " + spread)
}
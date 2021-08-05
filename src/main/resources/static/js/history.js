$("#historyDisplay").ready(function () {
  $.get( "/bets/user_bets").done(
    function ( data ) {
      populateHistory(data)
    }
  )
});

function populateHistory(bets) {
  var rows = "";
  bets.forEach(bet=>{
    rows += populateHistoryRow(bet);

    $("#historyDisplay").html(rows)//.attr('id', match.matchId);
  });
  $("#historyDisplay").html(rows);
}

function populateHistoryRow(bet) {

  let winnings;
  if (bet.concluded == "WIN") {
    winnings = bet.odds * bet.amount;
  } else if (bet.concluded == "LOSS") {
    winnings = -bet.amount;
  } else if (bets.concluded == "PUSH") {
    winnings = bet.amount;
  } else {
    winnings = ""
  }

  divText = ''
  divText+='<div class="col-md-12" style="margin:auto;padding-top:20px;" id="historyDisplay">'
  divText+='<div class="container" style="display: flex;">\n'
  divText+='  <div class="col-lg-4"\n' //outermost tag
  divText+='      style="display: inline-block; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
  divText+='   <div class="container">\n'
  for (let i = 0; i < bet.userBets.length; i++) {

    let teamName;
    if (bet.userBets[i].teamId == 0) {
      teamName = bet.userBets[i].match.team1.teamName;
    } else {
      teamName = bet.userBets[i].match.team2.teamName;
    }

    if (i == 0) {
      divText+='        <div class="row">\n'
      divText+='            <div class="col-lg-6">\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12" style="text-decoration: underline; font-weight: bold;">\n'
      divText+='                        Overall Bet\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Winnings:\n'
      divText+='                        <span id="winnings">' + winnings + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Wager:\n'
      divText+='                        <span id="amount">' + bet.amount + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Odds:\n'
      divText+='                        <span id="odds">' + bet.odds + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='            </div>\n'
      divText+='            <div class="col-lg-6">\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        <span style="text-decoration: underline; font-weight: bold;">Bet ' + i+1 + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Type:\n'
      divText+='                        <span id="userBets ' + i + 'BetType">' + bet.userBets[i].betType + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Team:\n'
      divText+='                        <span id="userBets ' + i + 'Team">' + teamName + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Result:\n'
      divText+='                        <span id="userBets ' + i + 'Concluded">' + bet.userBets[i].concluded + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='            </div>\n'
      divText+='        </div>\n'
    } else {
      divText+='        <div class="row">\n'
      divText+='            <div class="col-lg-6">\n'
      divText+='            </div>\n'
      divText+='            <div class="col-lg-6">\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        <span style="text-decoration: underline; font-weight: bold;">Bet ' + i+1 + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Type:\n'
      divText+='                        <span id="userBets ' + i + 'BetType">' + bet.userBets[i].betType + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Team:\n'
      divText+='                        <span id="userBets ' + i + 'Team">' + teamName + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='                <div class="row">\n'
      divText+='                    <div class="col-sm-12">\n'
      divText+='                        Result:\n'
      divText+='                        <span id="userBets ' + i + 'Concluded">' + bet.userBets[i].concluded + '</span>\n'
      divText+='                    </div>\n'
      divText+='                </div>\n'
      divText+='            </div>\n'
      divText+='        </div>\n'
    }
    if (i != bet.userBets.length -1) {
      divText+='        <br>'
    }
  }
  divText+='    </div>\n'
  divText+='  </div>\n'

  divText+='<div class="container">\n'
  for (let i = 0; i < bet.userBets.length; i++) {
    divText+='  <div class="col-lg-12"\n' //outermost tag
    divText+='     style="display: inline-block; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
    divText+='    <div class="container">\n'
    divText+='        <div class="row">\n'
    divText+='            <div class="col-lg-4" style="display: block; position: relative">\n'
    divText+='                <img src="images/halo5_maps/' + bet.userBets[i].match.map + '.png" width="100" height="100"\n'
    divText+='                     style="background-image: url(\'images/game_icon.png\'); background-size: 100px auto; height: 100px; width: auto; display: block; position: relative; float:left;">\n'
    divText+='                <img src="images/halo5_game_variants/' + bet.userBets[i].match.gameVariant.split(" ")[0] + '.png" width="100" height="100"\n'
    divText+='                     style="filter: invert(1); height: 80px; width: auto; position: absolute; left: 60px; top: 10px;">\n'
    divText+='            </div>\n'
    divText+='            <div class="col-lg-8">\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-6">\n'
    divText+='                        Time:\n'
    divText+='                        <span id="time">' + bet.userBets[i].match.time +'</span>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        Team\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        Score\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        Spread\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row '
    divText+=                               (bet.userBets[i].match.team1.team==0)?'alert-danger':'alert-info'
    divText+=                                '">\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        ' + bet.userBets[i].match.team1.teamName + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        ' + bet.userBets[i].match.team1.score + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        ' + bet.userBets[i].match.team1.spread + '\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row '
    divText+=                               (bet.userBets[i].match.team2.team==0)?'alert-danger':'alert-info'
    divText+=                                '">\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        ' + bet.userBets[i].match.team2.teamName + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        ' + bet.userBets[i].match.team2.score + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-4">\n'
    divText+='                        ' + bet.userBets[i].match.team2.spread + '\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='            </div>\n'
    divText+='        </div>\n'
    divText+='    </div>\n'
    divText+='  </div>\n'
  }
  divText+='  </div>\n'
  divText+='</div>\n'

  return divText;
}
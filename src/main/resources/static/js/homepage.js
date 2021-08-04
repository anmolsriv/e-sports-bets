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
  divText+='            </div>\n'
  divText+='        </div>\n'
  divText+='    </div>\n'
  divText+='</div>\n'

  return divText;
}

function selectBet(id_match, type, team, odds, spread) {
  window.alert("Looking to make a "+type+" bet on "+id_match+" team " + team + " with odds " + odds + " and spread " + spread)
}
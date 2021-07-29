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

// onclickk" selectBet(' + match.matchId + ') "
// onclick = " selectBet(' + match.matchId + ') "
// onclick = " selectBet(\'' + match.matchId + '\') "


function populateRow(match) {
    divText = ''
    // divText+='<div class="col-lg-9" onclick = " selectBet(\'' + match.matchId + '\') " \n' //outermost tag
    divText+='<div class="col-lg-9"\n' //outermost tag
    divText+='     style="margin:auto; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
    divText+='    <div class="container">\n'
    divText+='        <div class="row">\n'
    divText+='            <div class="col-lg-3">\n'
    divText+='                <img src="images/halo5_maps/' + match.map + '.png" width="100" height="100"\n'
    divText+='                     style="background-image: url(\'images/game_icon.png\'); background-size: 100px auto; height: 100px; width: auto;">\n'
    // divText+='                      <button class="spread" id="' + match.matchId + '" type="button"      onclick="selectBet(this.id,this.className)"    >Spread Bet</button>\n'
    // divText+='                      <button class="accumulator" id="' + match.matchId + '" type="button" onclick="selectBet(this.id,this.className)"    >Accumulator Bet</button>\n'
    // divText+='                      <button class="moneyline" id="' + match.matchId + '" type="button"   onclick="selectBet(this.id,this.className)"   >Moneyline Bet</button>\n'
    divText+='            </div>\n'
    divText+='            <div class="col-lg-8">\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-6">\n'
    divText+='                        Time:\n'
    divText+='                        <span id="time">' + match.time + '</span>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Team\n'
    divText+='                    </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        Score\n'
    // divText+='                    </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        Winner\n'
    // divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team_1 + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team_0 + '\n'
    divText+='                    </div>\n'
    divText+='                      <div class="row">\n'
    divText+='                      <button class="spread" id="' + match.matchId + '" type="button"      onclick="selectBet(this.id,this.className)"    >Spread Bet</button>\n'
    divText+='                      <button class="accumulator" id="' + match.matchId + '" type="button" onclick="selectBet(this.id,this.className)"    >Accumulator Bet</button>\n'
    divText+='                      <button class="moneyline" id="' + match.matchId + '" type="button"   onclick="selectBet(this.id,this.className)"   >Moneyline Bet</button>\n'
    divText+='                      </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        ' + match.team1.score + '\n'
    // divText+='                    </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        ' + match.team1.result + '\n'
    // divText+='                    </div>\n'
    divText+='                </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        ' + match.team2.score + '\n'
    // divText+='                    </div>\n'
    // divText+='                    <div class="col-sm-3">\n'
    // divText+='                        ' + match.team2.result + '\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='            </div>\n'
    divText+='        </div>\n'
    divText+='    </div>\n'
    divText+='</div>\n'

    return divText;
}

function selectBet(id_match, type) {
    window.alert("Looking to make a "+type+" bet on "+id_match)
}
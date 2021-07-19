$("resultsDisplay1").ready(function () {
    $.get( "/matches/results/count").done(
        function ( data ) {
            console.log("pages: ", data)
        }
    )
    $.get( "/matches/results?page=0").done(
        function ( data ) {
            populateResults(data)
        }
    )
});

function populateResults(matches) {
    var rows = "";
    matches.forEach(match=>{
        rows += populateRow(match);
        $("#resultsDisplay").html(rows);
    });
    $("#resultsDisplay").html(rows);
}

function populateRow(match) {
    divText = ''
    divText+='<div class="col-md-8"\n'
    divText+='     style="margin:auto; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
    divText+='    <div class="container">\n'
    divText+='        <div class="row">\n'
    divText+='            <div class="col-sm-3">\n'
    divText+='                <img src="images/halo5_maps/' + match.map + '.png" width="100" height="100"\n'
    divText+='                     style="background-image: url(\'images/game_icon.png\'); background-size: 100px auto; height: 100px; width: auto;">\n'
    divText+='            </div>\n'
    divText+='            <div class="col-lg-8">\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Time:\n'
    divText+='                        <span id="time">' + match.time +'</span>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Team\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Score\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        Winner\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row '
    divText+=                               (match.team1.result=="Win")?'alert-success':'alert-danger'
    divText+=                                '">\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team1.teamName + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team1.score + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team1.result + '\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row '
    divText+=                                   (match.team2.result=="Win")?'alert-success':'alert-danger'
    divText+=                                   '">\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team2.teamName + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team2.score + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-3">\n'
    divText+='                        ' + match.team2.result + '\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='            </div>\n'
    divText+='        </div>\n'
    divText+='    </div>\n'
    divText+='</div>\n'

    return divText;
}
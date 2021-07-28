$("#resultsDisplay").ready(function () {
    refreshResults(0);
});

function refreshResults(pageNumber) {
    // var data = {
    //     betType: "SINGLE",
    //     amount : 100,
    //     odds : 10.1,
    //     bets : [
    //         {
    //             matchId : "match-3",
    //             betType : "SPREAD",
    //             teamId : 1
    //         }
    //     ]
    // };
    //
    // $.ajax({
    //     url: "/bets/place_bet",
    //     data: JSON.stringify(data),
    //     contentType: "application/JSON",
    //     type: "POST",
    //     success: function (data) {
    //         console.log(data)
    //     }
    // })
    $("#loadingModal").modal('show');
    $.get( "/matches/results?page=" + pageNumber).done(
        function ( data ) {
            populateResults(data, pageNumber)
        }
    );

}

function populateResults(matches, pageNumber) {

    var rows = "";
    matches.forEach(match=>{
        rows+=populateRow(match);
    });

    $("#resultsDisplay").html(rows);
    $.get( "/matches/results/count").done(
        function ( data ) {
            var resultsHtml = $("#resultsDisplay").html();
            resultsHtml += populatePaginationNavBar(pageNumber, data);
            $("#resultsDisplay").html(resultsHtml);
            $("#loadingModal").modal('hide');
        }
    );
}

function populatePaginationNavBar( currPage, pages ) {
    if (pages == 0) {
        return '';
    }
    let divText = ''
    divText+='<nav aria-label="Results Page navigation" style="padding: 10px">'
    divText+='    <ul class="pagination justify-content-center">'
    divText+='        <li class="page-item"><a class="page-link"' + ( ((currPage)==0)?'':('onclick="refreshResults(' + (currPage-1) + ')"') ) + '>Previous</a></li>'
    divText+='        <li class="page-item"><a class="page-link" ' + ( (currPage==0)?'':('onclick="refreshResults(0)"') ) + '> 1 </a></li>'
    divText+='          <div style="overflow-x: scroll; max-width: 80%; display: inline-flex;">'
    for (let i = 1; i < pages; i++) {
        if (i==currPage) {
            divText+='        <li class="page-item active"><a class="page-link" aria-current="page">' + (i+1) + '</a></li>'
        } else {
            divText+='        <li class="page-item"><a class="page-link" onclick="refreshResults(' + i + ')">' + (i+1) + '</a></li>'
        }
    }
    divText+='        </div>'
    divText+='        <li class="page-item"><a class="page-link" ' + ( (currPage==pages)?'':('onclick="refreshResults(' + pages + ')"') ) + '>' + (pages+1) + '</a></li>'
    divText+='        <li class="page-item"><a class="page-link" ' + ( ((currPage)==pages)?'':('onclick="refreshResults(' + (currPage+1) + ')"') ) + '>Next</a></li>'
    divText+='    </ul>'
    divText+='</nav>'
    return divText;
}

function populateRow(match) {
    let divText = ''
    divText+='<div class="col-lg-9"\n'
    divText+='     style="margin:auto; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
    divText+='    <div class="container">\n'
    divText+='        <div class="row">\n'
    divText+='            <div class="col-lg-3" style="display: block; position: relative">\n'
    divText+='                <img src="images/halo5_maps/' + match.map + '.png" width="100" height="100"\n'
    divText+='                     style="background-image: url(\'images/game_icon.png\'); background-size: 100px auto; height: 100px; width: auto; display: block; position: relative; float:left;">\n'
    divText+='                <img src="images/halo5_game_variants/' + match.gameVariant.split(" ")[0] + '.png" width="100" height="100"\n'
    divText+='                     style="filter: invert(1); height: 80px; width: auto; position: absolute; left: 60px; top: 10px;">\n'
    divText+='            </div>\n'
    divText+='            <div class="col-lg-8">\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-sm-6">\n'
    divText+='                        Time:\n'
    divText+='                        <span id="time">' + match.time +'</span>\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-6">\n'
    divText+='                        Game Variant:\n'
    divText+='                        <span id="gameVariant">' + match.gameVariant +'</span>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row">\n'
    divText+='                    <div class="col-lg-5">\n'
    divText+='                        Team\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        Score\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        Winner\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        Spread\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row '
    divText+=                               (match.team1.team==0)?'alert-danger':'alert-info'
    divText+=                                '">\n'
    divText+='                    <div class="col-lg-5">\n'
    divText+='                        ' + match.team1.teamName + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        ' + match.team1.score + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        ' + match.team1.result + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        ' + match.team1.spread + '\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div class="row '
    divText+=                                   (match.team2.team==0)?'alert-danger':'alert-info'
    divText+=                                   '">\n'
    divText+='                    <div class="col-lg-5">\n'
    divText+='                        ' + match.team2.teamName + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        ' + match.team2.score + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        ' + match.team2.result + '\n'
    divText+='                    </div>\n'
    divText+='                    <div class="col-sm-2">\n'
    divText+='                        ' + match.team2.spread + '\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='            </div>\n'
    divText+='        </div>\n'
    divText+='    </div>\n'
    divText+='</div>\n'

    return divText;
}
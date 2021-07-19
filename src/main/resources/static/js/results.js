$("resultsDisplay").ready(function () {
    refreshResults(0);
});

function refreshResults(pageNumber) {
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
        rows += populateRow(match);
        $("#resultsDisplay").html(rows);
    });

    $("#resultsDisplay").html(rows);
    $.get( "/matches/results/count").done(
        function ( data ) {
            var resultsHtml = $("#resultsDisplay").html();
            resultsHtml += populatePaginationNavBar(pageNumber, data);
            $("#resultsDisplay").html(resultsHtml);
        }
    );
    $("#loadingModal").modal('hide');

}

function populatePaginationNavBar( currPage, pages ) {
    if (pages == 0) {
        return '';
    }
    divText = ''
    divText+='<nav aria-label="Results Page navigation" style="padding: 10px">'
    divText+='    <ul class="pagination justify-content-center">'
    divText+='        <li class="page-item"><a class="page-link" href="#" onclick="refreshResults(' + (((currPage)==0)?'#':(currPage-1)) + ')">Previous</a></li>'
    for (let i = 0; i <= pages; i++) {
        if (i==currPage) {
            divText+='        <li class="page-item"><a class="page-link">' + (i+1) + '</a></li>'
        } else {
            divText+='        <li class="page-item"><a class="page-link" href="#" onclick="refreshResults(' + i + ')">' + (i+1) + '</a></li>'
        }
    }
    divText+='        <li class="page-item"><a class="page-link" href="#" onclick="refreshResults(' + (((currPage)==pages)?'#':(pages+1)) + ')">Next</a></li>'
    divText+='    </ul>'
    divText+='</nav>'
    return divText;
}

function populateRow(match) {
    divText = ''
    divText+='<div class="col-lg-9"\n'
    divText+='     style="margin:auto; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
    divText+='    <div class="container">\n'
    divText+='        <div class="row">\n'
    divText+='            <div class="col-lg-3">\n'
    divText+='                <img src="images/halo5_maps/' + match.map + '.png" width="100" height="100"\n'
    divText+='                     style="background-image: url(\'images/game_icon.png\'); background-size: 100px auto; height: 100px; width: auto;">\n'
    divText+='            </div>\n'
    divText+='            <div class="col-lg-8">\n'
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
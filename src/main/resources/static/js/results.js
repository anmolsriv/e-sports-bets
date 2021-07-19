$().ready(function () {
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
    console.log(JSON.stringify(matches));
    matches.forEach(match=>{
       $("#resultsTable").appendChild("");
    });
}

function populateRow(match) {
    divText = ''
    divText+='<div className="col-md-8"\n'
    divText+='     style="margin:auto; padding:20px; border-style:solid; border-width:2px; border-color:lightgray; border-radius:10px;">\n'
    divText+='    <div className="container">\n'
    divText+='        <div className="row">\n'
    divText+='            <div className="col-sm-3">\n'
    divText+='                <img src="images/halo5_maps/' + match.map + '.png" width="100" height="100"\n'
    divText+='                     style="background-image: url(\'images/game_icon.png\'); background-size: 100px auto; height: 100px; width: auto;">\n'
    divText+='            </div>\n'
    divText+='            <div className="col-lg-8">\n'
    divText+='                <div className="row">\n'
    divText+='                    <div className="col-sm-3">\n'
    divText+='                        Time:\n'
    divText+='                        <span id="time">12:00</span>\n'
    divText+='                    </div>\n'
    divText+='                </div>\n'
    divText+='                <div className="row">\n'
    divText+='                    <div className="col-sm-3">\n'
    divText+='                        Team\n'
    divText+='                    </div>\n'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Score\n'
    divText+='                    </div>'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Winner'
    divText+='                    </div>'
    divText+='                </div>'
    divText+='                <div className="row alert-danger">'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Team'
    divText+='                    </div>'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Score'
    divText+='                    </div>'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Winner'
    divText+='                    </div>'
    divText+='                </div>'
    divText+='                <div className="row alert-success">'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Team'
    divText+='                    </div>'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Score'
    divText+='                    </div>'
    divText+='                    <div className="col-sm-3">'
    divText+='                        Winner'
    divText+='                    </div>'
    divText+='                </div>'
    divText+='            </div>'
    divText+='        </div>'
    divText+='    </div>'
    divText+='</div>'

}
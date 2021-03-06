'use strict';

let tableColumns = {"Gamertag": "gamertag", "Kills": "kills",
  "Deaths": "deaths", "Assists": "assists", "Weapon Damage": "weaponDamage",
  "KDA": "kda", "Win %": "winPercentage", "Accuracy %": "accuracy",
  "Perfect Kills": "perfectKill", "Power Weapon Kills": "powerWeaponKills"}

$(document).ready(function() {
  $(window).keydown(function(event){
    if(event.keyCode == 13) {
      event.preventDefault();
      let searchInputValue = $('#searchInput').val();
      handleClickGamertag(searchInputValue)
      return false;
    }
  });
});

$('input[type=search]').on('search', function () {
  findPlayer("");
});

function findPlayer(searchString) {
  if (searchString.length > 0) {
    searchString = searchString.toLowerCase();
    $.get( "/players/player_search/" + searchString).done(
      function ( data ) {
        createPlayerListGroup(data.map(player => player["gamertag"]))
      }
    )
  } else {
    createPlayerListGroup([])
  }
}

function getTopPlayers() {
  let attribute = $('#selectAttribute :selected').val();
  let limit = $('#playerLimit').val();
  if (document.getElementById('playerLimit').checkValidity() == true) {
    $.ajax({
      type: 'get',
      url: '/players/top_players',
      data: { attribute: attribute, limit: limit },
      traditional: true
    }).done(
      function ( data ) {
        $("#playerTable tbody").empty();
        addPlayersToTable(data);
      }
    )
  } else {
    document.getElementById('playerLimit').reportValidity()
  }
}

function selectTeam(matchId, teamId) {
  $.ajax({
    type: 'get',
    url: '/players/team_stats',
    data: { matchId: matchId, teamId: teamId },
    traditional: true
  }).done(
    function ( data ) {
      setCookie("gamertags", JSON.stringify(data), 0.003);
      window.location.href = window.location.origin + "/players";
    }
  )
}

function createPlayerListGroup(gamertags) {
  let searchResults = document.getElementById("searchResults");
  searchResults.innerHTML = "";
  for (let gamertag of gamertags) {
    let listItem = document.createElement('a');
    listItem.className = "list-group-item list-group-item-action";
    listItem.innerHTML = gamertag;
    listItem.addEventListener("click",
      () => handleClickGamertag(gamertag));
    searchResults.appendChild(listItem);
  }
}

function getPlayerStatistics(gamertags) {
  $.ajax({
    type: 'get',
    url: '/players/player_stats',
    data: { gamertags: gamertags },
    traditional: true
  }).done(
    function ( data ) {
      addPlayersToTable(data)
    }
  )
}

function getGamertagsInTable() {
  let gamertags = [];
  $("#playerTable tr").not(':first').each(function(){
    gamertags.push($(this).find("td:first").text().toLowerCase());
  });
  return gamertags
}

function handleClickGamertag(gamertag) {
  let gamertags = getGamertagsInTable();
  if (gamertags.indexOf(gamertag) === -1) {
    let url = window.location.href.split("/")
    if (url[url.length - 1] != "players") {
      setCookie("gamertags", gamertag, 0.003)
      window.location.href = window.location.origin + "/players";
    }
    getPlayerStatistics([gamertag]);
  }
  let searchInput = document.getElementById("searchInput");
  searchInput.value = gamertag;
  searchInput.value = "";
  findPlayer("");
}

function deletePlayerFromTable(btnId) {
  let rowId = "row" + btnId.substring(3)
  $('#' + rowId).remove();
}

function buildPlayersTable() {
  let table = document.getElementById("playerTable");
  let thead = document.createElement('thead');
  thead.setAttribute('id', 'playerHead');
  let tbody = document.createElement('tbody');
  tbody.setAttribute('id', 'playerBody');

  let theadTr = document.createElement('tr');
  for (let columnHeader in tableColumns) {
    let theadTh = document.createElement('th');
    theadTh.innerHTML = columnHeader;
    theadTr.appendChild(theadTh);
  }
  let theadTh = document.createElement('th');
  theadTr.appendChild(theadTh);
  thead.appendChild(theadTr);
  table.appendChild(thead);
  table.appendChild(tbody);
};

function addPlayersToTable(players) {
  let tbody = document.getElementById("playerBody");
  for (let j = 0; j < players.length; j++) {
    let tbodyTr = document.createElement('tr');
    tbodyTr.setAttribute('id',
      "row-" + players[j]["gamertag"].split(" ").join(""));
    for (let column in tableColumns) {
      let tbodyTd = document.createElement('td')
      let cellValue;
      let playerValue = players[j][tableColumns[column]]
      if (Number.isInteger(playerValue)) {
        cellValue = playerValue.toString().replace(
          /\B(?=(\d{3})+(?!\d))/g, ","
        );
      } else if (typeof playerValue == "string") {
        cellValue = playerValue
      } else {
        cellValue = Math.round(
          (playerValue + Number.EPSILON) * 100) / 100
      }
      tbodyTd.innerHTML = cellValue;
      tbodyTr.appendChild(tbodyTd);
    }
    let tbodyTd = document.createElement('td');
    let btnId = "btn-" + players[j]["gamertag"].split(" ").join("");
    tbodyTd.innerHTML = ('<button class="btn" id="'
      + btnId + '" onclick="deletePlayerFromTable(this.id)">' +
      '<i class="bi bi-trash"></i></button>');
    tbodyTr.appendChild(tbodyTd);
    tbody.appendChild(tbodyTr);
  }
}

function setCookie(name, value, days) {
  var expires = "";
  if (days) {
    var date = new Date();
    date.setTime(date.getTime() + (days*24*60*60*1000));
    expires = "; expires=" + date.toUTCString();
  }
  document.cookie = name + "=" + (value || "")  + expires + "; path=/";
}
function getCookie(name) {
  var nameEQ = name + "=";
  var ca = document.cookie.split(';');
  for(var i=0;i < ca.length;i++) {
    var c = ca[i];
    while (c.charAt(0)==' ') c = c.substring(1,c.length);
    if (c.indexOf(nameEQ) == 0) return c.substring(nameEQ.length,c.length);
  }
  return null;
}
function eraseCookie(name) {
  document.cookie = name +'=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function parseCookie(cookie) {
  if (cookie[0] == "[") {
    cookie = cookie.slice(1, -1);
    cookie = cookie.replace(/["]+/g, '')
    return cookie.split(',');
  }
  return [cookie]
}

window.onload = function() {
  buildPlayersTable();
  let cookie = getCookie("gamertags");
  let gamertags = parseCookie(cookie)
  if (gamertags.length != 0) {
    getPlayerStatistics(gamertags);
    eraseCookie("gamertags");
  }

};
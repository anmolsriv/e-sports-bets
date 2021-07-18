'use strict';



function findPlayer(searchString) {
  if (searchString.length > 0) {
    searchString = searchString.toLowerCase();
    $.get( "/player_search/" + searchString).done(
      function ( data ) {
        createPlayerListGroup(data.map(player => player["gamertag"]))
      }
    )
  } else {
    createPlayerListGroup([])
  }
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

function updatePlayerTable(gamertags) {
  let playerTable = document.getElementById("playerTable");
  playerTable.innerHTML = "";
  if (gamertags.length > 0) {
    $.ajax({
      type: 'get',
      url: '/player_stats',
      data: { gamertags: gamertags },
      traditional: true
    }).done(
      function ( data ) {
        buildPlayersTable(data)
      }
    )
  } else {
    buildPlayersTable([])
  }
}

function getGamertagsInTable() {
  let gamertags = [];
  $("#playerTable tr").not(':first').each(function(){
    gamertags.push($(this).find("td:first").text());
  });
  return gamertags
}

function handleClickGamertag(gamertag) {
  let gamertags = getGamertagsInTable();
  if (gamertags.indexOf(gamertag) === -1) {
    gamertags.push(gamertag)
  }
  let searchInput = document.getElementById("searchInput");
  searchInput.value = gamertag;
  updatePlayerTable(gamertags);
  searchInput.value = "";
  findPlayer("");
}

function deletePlayerFromTable(rowId) {
  let gamertags = getGamertagsInTable();
  gamertags.splice(rowId, 1);
  updatePlayerTable(gamertags)
}

function buildPlayersTable(players) {
  let table = document.getElementById("playerTable");
  let thead = document.createElement('thead');
  let tbody = document.createElement('tbody');

  let columns = {"Gamertag": "gamertag", "Kills": "kills",
    "Deaths": "deaths", "Assists": "assists", "Weapon Damage": "weaponDamage",
    "KDA": "kda", "Win %": "winPercentage", "Accuracy %": "accuracy",
    "Perfect Kills": "perfectKill", "Power Weapon Kills": "powerWeaponKills"}
  let theadTr = document.createElement('tr');
  for (let columnHeader in columns) {
    let theadTh = document.createElement('th');
    theadTh.innerHTML = columnHeader;
    theadTr.appendChild(theadTh);
  }
  let theadTh = document.createElement('th');
  theadTr.appendChild(theadTh);
  thead.appendChild(theadTr);
  table.appendChild(thead);

  for (let j = 0; j < players.length; j++) {
    let tbodyTr = document.createElement('tr');
    for (let column in columns) {
      let tbodyTd = document.createElement('td')
      let cellValue;
      if (Number.isInteger(players[j][columns[column]])) {
        cellValue = players[j][columns[column]].toString().replace(
          /\B(?=(\d{3})+(?!\d))/g, ","
        );
      } else if (typeof players[j][columns[column]] == "string") {
        cellValue = players[j][columns[column]]
      } else {
        cellValue = Math.round(
          (players[j][columns[column]] + Number.EPSILON) * 100) / 100
      }
      tbodyTd.innerHTML = cellValue;
      tbodyTr.appendChild(tbodyTd);
    }
    let tbodyTd = document.createElement('td');
    tbodyTd.innerHTML = ('<button class="btn" id="row' + j +
      '" onclick="deletePlayerFromTable(parseInt(this.id.substring(3)))">' +
      '<i class="bi bi-trash"></i></button>');
    tbodyTr.appendChild(tbodyTd);
    tbody.appendChild(tbodyTr);
  }
  table.appendChild(tbody);
};

window.onload = function() {
  buildPlayersTable([]);
};
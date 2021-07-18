'use strict';

let tableColumns = {"Gamertag": "gamertag", "Kills": "kills",
  "Deaths": "deaths", "Assists": "assists", "Weapon Damage": "weaponDamage",
  "KDA": "kda", "Win %": "winPercentage", "Accuracy %": "accuracy",
  "Perfect Kills": "perfectKill", "Power Weapon Kills": "powerWeaponKills"}

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

function getPlayerStatistics(gamertag) {
  $.ajax({
    type: 'get',
    url: '/player_stats',
    data: { gamertags: [gamertag] },
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
    gamertags.push($(this).find("td:first").text());
  });
  return gamertags
}

function handleClickGamertag(gamertag) {
  let gamertags = getGamertagsInTable();
  if (gamertags.indexOf(gamertag) === -1) {
    getPlayerStatistics(gamertag);
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

window.onload = function() {
  buildPlayersTable();
};
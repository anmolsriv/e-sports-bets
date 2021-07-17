'use strict';

let players = [
  {"Gamertag": "player1", "KDA": 2.5, "Win Percentage": 60, "Accuracy": 60},
  {"Gamertag": "player2", "KDA": 2.5, "Win Percentage": 60, "Accuracy": 60}
]

let allPlayers = [
  {"Gamertag": "player1", "KDA": 2.5, "Win Percentage": 60, "Accuracy": 60},
  {"Gamertag": "player2", "KDA": 2.5, "Win Percentage": 60, "Accuracy": 60},
  {"Gamertag": "testPlayer3", "KDA": 2.5, "Win Percentage": 60, "Accuracy": 60},
  {"Gamertag": "testPlayer4", "KDA": 2.5, "Win Percentage": 60, "Accuracy": 60}
]

function findPlayer(searchString) {
  searchString = searchString.toLowerCase();
  let players;
  if (searchString.length > 0) {
    $.get( "http://localhost:4567/players_info/" + searchString, function( data ) {
      console.log("data: ", data);
      createPlayerListGroup(data)
    })
  } else {
    players = [];
    createPlayerListGroup(players)
  }
}

function createPlayerListGroup(players) {
  let searchResults = document.getElementById("searchResults");
  searchResults.innerHTML = "";
  for (let player of players) {
    let listItem = document.createElement('a');
    listItem.className = "list-group-item list-group-item-action";
    listItem.innerHTML = player["gamertag"];
    listItem.addEventListener("click",
      () => handleClickGamertag(player["gamertag"]));
    searchResults.appendChild(listItem);
  }
}

function handleClickGamertag(gamertag) {
  let searchInput = document.getElementById("searchInput");
  searchInput.value = gamertag;
  getPlayerStats(searchInput.value);
  searchInput.value = "";
  findPlayer("");
}

function getPlayerStats(player) {
  console.log("getting ", player, " stats.")
}

function deletePlayerFromTable(rowId) {
  players.splice(rowId, 1);
  let playerTable = document.getElementById("playerTable");
  playerTable.innerHTML = "";
  buildPlayerTable();
}

function buildPlayerTable() {
  let table = document.getElementById("playerTable");
  let thead = document.createElement('thead');
  let tbody = document.createElement('tbody');

  let columnHeaders = Object.keys(players[0]);
  let theadTr = document.createElement('tr');
  for (let i = 0; i < columnHeaders.length; i++) {
    let theadTh = document.createElement('th');
    theadTh.innerHTML = columnHeaders[i];
    theadTr.appendChild(theadTh);
  }
  let theadTh = document.createElement('th');
  theadTr.appendChild(theadTh);
  thead.appendChild(theadTr);
  table.appendChild(thead);

  for (let j = 0; j < players.length; j++) {
    let tbodyTr = document.createElement('tr');
    for (let k = 0; k < columnHeaders.length; k++) {
      let tbodyTd = document.createElement('td');
      tbodyTd.innerHTML = players[j][columnHeaders[k]];
      tbodyTr.appendChild(tbodyTd);
    }
    let tbodyTd = document.createElement('td');
    tbodyTd.innerHTML = ('<button class="btn" id="row' + j +
      '" onclick="deletePlayerFromTable(this.id)">' +
      '<i class="bi bi-trash"></i></button>');
    tbodyTr.appendChild(tbodyTd);
    tbody.appendChild(tbodyTr);
  }
  table.appendChild(tbody);
};

window.onload = function() {
  buildPlayerTable();
};
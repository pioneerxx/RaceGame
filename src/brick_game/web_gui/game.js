import { applyRootStyles } from './src/utils.js';
import { GameBoard } from './src/game-board.js';
import { rootStyles, keyCodes } from './src/config.js';

applyRootStyles(rootStyles);
const url = 'http://localhost:8000/api'
const gameBoard = new GameBoard(document.querySelector('#game-board'));
const $sidePanel = document.querySelector('#side-panel');
const $scoreTabloid = document.getElementById('score')
const $topScoreTabloid = document.getElementById('top-score')
const $message = document.getElementById('message')
var gameStatus = false;
var isPaused = false;
var speed;
var $level = document.getElementById('level');
var $newHighscore = document.getElementById('newHighscore')

$newHighscore.style.display = 'none'
getHighScore()

document.getElementById("startButton").addEventListener("click", function() {
    if (!gameStatus)
        startGame();
})

document.getElementById("pauseButton").addEventListener("click", function() {
    if (gameStatus) {
        sendPostRequest({Action: 'Pause'})
        if (isPaused) {
            isPaused = false;
            $message.style.display = 'none';
        } else {
            isPaused = true;
            $message.style.display = 'flex';
        }
    }
})

document.getElementById("stopButton").addEventListener("click", function() {
    if (gameStatus)
        stopGame();
})

document.addEventListener('keydown', async function (event) {
    if (gameStatus && !isPaused) {
        var action = analyzeKey(event.code);
        if (action != "Nope") {
            var body = await sendPostRequest({Action: action, isHold: true});
            updateBoard(body);
        }
    }
});

window.addEventListener('beforeunload', function() {
    if (gameStatus) {
        stopGame()
    }
})

function sendPostRequest(data) {
    return new Promise((resolve, reject) => {
        const xhr = new XMLHttpRequest();
        xhr.open('POST', url, true);
        xhr.setRequestHeader('Content-Type', 'application/json; charset=UTF-8');

        xhr.onload = function() {
            if (xhr.status >= 200 && xhr.status < 300) {
                const responseData = JSON.parse(xhr.responseText);
                resolve(responseData);
            } else {
                reject('Request failed with status: ' + xhr.status);
            }
        };

        xhr.onerror = function() {
            reject('There was a problem with the POST request.');
        };

        xhr.send(JSON.stringify(data));
    });
}

function delay(time) {
    return new Promise(resolve => setTimeout(resolve, time));
  }

async function startGame() {
    var start = {
        Action: "Start"
    }
    var body = await sendPostRequest(start);
    console.log(body)
    if (body == null) {
        return;
    }
    gameStatus = true;
    $message.textContent = 'PAUSE'
    $message.style.display = 'none'
    while (gameStatus) {
        updateBoard(body);
        await delay(speed);
        body = await sendPostRequest({Action: "Action"});
    }
}

function getHighScore() {
    const xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.onload = function() {
            if (xhr.status >= 200 && xhr.status < 300) {
                $topScoreTabloid.textContent = xhr.responseText;
            } else {
                $topScoreTabloid.textContent = "0"
            }
        };

        xhr.onerror = function() {
            $topScoreTabloid.textContent = "0"
        };

        xhr.send();
}

async function stopGame() {
    gameStatus = false;
    await sendPostRequest({Action: "Terminate"});
    $message.textContent = "GAME OVER"
    $message.style.display = 'flex'
}

async function updateBoard(body) {
        speed = body.speed;
        $level.textContent = body.level;
        if (body.score == body.topScore)
            $newHighscore.style.display = 'flex'
        $scoreTabloid.textContent = body.score
        $topScoreTabloid.textContent = body.topScore
        for (var i = 0; i < body.map.length; i++) {
            for (var j = 0; j < body.map[i].length; j++) {
                if (body.map[i][j])
                    gameBoard.enableTile(i, j);
                else
                    gameBoard.disableTile(i, j);
            }
        }
        if (body.state == "END") {
            stopGame();
        }
}

function analyzeKey(code) {
    switch(code) {
        case "KeyD":
        case "ArrowRight":
            return "Right";
        case "KeyW":
        case "ArrowUp":
            return "Up";
        case "KeyA":
        case "ArrowLeft":
            return "Left";
        case "KeyS":
        case "ArrowDown":
            return "Down";
        default:
            return "Nope";
    }
}
'use strict';

//app to draw polymorphic shapes on canvas
var app;
var intervalID;

/**
 * Create the paint object world app for a canvas
 * @param canvas The canvas to draw paintable canvas objects on
 * @returns {{drawCircle: drawCircle, drawImage: drawImage, clear: clear}}
 */
function createApp(canvas) {
    let c = canvas.getContext("2d");

    /**
     * Draw a circle
     * @param x  The x location coordinate
     * @param y  The y location coordinate
     * @param radius  The circle radius
     * @param color The circle color
     */
    let drawCircle = function(x, y, radius, color) {
        c.fillStyle = color;
        c.beginPath();
        c.arc(x, y, radius, 0, 2 * Math.PI, false);
        c.closePath();
        c.fill();
    };

    let drawImage = function(src, x, y, width, height, angle, scaleX, scaleY) {
        let im = document.getElementById(src);
        c.save();
        //TODO WARP
            c.drawImage(im, x,y,width,height);
        c.restore();
    };

    let clear = function() {
        c.clearRect(0,0, canvas.width, canvas.height);
    };


    return {
        drawCircle,
        drawImage,
        clear,
        dims: {height: canvas.height, width: canvas.width}
    }
}


window.onload = function() {
    app = createApp(document.querySelector("canvas"));
    clear();
    canvasDims();

    clearInterval(intervalID);
    intervalID = setInterval(updatePaintWorld,100);
    //todo: fit&finish: make this timeout 100 instead

    //CREATE button
    //Per its name, its pressing calls loadPaintObject with on all of the details provided in the dropdown menus
    $("#btn-load").click(function () {

        let strategy = document.getElementById("menu-strategy").value;
        let variety = document.getElementById("menu-variety").value;
        let switchable = document.getElementById("menu-switchable").value;

        //The paintObj variable will eventually called from its respective load/paintObj object
        //the values variable is JSON of the object's strategy, variety, and switchability,
        // sent privately via JSON in the POST request, eventually
        //HEREBE JSON DEFINITIONS
        //TODO DELETE
        let values = {strategy:strategy, variety:variety, switchable:switchable};
        let paintObj = document.getElementById("menu-BallFish").value;

        loadPaintObj(JSON.stringify(values) ,paintObj);
    });

    $("#menu-BallFish").click(function () {
        let BallOrFish = document.getElementById("menu-BallFish").value;

        $('.ball').attr('hidden',true);
        $('.fish').attr('hidden',true);
        $('.default').val('');
        if (BallOrFish!=="") {
            $('.' + BallOrFish).attr('hidden',false);
        }

    });

    $("#btn-clear").click(clear);
    $("#btn-switch").click(switchStrategy);
};

function drawPaintObj(data) {
    switch(data.type) {
        case "ball":
            app.drawCircle(data.loc.x, data.loc.y, data.radius, data.color);
            break;
        case "fish":
            // app.drawFishImage("blobleft", 50, 50, 1000,30,0,10,10);
            // app.drawFishImage(values.variety,"left", data.loc.x, data.loc.y,
            //     data.angle, data.scale.x, data.scale.y);
            //TODO max parameter-based
            app.drawImage("blobleft",data.loc.x,data.loc.y,400,400,0,1,1);

            break;
        default:
            window.alert("Something is rotten in the state of Denmark.");
    }
}


/**
 * load a paint object at a location on the canvas
 */
function loadPaintObj(values, paintObj) {
    // let values = "";
    // let paintObj = "";

    $.post("/load/" + paintObj,  values, function (data) {
        // drawPaintObj(paintObj,data);
        switch(paintObj) {
            case "ball":
                app.drawCircle(data.loc.x, data.loc.y, data.radius, data.color);
                break;
            case "fish":
                // app.drawFishImage("blobleft", 50, 50, 1000,30,0,10,10);
                // app.drawFishImage(values.variety,"left", data.loc.x, data.loc.y,
                //     data.angle, data.scale.x, data.scale.y);
                app.drawImage("blobleft",400,400,400,400,0,1,1);

                break;
            default:
                window.alert("Something is rotten in the state of Denmark.")
        }
    }, "json");
}

/**
 * switch paint objects
 */
function switchStrategy() {
    let values = "";

    $.post("/switch", { strategies: values}, function (data) {


    }, "json");
}

function updatePaintWorld() {
    $.get("/update", function(dataData) {
        app.clear();
        dataData.forEach(function (data){
            // data.update();
            drawPaintObj(data);
        });

    }, "json");
}

/**
 * Pass along the canvas dimensions
 */
function canvasDims() {
    $.get("/canvas", {height: app.dims.height, width: app.dims.width});
}

/**
 * Clear the canvas
 */
function clear() {
    $.get("/clear");
    app.clear();
}
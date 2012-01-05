function getPosition() {
    var position = {};
    var x = $("#x").val();
    if (isNaN(x)) {
        position.x = 100;
    } else {
        position.x = x;
    }
    var y = $("#y").val();
    if (isNaN(y)) {
        position.y = 100;
    } else {
        position.y = y;
    }
    var z = $("#z").val();
    if (isNaN(z)) {
        position.z = 0;
    } else {
        position.z = z;
    }
    return position;
}
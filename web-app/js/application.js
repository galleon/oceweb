if (typeof jQuery !== 'undefined') {
    (function ($) {
        $('#spinner').ajaxStart(
            function () {
                $(this).fadeIn();
            }).ajaxStop(function () {
                $(this).fadeOut();
            });
    })(jQuery);
}
$(document).ready(function () {
    $(".shapeForm").submit(function () {
        showShape($(this).attr('action'), 'content', $(this).serialize())
        return false
    })

})

var camera, mesh, renderer;
function showShape(url, containerId, data) {
    var container = $('#' + containerId);
    var containerWidth = $(container).width();
    camera = new THREE.PerspectiveCamera(60, containerWidth / window.innerHeight, 1, 2000);
//    camera.position.x = data.x ? data.x : -6;
    camera.position.y = -6;
    camera.position.z = 1000;

    scene = new THREE.Scene();

    $.getJSON(url, data, function (response) {
        var loader = new THREE.JSONLoader();
        loader.createModel(response, function (geometry) {
            mesh = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
            scene.add(mesh);
        })

        renderer = new THREE.CanvasRenderer();
        renderer.setSize(containerWidth, window.innerHeight);
        $(container).html(renderer.domElement);
    })

}

//

function animate() {
    requestAnimationFrame(animate);
    render();
}

function render() {

    if (mesh) {
        mesh.rotation.x -= 0.005;
        mesh.rotation.y -= 0.01;
    }
    if (renderer) {
        renderer.render(scene, camera);
    }
}

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

var container;
var containerWidth, containerHeight;
var camera, scene, renderer;

var cube, plane;

var targetRotation = 0;
var targetRotationOnMouseDown = 0;

var mouseX = 0;
var mouseXOnMouseDown = 0;
var mouseY = 0;
var mouseYOnMouseDown = 0;

var windowHalfX, windowHalfY;

function createCube(width) {

    container = $("#cubeResult");
    containerWidth = $(container).width();
    containerHeight = $(container).height();
    windowHalfX = containerWidth;
    windowHalfY = containerHeight;
    scene = new THREE.Scene();

    camera = new THREE.PerspectiveCamera(70, containerWidth / containerHeight, 1, 1000);
    camera.position.y = 150;
    camera.position.z = 500;
    scene.add(camera);

    // Cube

    var materials = [];

    for (var i = 0; i < 6; i++) {
        materials.push(new THREE.MeshBasicMaterial({ color:Math.random() * 0xffffff }));
    }

    cube = new THREE.Mesh(new THREE.CubeGeometry(width, width, width, 1, 1, 1, materials), new THREE.MeshFaceMaterial());
    cube.position.y = 150;
    cube.overdraw = true;
    scene.add(cube);

    // Plane

    plane = new THREE.Mesh(new THREE.PlaneGeometry(width, width), new THREE.MeshBasicMaterial({ color:0xe0e0e0 }));
    plane.rotation.x = -90 * ( Math.PI / 180 );
    plane.overdraw = true;
    scene.add(plane);

    renderer = new THREE.CanvasRenderer();
    renderer.setSize(containerWidth, containerHeight);

    $(container).html(renderer.domElement);

    $(container).bind('mousedown', onDocumentMouseDown);
    $(container).bind('touchstart', onDocumentTouchStart);
    $(container).bind('touchmove', onDocumentTouchMove);
}

//

function onDocumentMouseDown(event) {

    event.preventDefault();

    $(container).bind('mousemove', onDocumentMouseMove);
    $(container).bind('mouseup', onDocumentMouseUp);
    $(container).bind('mouseout', onDocumentMouseOut);

    mouseXOnMouseDown = event.clientX - windowHalfX;
    targetRotationOnMouseDown = targetRotation;
}

function onDocumentMouseMove(event) {

    mouseX = event.clientX - windowHalfX;
    targetRotation = targetRotationOnMouseDown + ( mouseX - mouseXOnMouseDown ) * 0.02;
}

function onDocumentMouseUp(event) {

    $(container).unbind('mousemove', onDocumentMouseMove);
    $(container).unbind('mouseup', onDocumentMouseUp);
    $(container).unbind('mouseout', onDocumentMouseOut);
}

function onDocumentMouseOut(event) {

    $(container).unbind('mousemove', onDocumentMouseMove);
    $(container).unbind('mouseup', onDocumentMouseUp);
    $(container).unbind('mouseout', onDocumentMouseOut);
}

function onDocumentTouchStart(event) {

    if (event.touches.length == 1) {

        event.preventDefault();

        mouseXOnMouseDown = event.touches[ 0 ].pageX - windowHalfX;
        targetRotationOnMouseDown = targetRotation;

    }
}

function onDocumentTouchMove(event) {

    if (event.touches.length == 1) {

        event.preventDefault();

        mouseX = event.touches[ 0 ].pageX - windowHalfX;
        targetRotation = targetRotationOnMouseDown + ( mouseX - mouseXOnMouseDown ) * 0.05;

    }
}

//

function animateCube() {
    requestAnimationFrame(animateCube);
    renderCube();
}

function renderCube() {
    plane.rotation.z = cube.rotation.y += ( targetRotation - cube.rotation.y ) * 0.05;
    renderer.render(scene, camera);

}
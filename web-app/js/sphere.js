var container;
var containerWidth, containerHeight;
var camera, scene, renderer;

var sphere;

var targetRotation = 0;
var targetRotationOnMouseDown = 0;

var mouseX = 0;
var mouseXOnMouseDown = 0;
var mouseY = 0;
var mouseYOnMouseDown = 0;

var windowHalfX, windowHalfY;

function createSphere(radius) {
    var position = getPosition();
    container = $("#sphereResult");
    containerWidth = $(container).width();
    containerHeight = $(container).height();
    windowHalfX = containerWidth;
    windowHalfY = containerHeight;
    scene = new THREE.Scene();

    camera = new THREE.PerspectiveCamera(75, containerWidth / containerHeight, 1, 10000);
    camera.position.z = 1000;
    scene.add(camera);

    sphere = new THREE.SphereGeometry(radius, 30, 30, true);
    var material = new THREE.MeshBasicMaterial({ color:Math.random() * 0xffffff, wireframe:false });

    mesh = new THREE.Mesh(sphere, material);
    mesh.position.x = position.x;
    mesh.position.y = position.y;
    mesh.position.z = position.z;
    scene.add(mesh);

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

function animateSphere() {
    requestAnimationFrame(animateSphere);
    renderSphere();
}

function renderSphere() {
    mesh.rotation.y += 0.01;
    mesh.rotation.x += 0.01;
    renderer.render(scene, camera);


}
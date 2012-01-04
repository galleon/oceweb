var container;
var containerWidth, containerHeight;
var camera, scene, renderer;

var cylinder, plane;

var targetRotation = 0;
var targetRotationOnMouseDown = 0;

var mouseX = 0;
var mouseXOnMouseDown = 0;
var mouseY = 0;
var mouseYOnMouseDown = 0;

var windowHalfX, windowHalfY;

function createCylinder(radius, height) {

    container = $("#cylinderResult");
    containerWidth = $(container).width();
    containerHeight = $(container).height();
    windowHalfX = containerWidth;
    windowHalfY = containerHeight;
    scene = new THREE.Scene();

    camera = new THREE.PerspectiveCamera(75, containerWidth / containerHeight, 1, 10000);
    camera.position.z = 1000;
    scene.add(camera);

    cylinder = new THREE.CylinderGeometry(radius, radius, height, 30, true);
    var material = new THREE.MeshBasicMaterial({ color:0xff0000, wireframe:false });

    mesh = new THREE.Mesh(cylinder, material);
    scene.add(mesh);

    //Plane
    plane = new THREE.Mesh(new THREE.PlaneGeometry(radius, radius), new THREE.MeshBasicMaterial({ color:0xe0e0e0 }));
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

function animateCylinder() {
    requestAnimationFrame(animateCylinder);
    renderCylinder();
}

function renderCylinder() {
    mesh.rotation.y += 0.01;
    mesh.rotation.x += 0.01;
    renderer.render(scene, camera);


}
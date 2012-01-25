function showShape(url, containerId) {
    var container = $('#' + containerId);
    var containerWidth = $(container).width();
    camera = new THREE.PerspectiveCamera(60, containerWidth / window.innerHeight, 1, 2000);
    camera.position.y = -6;
    camera.position.z = 1000;

    scene = new THREE.Scene();

    $.getJSON(url, function (data) {
        loader = new THREE.JSONLoader();
        loader.createModel(data, function (geometry) {
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
<!doctype html>
<html lang="en">
<head>
    <title>three.js canvas - geometry - terrain</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">
    <style>
    body {
        color: #71544e;
        font-family: Monospace;
        font-size: 13px;
        text-align: center;

        background-color: #bfd1e5;
        margin: 0px;
        overflow: hidden;
    }

    #info {
        position: absolute;
        top: 0px;
        width: 100%;
        padding: 5px;
    }

    a {

        color: #b07861;
    }

    </style>
</head>

<body>

<div id="container"><br/><br/><br/><br/><br/>Generating...</div>

<div id="info"><a href="http://github.com/mrdoob/three.js" target="_blank">three.js</a> - terrain demo. <a href="canvas_geometry_terrain.html">generate another</a></div>

<script src="${resource(dir: 'data/three.js/build', file: 'Three.js')}"></script>
<script src="${resource(dir: 'data/three.js/examples/js', file: 'ImprovedNoise.js')}"></script>
<script src="${resource(dir: 'data/three.js/examples/js', file: 'RequestAnimationFrame.js')}"></script>
<script src="${resource(dir: 'data/three.js/examples/js', file: 'Stats.js')}"></script>

<script>

    var camera, scene, renderer,
            loader, mesh;

    init();
    animate();

    function init() {

        var container = document.getElementById('container');

        camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 2000);
        camera.position.y = -6;
        camera.position.z = 100;

        scene = new THREE.Scene();

        loader = new THREE.JSONLoader();
        loader.load('data/three.js/examples/obj/WaltHeadLo.js', function (geometry) {

            mesh = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
            scene.add(mesh);

        });

        renderer = new THREE.CanvasRenderer();
        renderer.setSize(window.innerWidth, window.innerHeight);
        container.appendChild(renderer.domElement);

    }

    //

    function animate() {

        requestAnimationFrame(animate);
        render();

    }

    function render() {

        var time = new Date().getTime() * 0.0005;
/*
        if (mesh) {

            mesh.rotation.x -= 0.005;
            mesh.rotation.y -= 0.01;

        }*/

        renderer.render(scene, camera);

    }

</script>

</body>
</html>
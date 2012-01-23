<!doctype html>
<html lang="en">
<head>
    <title>index</title>
    <meta name="layout" content="main"/>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0">

</head>

<body>
<jq:resources/>
<script src="${resource(dir: 'js/three', file: 'Three.js')}"></script>
<script src="${resource(dir: 'js/three', file: 'ImprovedNoise.js')}"></script>
<script src="${resource(dir: 'js/three', file: 'RequestAnimationFrame.js')}"></script>
<script src="${resource(dir: 'js', file: '3dViewer.js')}"></script>
<script src="${resource(dir: 'js', file: 'cube.js')}"></script>
<script src="${resource(dir: 'js', file: 'cylinder.js')}"></script>
<script src="${resource(dir: 'js', file: 'sphere.js')}"></script>

<body>
<div id="create" class="content scaffold-create" role="main">
    <div id="content">

    </div>
    <script type="text/javascript">

        var camera, scene, renderer, loader, mesh;

        init();
        animate();

        function init() {

            var container = $('#content');

            camera = new THREE.PerspectiveCamera(60, window.innerWidth / window.innerHeight, 1, 2000);
            camera.position.y = -6;
            camera.position.z = 1000;

            scene = new THREE.Scene();

            var url = "${createLink(controller: 'util',action:'fetchBox')}";
            $.getJSON(url, function (data) {
                loader = new THREE.JSONLoader();
                loader.createModel(data, function (geometry) {
                    mesh = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
                    scene.add(mesh);
                })

                renderer = new THREE.CanvasRenderer();
                renderer.setSize(window.innerWidth, window.innerHeight);
                $(container).html(renderer.domElement);
            })

        }

        //

        function animate() {
            requestAnimationFrame(animate);
            render();
        }

        function render() {
            var time = new Date().getTime() * 0.0005;

            if (mesh) {
                mesh.rotation.x -= 0.005;
                mesh.rotation.y -= 0.01;
            }

            renderer.render(scene, camera);
        }

    </script>
</div>
</body>
</body>
</html>

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
            camera.position.z = 100;

            scene = new THREE.Scene();
            var data = {
                "metadata":{ "formatVersion":3, "generatedBy":"tog" },
                "scale":1.000,
                "materials":[],
                "vertices":[ -5.0, 5.0, -5.0, 5.0, 5.0, -5.0, 5.0, 5.0, 5.0, -5.0, 5.0, 5.0, -5.0, -5.0, -5.0, -5.0, -5.0, 5.0, -5.0, 5.0, 5.0, -5.0, 5.0, -5.0, -5.0, -5.0, -5.0, -5.0, 5.0, -5.0, 5.0, 5.0, -5.0, 5.0, -5.0, -5.0, -5.0, -5.0, -5.0, 5.0, -5.0, -5.0, 5.0, -5.0, 5.0, -5.0, -5.0, 5.0, -5.0, -5.0, 5.0, -5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, -5.0, 5.0, 5.0, -5.0, -5.0, 5.0, -5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, -5.0],
                "morphTargets":[],
                "normals":[ 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
                    -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
                    0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
                    1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
                    0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
                    0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1  ],
                "colors":[ 0xffffff],
                "uvs":[
                    []
                ],
                "faces":[ 3, 2, 1, 3, 1, 4, 8, 6, 7, 5, 6, 8, 10, 11, 9, 9, 11, 12, 14, 15, 13, 13, 15, 16, 19, 18, 17, 19, 17, 20, 22, 24, 23, 22, 21, 24],
                "edges":[]
            }

            loader = new THREE.JSONLoader();
            loader.createModel(data, function (geometry) {
                mesh = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
                scene.add(mesh);
            })

            renderer = new THREE.CanvasRenderer();
            renderer.setSize(window.innerWidth, window.innerHeight);
            $(container).html(renderer.domElement);

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
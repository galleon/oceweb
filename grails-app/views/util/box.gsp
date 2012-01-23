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

        var camera, scene, renderer, loader, mesh, container;
        var targetRotation = 0;
        var targetRotationOnMouseDown = 0;

        var mouseX = 0;
        var mouseXOnMouseDown = 0;
        var windowHalfX, windowHalfY, containerHeight;
        init();

        function init() {

            container = $('#content');
            containerWidth = $(container).width();
            containerHeight = $(container).height();
            windowHalfX = containerWidth;
            windowHalfY = containerHeight;
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

                $(container).bind('mousedown', onDocumentMouseDown);
                $(container).bind('touchstart', onDocumentTouchStart);
                $(container).bind('touchmove', onDocumentTouchMove);
                animate();

            })

        }

        //

        function animate() {
            requestAnimationFrame(animate);
            render();
        }

        function render() {
            if (mesh) {
                mesh.rotation.y += ( targetRotation - mesh.rotation.y ) * 0.05;
            }
            if (renderer) {
                renderer.render(scene, camera);
            }
        }

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
    </script>
</div>
</body>
</body>
</html>

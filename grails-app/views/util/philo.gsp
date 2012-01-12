<html>

<head>
    <title>Learning WebGL &mdash; lesson 1</title>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <meta name="layout" content="main"/>
    <jq:resources/>
    <script src="${resource(dir: 'js/philo', file: 'PhiloGL.js')}"></script>
    <script id="shader-fs" type="x-shader/x-fragment">
        #ifdef GL_ES
        precision highp float;
        #endif

        void main(void) {
        gl_FragColor = vec4(1.0, 1.0, 1.0, 1.0);
        }
    </script>

    <script id="shader-vs" type="x-shader/x-vertex">
        attribute vec3 aVertexPosition;

        uniform mat4 uMVMatrix;
        uniform mat4 uPMatrix;

        void main(void) {
        gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition, 1.0);
        }
    </script>
    <script type="text/javascript">
        function webGLStart() {
            PhiloGL('canvasId', {
                program:{
                    from:'ids',
                    vs:'shader-vs',
                    fs:'shader-fs'
                },
                onError:function () {
                    alert("An error ocurred while loading the application");
                },
                onLoad:function (app) {
                    var gl = app.gl,
                            canvas = app.canvas,
                            program = app.program,
                            camera = app.camera;

                    gl.viewport(0, 0, canvas.width, canvas.height);
                    gl.clearColor(0, 0, 0, 1);
                    gl.clearDepth(1);
                    gl.enable(gl.DEPTH_TEST);
                    gl.depthFunc(gl.LEQUAL);

                    program.setBuffers({
                        'triangle':{
                            attribute:'aVertexPosition',
                            value:new Float32Array([0, 1, 0, -1, -1, 0, 1, -1, 0]),
                            size:3
                        },

                        'square':{
                            attribute:'aVertexPosition',
                            value:new Float32Array([1, 1, 0, -1, 1, 0, 1, -1, 0, -1, -1, 0]),
                            size:3
                        }
                    });

                    gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
                    camera.view.id();
                    //Draw Triangle
                    camera.view.$translate(-1.5, 0, -7);
                    program.setUniform('uMVMatrix', camera.view);
                    program.setUniform('uPMatrix', camera.projection);
                    program.setBuffer('triangle');
                    gl.drawArrays(gl.TRIANGLES, 0, 3);

                    //Draw Square
                    camera.view.$translate(3, 0, 0);
                    program.setUniform('uMVMatrix', camera.view);
                    program.setUniform('uPMatrix', camera.projection);
                    program.setBuffer('square');
                    gl.drawArrays(gl.TRIANGLE_STRIP, 0, 4);
                }
            });

        }
        $(document).ready(function () {
            webGLStart();
        })
    </script>
</head>


<body>
<canvas id="canvasId" style="border: none;" width="500" height="500"></canvas>
</body>

</html>

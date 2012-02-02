var httpData = $.httpData || function (xhr, type, s) { // lifted from jq1.4.4
    var ct = xhr.getResponseHeader("content-type") || "",
        xml = type === "xml" || !type && ct.indexOf("xml") >= 0,
        data = xml ? xhr.responseXML : xhr.responseText;

    if (xml && data.documentElement.nodeName === "parsererror") {
        $.error("parsererror");
    }

    if (s && s.dataFilter) {
        data = s.dataFilter(data, type);
    }

    if (typeof data === "string") {
        if (type === "json" || !type && ct.indexOf("json") >= 0) {
            data = $.parseJSON(data);
        } else if (type === "script" || !type && ct.indexOf("javascript") >= 0) {
            $.globalEval(data);
        }
    }
    return data;
};

jQuery("#spinner").ajaxStart(function () {
    jQuery(this).show();
});
jQuery("#spinner").ajaxStop(function () {
    jQuery(this).hide();
});
jQuery.ajaxSetup({cache:false});
jQuery("#ajax_spinner").ajaxComplete(function (event, xhr, options) {
    var data = httpData(xhr, options.dataType, options);
    var inputFieldIndex = -1;
    try {
        inputFieldIndex = data.indexOf("Session TimedOut url");
    } catch (err) {
    }
    if (inputFieldIndex > -1) {
        window.location.href = data.substring(data.indexOf("=") + 1, data.length);
    }
});
$(document).ready(function () {
    $(".shapeForm").submit(function () {
        showShape($(this).attr('action'), 'content', $(this).serialize(), {closePopup:true, reloadProjectTree:true});
        animate();
        return false
    })

})

var camera, mesh, renderer, containerWidth, containerHeight, scene, container;
var targetRotation = 0;
var targetRotationOnMouseDown = 0;

var mouseX = 0;
var mouseXOnMouseDown = 0;
var windowHalfX, windowHalfY;
function showShape(url, containerId, data, options) {
    container = $('#' + containerId);
    containerWidth = $(container).width();
    containerHeight = $(container).height();
    windowHalfX = containerWidth;
    windowHalfY = containerHeight;
    camera = new THREE.PerspectiveCamera(70, containerWidth / containerHeight, 1, 1000);
    camera.position.y = 150;
    camera.position.z = 500;
    scene = new THREE.Scene();

    scene.add(camera);

    $.getJSON(url, data, function (response) {
        var loader = new THREE.JSONLoader();
        loader.createModel(response, function (geometry) {
            mesh = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
            scene.add(mesh);
        })

        renderer = new THREE.CanvasRenderer();
        renderer.setSize(containerWidth, containerHeight);
        $(container).html(renderer.domElement);
        $(container).bind('mousedown', onDocumentMouseDown);
        $(container).bind('touchstart', onDocumentTouchStart);
        $(container).bind('touchmove', onDocumentTouchMove);
        if (options.closePopup) {
            $.nmTop().close();
        }
        if (options.reloadProjectTree) {
            reloadProjectTree();
        }
    })

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

function reloadProjectTree() {
    var url = createLink('project', 'listCadObjects')
    var projectId = $("#projectId").val();
    url = url + "/" + projectId;
    $.get(url, function (response) {
        if (response.error) {
            alert(response.error)
        } else {
            $("#projectTree").html(response);
        }
    })
}

function createLink(controller, action) {
    var link = window.location.pathname + controller + "/" + action;
    return link
}

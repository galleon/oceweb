var allObjects = {};
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
    $("#selectProject").change(function () {
        $("#changeProject").submit();
    });
    $("#toolbar").draggable();
    $("#projectTree").draggable();
    initialiseCanvas('content');
})

var stats, camera, mesh, renderer, containerWidth, containerHeight, scene, container, projector;
var targetRotation = 0;
var targetRotationY = 0;
var targetRotationOnMouseDown = 0;
var targetRotationYOnMouseDown = 0;

var mouseX = 0;
var mouseY = 0;
var mouseXOnMouseDown = 0;
var mouseYOnMouseDown = 0;
var windowHalfX, windowHalfY;

function showShape(url) {
    var object = allObjects[url];
    if (object) {
        object.visible = true;
        mesh = object;
        addToScene();
    } else {
        showShapeFromRemote(url);
    }
    animate();
}

function showShapeFromRemote(url) {
    $.getJSON(url, {cache:true}, function (response) {
        if (response.error) {
            alert(response.error)
        } else {
            setMesh(response);
            allObjects[url] = mesh;
            addToScene();
        }
    });
}

function setMesh(response) {
    targetRotation = 0;
    var loader = new THREE.JSONLoader();
    loader.createModel(response, function (geometry) {
        mesh = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
    })

}

function addToScene() {
    mesh.visible = true;
    scene.add(mesh);
    $(container).bind('mousedown', onDocumentMouseDown);
    $(container).mousewheel(zoom);
}

function initialiseCanvas(containerId) {
    container = $('#' + containerId);
    containerWidth = window.innerWidth;
    containerHeight = window.innerHeight;
    windowHalfX = containerWidth / 2;
    windowHalfY = containerHeight / 2;
    camera = new THREE.PerspectiveCamera(50, containerWidth / containerHeight, 1, 1000);
    camera.position.x = 0;
    camera.position.y = 0;
    camera.position.z = 500;
    projector = new THREE.Projector();
    renderer = new THREE.CanvasRenderer();
    renderer.setSize(containerWidth, containerHeight);
    $(container).html(renderer.domElement);
    scene = new THREE.Scene();
    scene.add(camera);
}

function zoom(event, delta, deltaX, deltaY) {
    event.preventDefault();
    if (delta != 0) {
        camera.translateZ(-(delta * 10))
    }
}
function onDocumentMouseDown(event) {
    event.preventDefault();

    var vector = new THREE.Vector3(( event.clientX / containerWidth ) * 2 - 1, -( event.clientY / containerHeight ) * 2 + 1, 0.5);
    projector.unprojectVector(vector, camera);
    var ray = new THREE.Ray(camera.position, vector.subSelf(camera.position).normalize());

    var intersects = ray.intersectScene(scene);
    mesh = findObjectById(intersects[ 0 ].object.id);
    $(container).bind('mousemove', onDocumentMouseMove);
    $(container).bind('mouseup', onDocumentMouseUp);
    $(container).bind('mouseout', onDocumentMouseOut);
    mouseXOnMouseDown = event.clientX - windowHalfX;
    mouseYOnMouseDown = event.clientY - windowHalfY;
    targetRotationOnMouseDown = targetRotation;
}

function onDocumentMouseMove(event) {
    mouseX = event.clientX - windowHalfX;
    mouseY = event.clientY - windowHalfY;
    targetRotation = targetRotationOnMouseDown + ( mouseX - mouseXOnMouseDown ) * 0.01;
    targetRotationY = targetRotationYOnMouseDown + ( mouseY - mouseYOnMouseDown ) * 0.01;
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

//

function animate() {
    requestAnimationFrame(animate);
    render();
    stats.update();
}

function render() {
    if (mesh) {
        mesh.rotation.y += ( targetRotation - mesh.rotation.y ) * 0.1;
        mesh.rotation.x += ( targetRotationY - mesh.rotation.x ) * 0.1;
    }
    if (renderer) {
        renderer.render(scene, camera);
    }

}

function createLink(controller, action) {
    var link = '/';
    var path = window.location.pathname;
    if (path.indexOf('threedViewer') == 1) {
        link = '/threedViewer' + link;
    }
    link = link + controller + '/' + action;
    return link
}

function enableJsTree() {
    $("#project").jstree({rules:{ multiple:"shift" }, "plugins":["themes", "html_data", "ui", "crrm", "hotkeys", "contextmenu"], "themes":{"theme":"apple",
        "icons":true}, contextmenu:{items:defaultMenu}, "core":{ "initially_open":[ "phtml_1" ] }}).bind("select_node.jstree",
        function (event, data) {
            if ($('#project').jstree('get_selected').size() == 1) {
                showShape(data.rslt.obj.children().filter('a').attr('href'))
            }

        }).bind("rename_node.jstree", function (event, data) {
            var id = data.rslt.obj.children().filter('a').attr('id');
            var name = data.rslt.name;
            updateName(id, name)
        })
}

function defaultMenu(node) {
    var items = {
        toggleVisibility:{
            label:"Toggle visibility",
            "action":toggleVisibility,
            "_class":"class",
            "separator_before":false,
            "separator_after":true
        },
        deleteNode:{
            label:"Delete",
            "action":deleteObject,
            "_class":"class",
            "separator_before":false,
            "separator_after":true
        },
        edit:{
            label:"Edit",
            "_class":"class",
            "action":function (obj) {
                this.rename(obj);
            },
            "separator_before":false,
            "separator_after":true
        },
        explode:{
            label:"Explode",
            "_class":"class",
            "action":function (obj) {
                var id = $(obj).children().filter('a').attr('id');
                $("#cadObjectId").val(id);
                $("#explodeLink").click();

            },
            "separator_before":false,
            "separator_after":true
        },
        mesh:{
            label:"Mesh",
            "_class":"class",
            "separator_before":false
        }
    };
    var selectedId = $(node).attr('id');
    if (selectedId == "phtml_1") {
        items = {}
    }
    if ($('#project').jstree('get_selected').size() == 2) {
        items = {
            toggleVisibility:items.toggleVisibility,
            deleteNode:items.deleteNode,
            booleanOperation:{
                label:"Boolean operation",
                "action":operation
            }
        };

    }
    if ($('#project').jstree('get_selected').size() > 2) {
        items = {
            toggleVisibility:items.toggleVisibility,
            deleteNode:items.deleteNode
        };

    }
    return items;
}

function toggleVisibility(node) {
    var url = $(node).children().filter('a').attr('href');
    var object = allObjects[url];
    if (object) {
        if (object.visible) {
            object.visible = false;
        } else {
            object.visible = true;
        }
    } else {
        showShape(url);
    }
}

function deleteObject(node) {
    var url = createLink('CADObject', 'delete');
    var ids = [];
    $.each($(node).children().filter('a'), function () {
        ids.push("ids=" + $(this).attr('id'));
    });
    url = url + "?" + ids.join("&");
    $.post(url, function (response) {
        if (response.success) {
            $(node).remove();
            alert(response.success)
        } else {
            alert(response.error)
        }
    });
}

function updateName(id, name) {
    var url = createLink('CADObject', 'updateName');
    url = url + "/" + id + "?name=" + name;
    $.post(url);
}

function operation(obj) {
    var id = $(obj).children().filter('a').attr('id');
    $("#operationLink").click();
    $.each($('#project').jstree('get_selected').children().filter('a'), function (index, value) {
        var count = index + 1;
        var id = $(value).attr('id');
        var text = $(value).text();
        $("#object" + count).val(id);
        $("#obj_" + count).text(text);
    })
}

function findObjectById(id) {
    var object;
    for (var currentObject in allObjects) {
        if (allObjects[currentObject].id == id) {
            object = allObjects[currentObject]
        }
    }
    return object
}
function debugStatement(msg) {
    if (typeof(console) != 'undefined') {
        console.debug(msg);
    }
}
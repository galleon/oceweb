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


$(document).ready(function () {
    $("#selectProject").change(function () {
        $("#changeProject").submit();
    });
    $("#projectTree").draggable();
    initialiseCanvas('content');

    jQuery("#spinner").ajaxStart(function () {
        jQuery(this).show();
    });
    jQuery("#spinner").ajaxStop(function () {
        jQuery(this).hide();
    });
    jQuery.ajaxSetup({cache:true});
    jQuery("#spinner").ajaxComplete(function (event, xhr, options) {
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
    removeErrorMessage();
    removeFlashMessage();
})

var stats, camera, renderer, containerWidth, containerHeight, scene, container;
var targetRotation = 0;
var targetRotationY = 0;
var targetRotationOnMouseDown = 0;
var targetRotationYOnMouseDown = 0;
var group;
var updateTimer = 0;
var mouseX = 0;
var mouseY = 0;
var fov = 50;
var mouseXOnMouseDown = 0;
var mouseYOnMouseDown = 0;
var windowHalfX, windowHalfY;

function showShape(url) {
    var object = group.getChildByName(url);
    if (object) {
        object.visible = true;
    } else {
        showShapeFromRemote(url);
    }
    animate();
}

function showShapeFromRemote(url) {
    $.getJSON(url, function (response) {
        if (response.error) {
            alert(response.error)
        } else {
            var object = createMesh(response, url);
            addToGroup(object);
        }
    });
}

function createMesh(response, name) {
    targetRotation = 0;
    var object;
    var loader = new THREE.JSONLoader();
    loader.createModel(response, function (geometry) {
        object = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
        object.updateMatrix();
    })
    object.name = name;
    return object
}

function addToGroup(object) {
    object.visible = true;
    group.add(object);
    $(container).bind('mousedown', onDocumentMouseDown);
    $(container).mousewheel(zoom);
}

function initialiseCanvas(containerId) {
    container = $('#' + containerId);
    containerWidth = window.innerWidth;
    containerHeight = window.innerHeight;
    windowHalfX = containerWidth / 2;
    windowHalfY = containerHeight / 2;

    camera = new THREE.PerspectiveCamera(fov, containerWidth / containerHeight, 1, 1000);
    camera.target = new THREE.Vector3();
    camera.position.x = 0;
    camera.position.y = 0;
    camera.position.z = 500;

    group = new THREE.Object3D();

    scene = new THREE.Scene();
    scene.add(camera);
    scene.add(group);


    renderer = new THREE.CanvasRenderer();
    renderer.setSize(containerWidth, containerHeight);
    renderer.sortObjects = false;

    $(container).html(renderer.domElement);
}

function zoom(event, delta, deltaX, deltaY) {
    event.preventDefault();
    if (delta != 0) {
        fov -= deltaY * 2;
        camera.projectionMatrix = THREE.Matrix4.makePerspective(fov, containerWidth / containerHeight, 1, 1100);
    }
}
function onDocumentMouseDown(event) {
    event.preventDefault();

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
    group.rotation.y += ( targetRotation - group.rotation.y ) * 0.1;
    group.rotation.x += ( targetRotationY - group.rotation.x ) * 0.1;

    if (renderer) {
        renderer.render(scene, camera);
    }

}

function createLink(controller, action) {
    var link = getContext() + controller + '/' + action;
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
                var id = $(obj).children().filter('a').attr('id');
                $("#cadObjectId").val(id);
                var url = createLink('CADObject', 'edit');
                url = url + "/" + id;
                $.post(url, function(response){
                    editShape(response)
                });
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
            "action":function (obj) {
                var id = $(obj).children().filter('a').attr('id');
                $("#meshForm #cadObjectId").val(id);
                $("#meshLink").click();
            },
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

function editShape(shape){
    $("#"+shape.type.toLowerCase()+"Form "+ "#name").val(shape.name)
    $("#"+shape.type.toLowerCase()+"Form "+ "#x").val(shape.x)
    $("#"+shape.type.toLowerCase()+"Form "+ "#y").val(shape.y)
    $("#"+shape.type.toLowerCase()+"Form "+ "#z").val(shape.z)
    debugStatement("#"+shape.type.toLowerCase()+"Form"+ "#name")
    $("#"+shape.type).click()
}

function toggleVisibility(node) {
    var url = $(node).children().filter('a').attr('href');
    var object = group.getChildByName(url);
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
    var objectIds = [];
    $.each($('#project').jstree('get_selected').children().filter('a'), function () {
        var id = $(this).attr('id');
        ids.push("ids=" + id);
        objectIds.push(id)
    });
    $.each($(node).children().filter('a'), function () {
        var id = $(this).attr('id');
        ids.push("ids=" + id);
        objectIds.push(id)
    });
    if (ids.length > 0) {
        url = url + "?" + ids.join("&");
        $.post(url, function (response) {
            if (response.success) {
                $('#project').jstree('get_selected').remove();
                $(node).remove();
                removeObjects(objectIds)
                alert(response.success)
            } else {
                alert(response.error)
            }
        });
    }
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

function debugStatement(msg) {
    if (typeof(console) != 'undefined') {
        console.debug(msg);
    }
}
function removeErrorMessage() {
    updateTimer = setTimeout('jQuery("#instanceErrors").remove()', 2000);
}
function removeFlashMessage() {
    updateTimer = setTimeout('jQuery("#flashError").remove()', 2000);
}

function removeObjects(ids) {
    $.each(ids, function (index, value) {
        var url = createLink('CADObject', 'show') + "/" + value;
        var object = group.getChildByName(url);
        if (object) {
            group.remove(object)
        }
    })
}

function getContext(){
    var context = '/';
    var path = window.location.pathname;
    if (path.indexOf('threedViewer') == 1) {
        context = '/threedViewer' + context;
    }
    return context
}
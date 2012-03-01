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
    $("#toolbox").draggable();
    initialiseCanvas('content');

    jQuery("#spinner").ajaxStart(function () {
        jQuery(this).show();
    });
    jQuery("#spinner").ajaxStop(function () {
        jQuery(this).hide();
    });
    setupUI();
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
    enableJsTree();
    closeModel();
    $("input:submit,input:button, button,a.modelLink").button();
    $(".model").click(function () {
        if ($(this).hasClass('ajax')) {
            var url = $(this).attr("href")
            var title = $(this).attr("title")
            $.post(url, function (response) {
                $("#templateHolder").html(response);
                $("#templateHolder").dialog({title:title});
                setupUI();
                ajaxSubmit();
            });
            return false;
        }
        else {
            $($(this).attr('href')).dialog();
            $('.ui-dialog').css('padding-top', '8px').width('340px');
        }
    })
})

function setupUI() {
    $('.ui-dialog').css('padding-top', '8px').width('340px');
    $("input:submit,input:button, button,a.modelLink").button();
    closeModel();
}

function closeModel() {
    $(".closeModel").click(function () {
        $(".ui-icon-closethick").click();
    })
}
var stats, camera, renderer, containerWidth, containerHeight, scene, container, trihedra;
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

function showShape(id) {
    var object = group.getChildByName(id);
    if (object) {
        object.visible = true;
        object.doubleSided = true;
    } else {
        showShapeFromRemote(id);
    }
    animate();
}

function showShapeFromRemote(id) {
    if (id) {
        var url = createLink('CADObject', 'show');
        $.getJSON(url, {id:id}, function (response) {
            if (response.error) {
                showError(response.error)
            } else {
                var object = createMesh(response, id);
                addToGroup(object);
            }
        });
    }
}

function showError(message) {
    $("#dialog-confirm p").html(message);
    $("#dialog-confirm").dialog({title:'Error'});
}

function createMesh(response, name) {
    var object;
    var loader = new THREE.JSONLoader();
    loader.createModel(response, function (geometry) {
        object = new THREE.Mesh(geometry, new THREE.MeshNormalMaterial({ overdraw:true }));
        object.updateMatrix();
    })
    object.doubleSided = true;
    object.name = name;
    return object
}

function addToGroup(object) {
    object.visible = true;
    group.add(object);
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
    renderer = new THREE.CanvasRenderer();
    renderer.setSize(containerWidth, containerHeight);
    renderer.sortObjects = false;

    trihedra = new THREE.Axes();
    trihedra.position.set(0, 0, 0);
    trihedra.scale.x = trihedra.scale.y = trihedra.scale.z = 0.5;
    trihedra.rotation.y = 0;
    addToGroup(trihedra);
    scene.add(group);
    $(container).html(renderer.domElement);
    $(container).bind('mousedown', onDocumentMouseDown);
    $(container).mousewheel(zoom);
    stats = new Stats();
    $("#frameArea").append($(stats.domElement).find('div>div:first').css({'float':'right', 'margin-right':'13px', 'padding-top':'6px'}));
    animate();
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

function ajaxSubmit() {
    $(".shapeForm").ajaxForm(function (response) {
        $("#templateHolder").dialog("close")
        if (response.error) {
            showError(response.error)
        }
        else {
            reloadProjectTree()
            removeObjects([response])
            showShape(response)
        }
    })
}

function createLink(controller, action) {
    var link = getContext() + controller + '/' + action;
    return link
}

function enableJsTree() {
    $("#project").jstree({
        rules:{
            multiple:"shift"
        },
        "plugins":["themes", "html_data", "ui", "crrm", "hotkeys", "contextmenu", "types", "dnd"],
        "themes":{
            "theme":"apple",
            "icons":true
        },
        contextmenu:{
            items:defaultMenu
        },
        "core":{
            "initially_open":[ "phtml_1" ]
        },
        "types":{
            "types":{
                "cone":{
                    "icon":{
                        "image":"images/cone.png"
                    }
                },
                "cube":{
                    "icon":{
                        "image":"images/cube.png"
                    }
                },
                "cylinder":{
                    "icon":{
                        "image":"images/cylinder_icon.png"
                    }
                },
                "sphere":{
                    "icon":{
                        "image":"images/sphere_icon.png"
                    }
                },
                "default":{
                    "icon":{
                        "image":"images/folder_icon.png"
                    }
                }
            }
        }
    }).
        bind("select_node.jstree",
        function (event, data) {
            if ($('#project').jstree('get_selected').size() == 1) {
                showShape(data.rslt.obj.children().filter('a').attr('id'))
            }

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
            "action":confirmDelete,
            "_class":"class",
            "separator_before":false,
            "separator_after":true
        },
        edit:{
            label:"Edit",
            "_class":"class",
            "action":function (obj) {
                var anchor = $(obj).children().filter('a');
                var url = $(anchor).attr('rel');
                var title = $(anchor).attr('title');
                $.post(url, function (response) {
                    $("#templateHolder").html(response);
                    $("#templateHolder").dialog({title:title});
                    setupUI();
                    ajaxSubmit();
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
                $("#explode").dialog();

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
                $("#meshForm #cadProjectId").val($("#projectid").val());
                $("#mesh").dialog();
                $('.ui-dialog').width('340px');
            },
            "separator_before":false
        }
    };
    var selectedId = $(node).attr('id');
    if (selectedId == "phtml_1") {
        items = {deleteNode:{
            label:"Delete",
            "action":deleteProject,
            "_class":"class",
            "separator_before":false,
            "separator_after":true
        } }
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
    var id = $(node).children().filter('a').attr('id');
    var object = group.getChildByName(id);
    if (object) {
        if (object.visible) {
            object.visible = false;
        } else {
            object.visible = true;
        }
    } else {
        showShape(id);
    }
}

function getSelectedObjects(node) {
    var objects = [];
    $.each($('#project').jstree('get_selected').children().filter('a'), function () {
        objects.push($(this))
    });
    $.each($(node).children().filter('a'), function () {
        objects.push($(this))
    });
    return $.unique(objects);
}

function updateName(id, name) {
    var url = createLink('CADObject', 'updateName');
    url = url + "/" + id + "?name=" + name;
    $.post(url);
}

function operation(obj) {
    var id = $(obj).children().filter('a').attr('id');
    $("#operation").dialog();
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

function removeObjects(ids) {
    $.each(ids, function (index, value) {
        var object = group.getChildByName(value);
        if (object) {
            group.remove(object)
        }
    })
}

function getContext() {
    var context = '/';
    var path = window.location.pathname;
    if (path.indexOf('threedViewer') == 1) {
        context = '/threedViewer' + context;
    }
    return context
}
function deleteProject() {
    $("#confirmDeleteProject").show().removeClass('hidden').dialog();
}

function confirmDelete(node) {
    var selectedObjects = getSelectedObjects(node);
    var names = [];
    var ids = [];
    var idVars = [];
    $(selectedObjects).each(function (index, value) {
        var id = $(value).attr('id');
        if (ids.indexOf(id) < 0) {
            names.push($(value).text());
            ids.push(id);
            idVars.push("ids=" + id);
        }
    });
    $("#dialog-confirm p").html("Are you sure you want to delete" + names.join(", ") + "?")
    $("#dialog-confirm").dialog({
        buttons:{
            "Delete all items":function () {
                var model = $(this);
                if (ids.length > 0) {
                    var url = createLink('CADObject', 'delete');
                    url = url + "?" + idVars.join("&");
                    $.post(url, function (response) {
                        if (response.success) {
                            $('#project').jstree('get_selected').remove();
                            $(node).remove();
                            removeObjects(ids);
                            $(model).dialog("close");
                        } else {
                            $("#dialog-confirm p").html(response.error);
                        }
                    });
                }
            },
            Cancel:function () {
                $(this).dialog("close");
            }
        }
    })
}

function reloadProjectTree() {
    var url = createLink('project', 'listCadObjects')
    var projectId = $("#projectId").val();
    url = url + "/" + projectId;
    $.get(url, function (response) {
        if (response.error) {
            showError(response.error)
        } else {
            $("#projectTree").html(response);
            $("#projectTree").draggable();
            enableJsTree()
        }
    })
}
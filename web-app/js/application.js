var group, stats, camera, renderer, containerWidth, containerHeight, scene, container, trihedra, projector, windowHalfX, windowHalfY;
var targetRotation = 0;
var targetRotationY = 0;
var targetRotationOnMouseDown = 0;
var targetRotationYOnMouseDown = 0;
var mouseX = 0;
var mouseY = 0;
var fov = 50;
var mouseXOnMouseDown = 0;
var mouseYOnMouseDown = 0;
var objectColor = 0x545354;
var selectionColor = 0xff0000;
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
    $("#closeFlash").click(function () {
        hideFlashMessage();
    });
    $("#selectProject").change(function () {
        $("#changeProject").submit();
    });
    $("#projectTree").draggable();
    $("#toolbox").draggable();
    enableJsTree();
    initialiseCanvas('content');
    showShapeFromLocalStorage();
    jQuery("#spinner").ajaxStart(function () {
        showFlashInfo('Please wait while you content is loading')
    });
    jQuery("#spinner").ajaxStop(function () {
        hideFlashMessage();
    });
    setupUI();
    ajaxSubmit();
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
            $('.ui-dialog').width('340px');
        }
    })
})

function setupUI() {
    $('.ui-dialog').width('340px');
    $("input:submit,input:button, button,a.modelLink").button();
    closeModel();
}

function closeModel() {
    $(".closeModel").click(function () {
        $(".ui-icon-closethick").click();
    })
}

function showShape(id) {
    if (id) {
        var object = group.getChildByName(id + '');
        if (object) {
            debugStatement("Object found in group -: " + id)
            repaint();
            object.doubleSided = true;
            setColor(object, selectionColor)
        } else {
            debugStatement("Object not found in group -: " + id)
            showShapeFromRemote(id);
        }
    }
    animate();
}

function showShapeFromRemote(id) {
    if (id) {
        var url = createLink('CADObject', 'show');
        debugStatement("Fetching  object from remote -: " + id);
        $.getJSON(url, {id:id}, function (response) {
            if (response.error) {
                showError(response.error)
            } else {
                repaint();
                var object = createMesh(response, id);
                addToGroup(object);
            }
        });
    } else {
        debugStatement("No id passed");
    }

}

function showShapeFromLocalStorage() {
    var objectIDs = getLocalShapeIds();
    if (objectIDs) {
        var projectShapeIds = getProjectShapeIds();
        $.each(objectIDs, function (key, value) {
            $.each(projectShapeIds, function (index, shapeId) {
                if (parseInt(value) == parseInt(shapeId)) {
                    $("#" + value + '').click()
                }
            });
        })
    }
}

function addToGroup(object) {
    var id = object.name;
    var visible = window.localStorage["visible_" + id] != '0';
    setVisible(object, visible);
    group.add(object);
    addToLocalStorage(id);
}

function addToLocalStorage(id) {
    if (id) {
        debugStatement("Found id to add to local storage -: " + id)
        var currentIds = getLocalShapeIds() ? getLocalShapeIds() : [];
        var found = false;
        $.each(currentIds, function (key, value) {
            if ((parseInt(id) == value)) {
                found = true;
            }
        })
        debugStatement("Value of found after reading from local storage is -: " + found)
        if (found == false) {
            debugStatement("Id -: " + id + " not in local storage so adding it")
            currentIds.push(parseInt(id))
            window.localStorage.objectIDs = JSON.stringify(currentIds)
        }
    }
}

function getLocalShapeIds() {
    var ids = window.localStorage.objectIDs ? JSON.parse(window.localStorage.objectIDs) : [];
    var shapeIds = [];
    $(ids).each(function (index, value) {
        if ($.inArray(parseInt(value), shapeIds) == -1) {
            shapeIds.push(parseInt(value))
        }
    });
    debugStatement("Ids on local storage are -: " + $.unique(shapeIds))
    return $.unique(shapeIds);
}

function getLocalColor(id) {
    return window.localStorage["color_" + id]
}

function getProjectShapeIds() {
    var ids = [];
    $.each($(".showObject"), function (index, val) {
        ids.push(parseInt($(val).attr('id')))
    })
    debugStatement("Ids on Project tree are -: " + $.unique(ids))
    return $.unique(ids);
}

function showError(message) {
    $("#dialog-confirm p").html(message);
    $("#dialog-confirm").dialog({title:'Error'});
}

function createMesh(response, name) {
    var object, color;
    var loader = new THREE.JSONLoader();
    loader.createModel(response, function (geometry) {
        if (response.wireframe) {
            geometry = changeFaceOrientation(geometry);
        }
        color = selectionColor;
        var localColor = getLocalColor(name)
        if (localColor) {
            color = localColor
        }
        var material = new THREE.MeshLambertMaterial({overdraw:true, shading:THREE.FlatShading, wireframe:response.wireframe});
        object = new THREE.Mesh(geometry, material);
        object.updateMatrix();
    })
    object.doubleSided = true;
    object.name = name + '';
    setColor(object, color);
    return object
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

    scene = new THREE.Scene();
    scene.add(camera);

    group = new THREE.Object3D();
    group.parent = scene;
    renderer = new THREE.CanvasRenderer();
    renderer.setSize(containerWidth, containerHeight);
    renderer.sortObjects = false;

    trihedra = new THREE.Axes();
    trihedra.position.set(0, 0, 0);
    trihedra.scale.x = trihedra.scale.y = trihedra.scale.z = 0.5;
    trihedra.rotation.y = 0;
    addToGroup(trihedra);

    var ambientColor = 2.0894;
    var ambientLight = new THREE.AmbientLight(ambientColor);
    scene.add(ambientLight);
    projector = new THREE.Projector();
    var directionalLight = new THREE.DirectionalLight(0xffffff);
    directionalLight.position.x = 0.5;
    directionalLight.position.y = 0.75;
    directionalLight.position.z = 2;
    scene.add(directionalLight);

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

    var vector = new THREE.Vector3(( event.clientX / containerWidth ) * 2 - 1, -( event.clientY / containerHeight ) * 2 + 1, 0.5);
    projector.unprojectVector(vector, camera);
    var ray = new THREE.Ray(camera.position, vector.subSelf(camera.position).normalize());

    var intersects = ray.intersectScene(group);
    if (intersects.length > 0) {
        var object = intersects[ 0 ].object;
        if (object.visible) {
            repaint();
            $("#" + object.name).click();
        }
    }
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
            if (response.success) {
                showFlashSuccess(response.success);
            }
            if (response.id) {
                removeObjects([response.id])
                showShape(response.id)
            }
            $(".closeModel").click();
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
                "explode":{
                    "icon":{
                        "image":"images/explode.gif"
                    }
                },
                "mesh":{
                    "icon":{
                        "image":"images/drive.png"
                    }
                },
                "file":{
                    "icon":{
                        "image":"images/file.png"
                    }
                },
                "default":{
                    "icon":{
                        "image":"images/folder.png"
                    }
                }
            }
        }
    }).bind("select_node.jstree",
        function (event, data) {
            if ($('#project').jstree('get_selected').size() == 1) {
                showShape(data.rslt.obj.children().filter('a').attr('id'))
            }

        }).bind("rename.jstree", function (e, data) {
            var id = data.rslt.obj.children().filter('a').attr("id")
            var name = data.rslt.new_name;
            $.post(createLink('CADObject', 'rename'), {id:id, name:name}, function (response) {
                if (response.success) {
                    showFlashSuccess(response.success)
                }
                if (response.error) {
                    showFlashError(response.error)
                }
            })
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
                $("#explodeForm #cadObjectId").val(id);
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
                $("#mesh").dialog();
                $(".ui-dialog").css('width', '410px');
                setupUI();
            },
            "separator_before":false
        }
    };
    var selectedId = $(node).attr('id');
    var rel = $(node).attr('rel');
    if (rel == "MESH") {
        items = {
            toggleVisibility:items.toggleVisibility,
            deleteNode:items.deleteNode,
            edit:{
                label:"Edit",
                "_class":"class",
                "action":function (obj) {
                    $("#project").jstree("rename", '#' + $(obj).attr('id'));
                },
                "separator_before":false,
                "separator_after":true
            }
        }
        var parentMesh = $(node).children().filter('a').hasClass('parent_MESH');
        if (!parentMesh) {
            items['compute'] = {
                label:"Compute",
                "_class":"class",
                "action":function (obj) {
                    var id = $(obj).children().filter('a').attr('id');
                    $("#computeForm #cadObjectId").val(id);
                    $("#compute").dialog();
                    $('.ui-dialog').width('600px');
                },
                "separator_before":false,
                "separator_after":true
            }
        }
    }
    if (selectedId == "phtml_1") {
        items = {deleteNode:{
            label:"Delete",
            "action":deleteProject,
            "_class":"class",
            "separator_before":false,
            "separator_after":true
        } }
    }
    if ($('#project').jstree('get_selected').size() >= 2) {
        items = {
            toggleVisibility:items.toggleVisibility,
            deleteNode:items.deleteNode
        };
        if ($('#project').jstree('get_selected').size() == 2) {
            var hasMesh = false;
            $.each($('#project').jstree('get_selected'), function (index, val) {
                var rel = $(val).attr('rel');
                debugStatement("index " + index + " rel -: " + rel);
                if (!hasMesh && rel == "MESH") {

                    hasMesh = true
                }
            });
            if (!hasMesh) {
                items['booleanOperation'] = {
                    label:"Boolean operation",
                    "action":operation
                }
            }
        }
        var allParentMesh = true;
        $.each($('#project').jstree('get_selected').children().filter('a'), function (index, val) {
            var hasParentMesh = $(val).hasClass('parent_MESH')
            debugStatement("index " + index + " hasParentMesh -: " + hasParentMesh)
            if (allParentMesh && !hasParentMesh) {
                allParentMesh = false
            }
        });
        if (allParentMesh) {
            var url = createLink('CADObject', 'merge');
            items['merge'] = {
                label:"Merge",
                "action":function (obj) {
                    var ids = [];
                    $.each($('#project').jstree('get_selected').children().filter('a'), function (index, val) {
                        ids.push('ids=' + $(val).attr('id'))
                    })
                    window.location = url + "/?" + ids.join('&');
                }
            }
        }
    }
    return items;
}

function toggleVisibility(node) {
    var id = $(node).children().filter('a').attr('id');
    var object = group.getChildByName(id);
    if (object) {
        var color = '';
        var visible;
        if (object.visible) {
            visible = false;
            color = objectColor;
        } else {
            color = selectionColor;
            visible = true;
        }
        setVisible(object, visible);
        setColor(object, color);
    } else {
        showShape(id);
    }
}

function setColor(object, color) {
    if (object.name) {
        debugStatement("Setting color -: " + color + " of id -: " + object.name);
        object.material.color.setHex(color);
        window.localStorage["color_" + object.name] = color;
    }
}

function setVisible(object, visible) {
    if (object.name) {
        debugStatement("Setting visible " + visible + " for -: " + object.name)
        object.visible = visible;
        window.localStorage["visible_" + object.name] = visible ? '1' : '0';
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
    debugStatement("Removing objects with ids -: " + ids)
    $.each(ids, function (index, value) {
        var object = scene.getChildByName(value + '', true);
        if (object) {
            debugStatement("Removing object -: " + value);
            group.remove(object);
            removeFromLocalStorage(value);
        } else {
            debugStatement("Object not found -: " + value);
        }
    })
}

function removeFromLocalStorage(value) {
    localStorage.removeItem("color_" + value);
    localStorage.removeItem("visible_" + value);
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
                            $(model).dialog("close");
                            removeObjects(ids);
                            reloadProjectTree();
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
            enableJsTree();
            setupUI();
            ajaxSubmit();
        }
    })
}

function repaint() {
    debugStatement("Repainting the group");
    $.each($("#phtml_1").children().find('li a'), function () {
        var id = $(this).attr('id');
        debugStatement("Repainting " + id + " to color " + objectColor)
        var object = scene.getChildByName(id + '', true)
        if (object) {
            setColor(object, objectColor);
        } else {
            debugStatement("object not found for coloring -: " + id)
        }
    })
}

function showMessage(message, className) {
    $("#spinner p").text(message);
    $("#spinner").removeClass('errormsg').removeClass('success').removeClass('info').addClass(className).show();
}

function showFlashInfo(message) {
    showMessage(message, 'info');
}

function showFlashSuccess(message) {
    showMessage(message, 'success');
}

function showFlashError(message) {
    showMessage(message, 'errormsg');
}

function hideFlashMessage() {
    $("#spinner").hide();
}

function changeFaceOrientation(geometry) {
    for (var i = 0; i < geometry.faces.length; i++) {
        var face = geometry.faces[ i ];
        if (face instanceof THREE.Face3) {
            var tmp = face.b;
            face.b = face.c;
            face.c = tmp;

        } else if (face instanceof THREE.Face4) {
            var tmp = face.b;
            face.b = face.d;
            face.d = tmp;
        }
    }

    geometry.computeCentroids();
    geometry.computeFaceNormals();
    geometry.computeVertexNormals();
    return geometry
}
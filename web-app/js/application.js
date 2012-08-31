var group, stats, camera, renderer, containerWidth, containerHeight, scene, container, trihedra, projector, windowHalfX, windowHalfY;
var targetRotation = 0;
var targetRotationY = 0;
var targetRotationOnMouseDown = 0;
var targetRotationYOnMouseDown = 0;
var mouseX = 0;
var mouseY = 0;
var mouseXOnMouseDown = 0;
var mouseYOnMouseDown = 0;
var objectColor = 0x545354;
var selectionColor = 0xff0000;
var debug = false;
$(document).ready(function () {
    $("#spinner").ajaxStart(function () {
        showFlashInfo('Please wait while you content is loading')
    });
    $("#spinner").ajaxStop(function () {
        hideFlashMessage();
    });
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
    jQuery.ajaxSetup({cache:true});
    setupUI();
    ajaxSubmit();
    closeModel();
    $(".addContent").live("click", function () {
        var $content = $("#domainContent").clone();
        $(".removeContent", $content).parent().show();
        var $domainName = $("#domainName", $content);
        $domainName.text("Domains");
        $domainName.removeAttr("id");
        $(this).parent().parent().parent().after($content);
        $.each($("input[type='text']", $content), function (inputIndex, inputValue) {
            $(inputValue).val("");
        })
        renameInputFields();
    });
    $(".removeContent").live("click", function () {
        $(this).parent().parent().parent().remove();
        renameInputFields();
    });
    bindShapeCreation();
    bindChangeScale();
});

function setupButtonUI() {
    $("input:submit,input:button, button,a.modelLink").button();
}

function bindChangeScale() {
    $("#changeScaleForm").submit(function () {
        var url = $(this).attr('action') + "?" + $(this).serialize();
        $.post(url, function (response) {
            $.each(getCurrentlyVisibleIds(), function (index, value) {
                showShapeFromRemote(value);
            })
        })
        return false;
    })
}

function bindShapeCreation() {
    $(".model").live("click", function () {
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
    });
}

function renameInputFields() {
    $.each($(".domainContent"), function (index, val) {
        $.each($("input[type='text']", $(val)), function (inputIndex, inputValue) {
            var name = $(inputValue).attr("name");
            name = name.split(".")[0] + "." + index;
            $(inputValue).attr("name", name);
        })
    })
}

function setupUI() {
    $('.ui-dialog').width('340px');
    setupButtonUI();
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
    highlightInProjectTree(id);
}

function highlightInProjectTree(id) {
    $("#" + id).css('border', '1px solid red')
}

function showShapeFromRemote(id) {
    if (id) {
        var url = createLink('CADObject', 'show');
        debugStatement("Fetching  object from remote -: " + id);
        $.getJSON(url, {id:id}, function (response) {
            highlightInProjectTree(id);
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

function addToGroup(object) {
    var id = object.name;
    setVisible(object, true);
    group.add(object);
}

function getLocalColor(id) {
    return window.localStorage["color_" + id]
}

function showError(message) {
    $("#dialog-confirm p").html(message);
    $("#dialog-confirm").dialog({title:'Error'});
}

function createMesh(response, name) {
    var object, color;
    var loader = new THREE.JSONLoader();
    loader.createModel(response, function (geometry) {
                addScaleFactorToLocalStorage(name, response.scalingFactor)
                if (response.simulated) {
                    var material
                    geometry = changeFaceOrientation(geometry);
                    var color, f, f2, f3, p, n, vertexIndex, radius = 10
                    var faceIndices = [ 'a', 'b', 'c', 'd' ];
                    for (var i = 0; i < geometry.faces.length; i++) {
                        f = geometry.faces[ i ];
                        n = ( f instanceof THREE.Face3 ) ? 3 : 4;
                        for (var j = 0; j < n; j++) {
                            vertexIndex = f[ faceIndices[ j ] ];
                            p = geometry.vertices[ vertexIndex ];
                            color = new THREE.Color(0xffffff);
                            var colorValue = response.colors[i];
                            var colorValue1 = response.colors[i];
                            var colorValue2 = response.colors[i];
                            color.setHSV(colorValue, colorValue1, colorValue2)
                            f.vertexColors[ j ] = color;
                        }
                    }
                    material = [
                        new THREE.MeshLambertMaterial({ color:0xffffff, shading:THREE.FlatShading, vertexColors:THREE.VertexColors})
                    ];
                    object = THREE.SceneUtils.createMultiMaterialObject(geometry, material);

                    object.updateMatrix();
                    object.doubleSided = true;
                    object.name = name + '';

                    $.each($("#" + name).parent().parent().children().children().filter('a'), function (index, val) {
                        var objectId = $(val).attr('id')
                        var otherObjects = group.getChildByName(objectId);
                        if (otherObjects != undefined && otherObjects.children != '' && otherObjects.children != undefined && otherObjects.children.length > 0) {
                            if (otherObjects.children[0].visible) {
                                $("#" + objectId).css('border', '0px')
                                otherObjects.children[0].visible = false;
                            }

                        }
                    });

                } else {
                    var material
                    if (response.wireframe) {
                        geometry = changeFaceOrientation(geometry);
                        material = new THREE.MeshLambertMaterial({color:0xffffff, shading:THREE.FlatShading, wireframe:response.wireframe, vertexColors:THREE.VertexColors, opacity:0.75 });
                    } else {
                        material = new THREE.MeshLambertMaterial({overdraw:true, shading:THREE.FlatShading, wireframe:response.wireframe});
                    }
                    color = selectionColor;
                    if (response.color) {
                        color
                                = response.color
                    }
                    var actualColor = response.color ? response.color : objectColor;
                    setActualColorOnLocal(name, actualColor);
                    var localColor = getLocalColor(name);
                    if (localColor) {
                        color = localColor
                    }
                    object = new THREE.Mesh(geometry, material);
                    object.updateMatrix();

                    object.doubleSided = true;
                    object.name = name + '';
                    setColor(object, color);
                }
                var deltaY = trihedra.scale.x < .5 ? -1 : 1;
                for (i = 0; i < getNumberOfRotations(); i++) {
                    zoomObject(object, deltaY)
                }
            }
    )
    return object
}

function getNumberOfRotations() {
    return Math.ceil((trihedra.scale.x - 0.5) / (0.2));
}

function addScaleFactorToLocalStorage(id, value) {
    localStorage["scalingFactor_" + id] = value;
}

function initialiseCanvas(containerId) {
    container = $('#' + containerId);
    containerWidth = window.innerWidth;
    containerHeight = window.innerHeight;
    windowHalfX = containerWidth / 2;
    windowHalfY = containerHeight / 2;

    var fov = 50;
    camera = new THREE.PerspectiveCamera(fov, containerWidth / containerHeight, 1, 1000);
    camera.target = new THREE.Vector3();
    camera.position.x = 0;
    camera.position.y = 0;
    camera.position.z = 500;

    scene = new THREE.Scene();
    scene.add(camera);


    group = new THREE.Object3D();
    group.parent = scene;
    renderer = new THREE.WebGLRenderer({ antialias:true });
    renderer.setSize(window.innerWidth, window.innerHeight);

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
        $.each(group.children, function (index, value) {
            zoomObject(value, deltaY)
        })
    }
}

function zoomObject(object, deltaY) {

    var scalingFactor = localStorage["scalingFactor_" + object.name] ? parseFloat(localStorage["scalingFactor_" + object.name]) : 5;
    var scale = deltaY / scalingFactor;
    scaleObject(object, scale);
}

function scaleObject(object, scale) {
    var newScale = object.scale.x + scale;
    console.debug("scale -: " + scale + " new " + newScale)
    if (newScale > 0) {
        object.scale.x = newScale;
        object.scale.y = newScale;
        object.scale.z = newScale;
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
            debugStatement("Repainting for selection")
            repaint();
            showShape(object.name);
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
            reloadProjectTree();
            if (response.success) {
                showFlashSuccess(response.success);
            }
            if (response.ids) {
                hideAll();
                $.each(response.ids, function (index, value) {
                    showShapeFromRemote(value)
                })
            }
            if (response.id) {
                removeObjects([response.id])
                showShape(response.id)
            }
            $(".closeModel").click();
        }
    })
}

function hideAll() {
    $.each(getCurrentlyVisibleIds(), function (index, value) {
        var object = group.getChildByName(value + "");
        if (object) {
            object.visible = false;
        }
    })
}

function highLightVisible() {
    $.each(getCurrentlyVisibleIds(), function (index, value) {
        highlightInProjectTree(value);
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
    var exportFile = {
        label:"Export",
        "_class":"class",
        "action":function (obj) {
            var id = $(obj).children().filter('a').attr('id');
            window.location = createLink('CADObject', 'export') + "/" + id;
        },
        "separator_before":false,
        "separator_after":true
    }
    if (rel == "SIMULATED") {
        items['edit'] = null;
        items['explode'] = null;
        items['mesh'] = null;
    }
    if (rel == 'result') {
        items = {

        }
    }
    if (rel == "MESH") {
        var parentMeshValue = $(node).children().filter('a').hasClass('parent_MESH');
        items = {

        }

        items['toggleVisibility'] = {
            label:"Toggle visibility",
            "action":toggleVisibility,
            "_class":"class",
            "separator_before":false,
            "separator_after":true
        }

        items['deleteNode'] = {
            label:"Delete",
            "action":confirmDelete,
            "_class":"class",
            "separator_before":false,
            "separator_after":true
        }
        items['edit'] = {
            //  deleteNode:items.deleteNode,
            label:"Edit",
            "_class":"class",
            "action":function (obj) {
                $("#project").jstree("rename", '#' + $(obj).attr('id'));
            },
            "separator_before":false,
            "separator_after":true

        }


        var parentMesh = $(node).children().children().filter('a').hasClass('parent_MESH');
        if (!parentMesh) {

            items['compute'] = {
                label:"Compute",
                "_class":"class",
                "action":function (obj) {
                    var id = $(obj).children().filter('a').attr('id');
                    $.each($("input[type='text']"), function () {
                        $(this).val('');
                    })
                    showComputeForm(id);
                },
                "separator_before":false,
                "separator_after":true
            }
            items['export'] = exportFile
        }
    }
    if (rel == "group") {

        items = {

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
            debugStatement("index " + index + " val " + val + " hasParentMesh -: " + hasParentMesh)
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
                    var objectIds = [];
                    $.each($('#project').jstree('get_selected').children().filter('a'), function (index, val) {
                        var id = $(val).attr('id')
                        ids.push('ids=' + id);
                        objectIds.push(id);
                    })
                    url = url + "/?" + ids.join('&');
                    $.post(url, function (response) {
                        if (response.success) {
                            removeObjects(objectIds);
                            reloadProjectTree();
                        } else {
                            showError(response.error);
                        }
                    });
                }
            }
        }
    }
    return items;
}

function showComputeForm(id) {
    var url = "CADObject/computePage?id=" + id
    $.get(url, function (response) {
        $("#compute").html(response);
        $("#compute").dialog();
        $('.ui-dialog').width('600px');
        $("#computeForm #cadObjectId").val(id);
        ajaxSubmit();
        addRemoveComputeDialog();
        setupButtonUI();
        closeModel();
    });
}

function addRemoveComputeDialog() {
    $(".addComputeDialog").live("click", function () {
        var id = $(this).attr("id");
        var value = parseInt(id) + 1

        var computeId = "compute_" + value;
        if ($("#" + computeId) != 'undefined') {
            $("#" + computeId).css('display', '')
        }
        // $('.ui-dialog').width('600px');
    });
    $(".removeComputeDialog").live("click", function () {
        var id = $(this).attr("id");
        var value = parseInt(id)
        var computeId = "compute_" + value;
        if ($("#" + computeId) != 'undefined') {
            $("#" + computeId).css('display', 'none')
        }
        $('.ui-dialog').width('600px');
    });
}
function toggleVisibility(node) {

    $.each($('#project').jstree('get_selected').children().filter('a'), function (index, val) {

        var id = $(val).attr('id');

        toggleVisibilityById(id)
    });
}

function toggleVisibilityById(id) {
    var object = group.getChildByName(id);

    var isNotParentMesh = $("#" + id).hasClass('showObject');

    if (object) {
        if (isNotParentMesh) {
            toggleVisibilityByObject(object);
        }
    } else {
        if (isNotParentMesh) {
            debugStatement("Object not found in group and this object is not parent mesh");
            showShape(id);
        } else {
            debugStatement("Object " + id + " is parent mesh")
            $.each($("#" + id).parent().children().filter('ul').children().filter('li').children().filter('ul').children().children().filter('a'), function (index, value) {
                id = $(value).attr('id')
                toggleVisibilityById(id)
            })
        }
    }
}

function toggleVisibilityByObject(object) {
    var objectId = object.name;

    if (object) {
        debugStatement("Object " + object.name + " is not parent mesh")
        var color = '';
        var visible;
        if (object.visible) {
            $("#" + objectId).css('border', '0px')
            visible = false;
            color = getColorForRepaint(object.name);
        } else {
            highlightInProjectTree(objectId);
            color = getColorForSelection(object.name);
            visible = true;
        }
        if (object.children != '' && object.children != undefined && object.children.length > 0) {
            if (object.children[0].visible) {
                highlightInProjectTree(objectId);
                object.children[0].visible = false;
                color = getColorForRepaint(object.name);
            } else {
                $("#" + objectId).css('border', '1px solid red')
                color = getColorForSelection(object.name);
                object.children[0].visible = true;
            }
            $.each($("#" + objectId).parent().parent().children().children().filter('a'), function (index, val) {

                var newObjectId = $(val).attr('id')
                var otherObjects = group.getChildByName(newObjectId);
                if (otherObjects != undefined && otherObjects.children != '' && otherObjects.children != undefined && otherObjects.children.length > 0 && objectId != newObjectId) {
                    if (otherObjects.children[0].visible) {
                        $("#" + newObjectId).css('border', '0px')
                        otherObjects.children[0].visible = false;
                    }
                }
            });


        }
        setVisible(object, visible);
        setColor(object, color);
    } else {
        debugStatement("No object found for toggle visibility")
    }
}

function setColor(object, color) {
    if (object.name) {
        debugStatement("Setting color -: " + color + " of id -: " + object.name);
        if (object.material != undefined) {
            object.material.color.setHex(color);
            window.localStorage["color_" + object.name] = color;
        }
    }
}

function setVisible(object, visible) {
    if (object.name) {
        debugStatement("Setting visible " + visible + " for -: " + object.name)
        object.visible = visible;
    }
}

function setActualColorOnLocal(name, color) {
    window.localStorage["actualColor_" + name] = color;
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
    if (typeof(console) != 'undefined' && debug == true) {
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
        if ($("#" + value).siblings().find('ul').length > 0) {
            removeChildren(value);
        }
    })
}

function removeChildren(id) {
    var rel = $($("#" + id).parent()).attr('rel');
    if (rel == "MESH") {
        var ids = [];
        $.each($("a.showObject", $("#" + id).parent()), function (index, value) {
            ids.push($(value).attr('id'))
        })
        removeObjects(ids);
    }
}

function removeFromLocalStorage(value) {
    localStorage.removeItem("color_" + value);
    localStorage.removeItem("visible_" + value);
    localStorage.removeItem("scalingFactor_" + value);
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
                            showFlashError(response.error);
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
            highLightVisible();
        }
    })
}

function repaint() {
    debugStatement("Repainting the group");
    $.each($("#phtml_1").children().find('li'), function () {
        var className = $(this).attr('rel')
        var id = $(this).find('a').attr('id');
        var object = scene.getChildByName(id + '', true);
        if (object) {
            debugStatement("Repainting " + id + " to color " + objectColor);
            setColor(object, getColorForRepaint(id));
        } else {
            debugStatement("object not found for coloring -: " + id)
        }
    })
}

function getColorForRepaint(id) {
    return window.localStorage["actualColor_" + id]
}
function getColorForSelection(id) {
    var color = getLocalColor(id);
    if (!color) {
        color = selectionColor
    }
    return color;
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

function isNumberKey(evt) {

    var charCode = (evt.which) ? evt.which : event.keyCode;

    if (charCode == 46)
        return true
    else if (charCode > 31 && (charCode < 48 || charCode > 57))
        return false;

    return true;
}

function getCurrentlyVisibleIds() {
    var ids = [];
    $(group.children).each(function () {
        if (this.name && this.visible) {
            var id = parseInt(this.name);
            ids.push(id)
        }
    })
    return ids;
}